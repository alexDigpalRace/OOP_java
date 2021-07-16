package db.exceptions;

public class DBCommandTypeInvalidException extends DBException {
	String word;
	public DBCommandTypeInvalidException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBCommandTypeInvalidException: " + word;
	}
}

