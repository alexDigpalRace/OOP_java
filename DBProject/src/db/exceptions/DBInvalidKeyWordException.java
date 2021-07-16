package db.exceptions;

public class DBInvalidKeyWordException extends DBException {
	String word;

	public DBInvalidKeyWordException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "Invalid key word: " + word;
	}
}
