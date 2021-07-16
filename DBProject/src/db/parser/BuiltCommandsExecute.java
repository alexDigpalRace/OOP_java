package db.parser;

import db.exceptions.*;
import db.storage.DBColumn;
import db.storage.DBManager;
import db.storage.DBTable;
import db.storage.DBTableManager;

import java.util.ArrayList;
import java.util.List;

public class BuiltCommandsExecute {
	BuiltCommands builtCommands;
	DBManager databases;
	DBTableManager database;
	DBTable table;
	DBColumn column;         //TODO add DBColumn to reduce method sizing of loading
	ArrayList<ArrayList<String>> generatedUserTable;
	List<Integer> savedIndices;


	public BuiltCommandsExecute(DBManager databases, BuiltCommands builtCommands) throws DBException {
		this.databases = databases;
		this.builtCommands = builtCommands;
		savedIndices = new ArrayList<Integer>();

		switch (builtCommands.getCommandType()) {
			case "USE":
				this.executeUse();
				break;
			case "CREATE":
				this.executeCreate();
				break;
			case "DROP":
				this.executeDrop();
				break;
			case "ALTER":
				this.executeAlter();
				break;
			case "INSERT":
				this.executeInsert();
				break;
			case "SELECT":
				this.executeSelect();
				break;
			case "UPDATE":
				this.executeUpdate();
				break;
			case "DELETE":
				this.executeDelete();
				break;
			case "JOIN":
				this.executeJoin();
				break;
		}
	}

	//select a database to use
	private void executeUse() throws DBException {
		//check if database exists
		databases.getDatabase(builtCommands.getDbName());
		//save its state
		databases.setCurrentDatabase(builtCommands.getDbName());
	}

	//create a new database or table (and columns of table)
	private void executeCreate() throws DBException {
		switch (builtCommands.getCommandTypeSpecifier()) {
			case "DATABASE":
				databases.addDatabase(builtCommands.getDbName());
				break;
			case "TABLE":
				//shouldnt ever have more than one table name, so always index = 0
				database = databases.getDatabase(databases.getCurrentDatabase());
				database.addTable(builtCommands.getTableName(0));
				table = database.getTable(builtCommands.getTableName(0));
				//check if there are column names to add
				if (builtCommands.getColNames().size() > 0) {
					//loop through columns names to add to table + id so start on -1 as im lazy
					for (int i = -1; i < builtCommands.getColNames().size(); i++) {
						if (i == -1) {
							table.addColumn("id");
						} else {
							//add them
							table.addColumn(builtCommands.getColNames(i));
						}
					}
				}
				break;
			//already checked if its one of the cases
		}
	}

	//delete database or table
	private void executeDrop() throws DBException {
		switch (builtCommands.getCommandTypeSpecifier()) {
			case "DATABASE":
				databases.removeDatabase(builtCommands.getDbName());
				break;
			case "TABLE":
				database = databases.getDatabase(databases.getCurrentDatabase());
				//same as above
				database.removeTable(builtCommands.getTableName(0));
				break;
			//like above
		}
	}

	//add or remove attributes (Columns) from tables
	private void executeAlter() throws DBException {
		database = databases.getDatabase(databases.getCurrentDatabase());
		//can only reference one table so index always 0
		table = database.getTable(builtCommands.getTableName(0));
		switch (builtCommands.getCommandTypeSpecifier()) {
			case "ADD":
				//can only add col one at a time so index always 0
				table.addColumn(builtCommands.getColNames(0));
				//populate cells of column
				for (int i = 0; i < table.getColumnAt(0).getNumOfRows(); i++) {
					table.getColumnAt(table.getNumOfCols() - 1).addCell("");
				}
				break;
			case "DROP":
				//index reason like others
				table.removeColumn(builtCommands.getColNames(0));
				break;
			//same as above
		}
	}

	private void executeInsert() throws DBException {
		//retrieve saved database
		database = databases.getDatabase(databases.getCurrentDatabase());
		//retrieve table of above database
		table = database.getTable(builtCommands.getTableName(0)); //only one table so index 0
		//add the id
		Integer id = table.getColumnAt(0).getNumOfRows() + 1;
		table.getColumnAt(0).addCell(id.toString());
		//assuming values is only loading in A row at a time, go through the values
		for (int i = 0; i < builtCommands.getDataListSize(); i++) {
			//get the column +1 as ID column already populated
			table.getColumnAt(i + 1).addCell(builtCommands.getData(i));
		}
	}

	//sends back the String to be printed to the user
	private void executeSelect() throws DBException {
		//retrieve saved database
		database = databases.getDatabase(databases.getCurrentDatabase());
		//retrieve table
		table = database.getTable(builtCommands.getTableName(0)); //only one table
		//apply conditions to get appropriate rows (indexes of which saved in savedIndices)
		this.applyConditions();
		//if wildcard then print whole table
		if (builtCommands.getColNames(0).equals("*")) {
			//generate the whole table
			this.generateTable();
		} else {
			//else generate table from cols specified
			this.generateTable(builtCommands.getColNames());
		}
	}

