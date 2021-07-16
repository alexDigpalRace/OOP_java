package db.exceptions;

public class DBColumnDoesNotExistException extends DBException {
	String name;
	public DBColumnDoesNotExistException(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DBColumnDoesNotExistException: " + name;
	}
}
