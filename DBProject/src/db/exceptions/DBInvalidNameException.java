package db.exceptions;

public class DBInvalidNameException extends DBException {
	String word;

	public DBInvalidNameException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBInvalidNameException: " + word;
	}
}
