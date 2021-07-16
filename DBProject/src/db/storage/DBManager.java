package db.storage;

import db.exceptions.DBDoesNotExistException;
import db.exceptions.DBException;

import java.util.ArrayList;

public class DBManager {
	private ArrayList<DBTableManager> databases;
	private String currentDatabase;

	public DBManager() {
		this.databases = new ArrayList<DBTableManager>();
	}

	public DBTableManager getDatabase(String name) throws DBDoesNotExistException {
		for (int i = 0; i < databases.size(); i++) {
			if (databases.get(i).getName().equals(name)) {
				return databases.get(i);
			}
		}
		throw new DBDoesNotExistException();
	}

	public DBTableManager getDatabase(int index) throws DBException {
		return databases.get(index);
	}

	//gets the saved database selected by the user
	public String getCurrentDatabase() {
		return currentDatabase;
	}

	//sets the index of the database the user wants
	public void setCurrentDatabase(String name) {
		currentDatabase = name;
	}

	//returns the index of the database just created
	public int addDatabase(String name)
	{
		databases.add(new DBTableManager(name));
		return databases.size() - 1;
	}

	public int getSize()
	{
		return databases.size();
	}

	public void removeDatabase(String name) throws DBDoesNotExistException {
		databases.remove(this.getDatabase(name));
	}
}