	//find the row/s that match the condition and set its value
	private void executeUpdate() throws DBException {
		//retrieve saved database
		database = databases.getDatabase(databases.getCurrentDatabase());
		//retrieve table
		table = database.getTable(builtCommands.getTableName(0)); //only one table
		//find rows that match the condition
		this.applyConditions();
		//loop through list of colNames
		for (int i = 0; i < builtCommands.getColNamesSize(); i++) {
			column = table.getColumn(builtCommands.getColNames(i));
			//loop through the row of the column
			for (int j = 0; j < savedIndices.size(); j++) {
				//edit the cell accordingly
				column.setCell(savedIndices.get(j), builtCommands.getData(j));
			}
		}
	}

	private void executeDelete() throws DBException {
		Integer id;
		int deleteCount = 0;
		//retrieve saved database
		database = databases.getDatabase(databases.getCurrentDatabase());
		//retrieve table
		table = database.getTable(builtCommands.getTableName(0)); //only one table
		//find row/s that match condition
		this.applyConditions();
		//loop through rows to be deleted
		for (int i = 0; i < savedIndices.size(); i++) {
			//loop through columns of each
			for (int j = 0; j < table.getNumOfCols(); j++) {
				//delete row from column
				table.getColumnAt(j).removeCell(savedIndices.get(i) - deleteCount);
			}
			//whole row deleted so increment deleteCount
			deleteCount++;
		}
		//reset ID's
		for (int i = 0; i < table.getColumnAt(0).getNumOfRows(); i++) {
			table.getColumnAt(0).setCell(i, (id = i + 1).toString());
		}
	}

	private void executeJoin() throws DBException {
		generatedUserTable = new ArrayList<>();
		//retrieve saved database
		database = databases.getDatabase(databases.getCurrentDatabase());
		//retrieve first table
		table = database.getTable(builtCommands.getTableName(0)); //only one table
		//retrieve second table
		DBTable table2 = database.getTable(builtCommands.getTableName(1));

		//retrieve columns of both tables to join
		column = table.getColumn(builtCommands.getColNames(0));
		DBColumn column2 = table2.getColumn(builtCommands.getColNames(1));

		//populate user table with table1 columns
		for (int i = 0; i < table.getNumOfCols(); i++) {
			if (table.getColumnAt(i) != column) {
				//add a col to copy of data to
				generatedUserTable.add(new ArrayList<String>());
				//add name to created col
				generatedUserTable.get(generatedUserTable.size() - 1).add(table.getColumnAt(i).getName());
				//loop throw row cells of the column and add to created column
				for (int j = 0; j < table.getColumnAt(i).getNumOfRows(); j++) {
					//get col created and add table cells to it
					generatedUserTable.get(generatedUserTable.size() - 1).add(table.getColumnAt(i).getCell(j));
				}
			}
		}

		//same thing with table 2 columns
		for (int i = 0; i < table2.getNumOfCols(); i++) {
			//block the column thats being joined
			if (table2.getColumnAt(i) != column2) {
				//add a col to copy of data to
				generatedUserTable.add(new ArrayList<String>());
				//add name to created col
				generatedUserTable.get(generatedUserTable.size() - 1).add(table2.getColumnAt(i).getName());
				//loop throw row cells of the column and add to created column
				for (int j = 0; j < table2.getColumnAt(i).getNumOfRows(); j++) {
					//get col created and add table cells to it
					generatedUserTable.get(generatedUserTable.size() - 1).add(table2.getColumnAt(i).getCell(j));
				}
			}
			//TODO doesnt work
		}
	}

	//generate table with all columns
	private void generateTable() {
		generatedUserTable = new ArrayList<>();
		//generate names
		//loop through columns
		for (int i = 0; i < table.getNumOfCols(); i++) {
			//add a column to the use generate table for each column in table
			generatedUserTable.add(new ArrayList<String>());
			//add the name of the column
			generatedUserTable.get(i).add(table.getColumnAt(i).getName());
		}
		//generate values
		//loop through the rows specified in savedIndices
		for (int i = 0; i < savedIndices.size(); i++) {
			//for each row loop through the columns to add the table data
			for (int j = 0; j < generatedUserTable.size(); j++) {
				generatedUserTable.get(j).add(table.getColumnAt(j).getCell(savedIndices.get(i)));
			}
		}
	}

	//generate specific column table only
	private void generateTable(List<String> colNames) throws DBColumnDoesNotExistException {
		generatedUserTable = new ArrayList<>();
		//generate names
		//loop through columns +1 for ID
		for (int i = 0; i < colNames.size(); i++) {
			//add a column to the user generated table for each column in table
			generatedUserTable.add(new ArrayList<String>());
			//add the name of the column
			generatedUserTable.get(i).add(table.getColumn(colNames.get(i)).getName());
		}
		//generate values
		//loop through the rows specified in savedIndices
		for (int i = 0; i < savedIndices.size(); i++) {
			//for each row loop through the columns to add the table data
			for (int j = 0; j < generatedUserTable.size(); j++) {
				generatedUserTable.get(j).add(table.getColumn(colNames.get(j)).getCell(savedIndices.get(i)));
			}
		}
	}

