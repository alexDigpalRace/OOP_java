package db.storage;

import db.exceptions.DBColumnDoesNotExistException;

import java.util.ArrayList;

public class DBTable {

	private String name;
	private ArrayList<DBColumn> table;

	//only need to set the name once, when creating it...
	public DBTable(String name) {
		this.name = name;
		table = new ArrayList<DBColumn>();
	}

	public String getName() {
		return name;
	}

	//returns the column of a specified row
	public DBColumn getColumnAt (int index) {
		return table.get(index);
	}

	public DBColumn getColumn(String name) throws DBColumnDoesNotExistException {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getName().equals(name)) {
				return table.get(i);
			}
		}
		throw new DBColumnDoesNotExistException(name);
	}

	//NAMED RIGHT!
	public void addColumn(String name) {
		table.add(new DBColumn(name));
	}

	//the size of the row that holds reference to all the columns, thought the name was less confusing below
	public int getNumOfCols ()
	{
		return table.size();
	}

	public void removeColumn(String name) throws DBColumnDoesNotExistException {
		table.remove(this.getColumn(name));
	}
}
