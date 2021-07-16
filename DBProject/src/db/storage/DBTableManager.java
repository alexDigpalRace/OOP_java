package db.storage;

import db.exceptions.DBTableDoesNotExist;

import java.util.ArrayList;

/*Keeps track of what tables are stored in a particular instance of a database*/

public class DBTableManager {

	private String name;                        //name of table manager (database)
	private ArrayList<DBTable> tableList;

	//constructor
	public DBTableManager(String databaseName) {
		this.tableList = new ArrayList<DBTable>();
		this.name = databaseName;   //e.g. Database 1
	}

	//
	public DBTable getTable(String name) throws DBTableDoesNotExist {
		for (int i = 0; i < tableList.size(); i++) {
			if (tableList.get(i).getName().equals(name)) {
				return tableList.get(i);
			}
		}
		throw new DBTableDoesNotExist(name);
	}

	public DBTable getTable(int index) throws DBTableDoesNotExist {
		return tableList.get(index);
	}

	public int addTable(String name) {
		tableList.add(new DBTable(name));
		return tableList.size()-1;
	}

	public int getSize() {
		return tableList.size();
	}

	public String getName()
	{
		return this.name;
	}

	//for debugging
	public void printExistingTables()
	{
		System.out.println("\nCurrent tables in database:");
		for (int i = 0; i < tableList.size(); i++) {
			System.out.println(tableList.get(i).getName());
		}
	}

	public String getStringExistingTables()
	{
		String message = new String();
		for (int i = 0; i < tableList.size(); i++) {
			message += (tableList.get(i).getName() + "\n");
		}
		return message;
	}

	public void removeTable(String tableName) throws DBTableDoesNotExist {
		tableList.remove(this.getTable(tableName));
	}
}