	//choose which rows to print
	private void applyConditions() throws DBException {
		DBCondition cond;
		//check if there are conditions
		if (builtCommands.conditionSize() > 0) {
			//loop through conditions
			for (int i = 0; i < builtCommands.conditionSize(); i++) {
				//load the condition
				cond = builtCommands.getConditions(i);
				//decide datatype
				if (condIsNumeric(cond)) {
					evaluateNumOperator(cond);
				} else {
					evaluateWordOperator(cond);
				}
				//if two conditions apply AND or OR to saved indices
				if (i > 0) {
					switch (builtCommands.getCondChainMod(i - 1)) {
						case "AND":
							//delete indices that only appear once
							this.deleteIndexNonDuplicates();
							//now only left with indices that satified both conds, delete the dup
							this.deleteIndexDuplicates();
						default:
							//delete any duplicates
							this.deleteIndexDuplicates();
					}
				}
			}
		} else {
			//add all row index'
			for (int i = 0; i < table.getColumnAt(0).getNumOfRows(); i++) {
				savedIndices.add(i);
			}
		}
	}

	private void evaluateNumOperator(DBCondition cond) throws DBException {
		Float conditionValue = Float.parseFloat(cond.getValue());
		Float columnValue;
		ArrayList<Integer> newIndices = new ArrayList<Integer>();
		//get column that condition specifies
		column = table.getColumn(cond.getColumnName());
		//loop through the column to find rows that match condition
		for (int i = 0; i < column.getNumOfRows(); i++) {
			try { columnValue = Float.parseFloat(column.getCell(i)); }
			catch (NumberFormatException e) { throw new DBNumFormatException(); }
			switch (cond.getOperator()) {
				case ">":
					if (columnValue > conditionValue) {
						newIndices.add(i);
					}
					break;
				case "<":
					if (columnValue < conditionValue) {
						newIndices.add(i);
					}
					break;
				case ">=":
					if (columnValue >= conditionValue) {
						newIndices.add(i);
					}
					break;
				case "<=":
					if (columnValue <= conditionValue) {
						newIndices.add(i);
					}
					break;
				default:
					//shouldn't ever happen
					throw new DBInvalidOperatorException(cond.getOperator());
			}
		}
		this.addNewIndicesToMain(newIndices);
	}

	private void evaluateWordOperator(DBCondition cond) throws DBException {
		String conditionValue = cond.getValue();
		String columnValue;
		ArrayList<Integer> newIndices = new ArrayList<Integer>();
		//get column that condition specifies
		column = table.getColumn(cond.getColumnName());
		//loop through the column to find rows that match condition
		for (int i = 0; i < column.getNumOfRows(); i++) {
			columnValue = column.getCell(i);
			switch (cond.getOperator()) {
				case "==":
					if (columnValue.equals(conditionValue)) {
						newIndices.add(i);
					}
					break;
				case "!=":
					if (!columnValue.equals(conditionValue)) {
						newIndices.add(i);
					}
					break;
				case "LIKE":
					this.like(cond);
					break;
				default:
					//shouldn't ever happen
					throw new DBInvalidOperatorException(cond.getOperator());
			}
		}
		this.addNewIndicesToMain(newIndices);
	}

	private boolean condIsNumeric(DBCondition cond) {
		if (cond.getValue().charAt(0) == '\'' || cond.getValue().toLowerCase().equals("true") ||
				cond.getValue().toLowerCase().equals("false")) {
			return false;
		}
		return true;
	}


	private void addNewIndicesToMain(List<Integer> newIndices) {
		for (int i = 0; i < newIndices.size(); i++) {
			savedIndices.add(newIndices.get(i));
		}
	}

	//for finding unique rows that have been OR'd
	private void deleteIndexDuplicates() {
		for (int i = 0; i < savedIndices.size(); i++) {
			for (int j = 0; j < savedIndices.size(); j++) {
				if (i != j) {
					if (savedIndices.get(i) == savedIndices.get(j)) {
						savedIndices.remove(savedIndices.get(j));
					}
				}
			}
		}
	}

	//for finding unique rows that have been AND'd
	private void deleteIndexNonDuplicates() {
		for (int i = 0; i < savedIndices.size(); i++) {
			boolean found = false;
			for (int j = 0; j < savedIndices.size(); j++) {
				if (i != j) {
					if (savedIndices.get(i) == savedIndices.get(j)) {
						found = true;
					}
				}
			}
			if (!found) {
				savedIndices.remove(i);
				i--;
			}
		}
	}

	//TODO implement like
	private boolean like(DBCondition condition) {
		return false;
	}

	public ArrayList<ArrayList<String>> getUserTable() {
		return generatedUserTable;
	}
}
