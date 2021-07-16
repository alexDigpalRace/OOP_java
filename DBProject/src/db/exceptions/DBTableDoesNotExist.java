package db.exceptions;

public class DBTableDoesNotExist extends DBException {
	String name;
	public DBTableDoesNotExist(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DBTableDoesNotExist: " + name;
	}
}
