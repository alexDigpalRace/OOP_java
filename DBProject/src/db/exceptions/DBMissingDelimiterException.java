package db.exceptions;

public class DBMissingDelimiterException extends DBException {
	String word;

	public DBMissingDelimiterException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBMissingDelimiterException: Expected delimiter near: " + word;
	}
}
