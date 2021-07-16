package db.exceptions;

public class DBInvalidDataValueException extends DBException {
	String word;
	public DBInvalidDataValueException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return ("DBInvalidDataValueException: " + word);
	}
}
