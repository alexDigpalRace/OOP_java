package db.exceptions;

public class DBInvalidOperatorException extends DBException {
	String word;
	public DBInvalidOperatorException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBInvalidOperatorException: " + word;
	}
}
