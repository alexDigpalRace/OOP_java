package db.exceptions;

public class DBMissingBracketException extends DBException{
	String word;

	public DBMissingBracketException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBMissingBracketException near:" + word;
	}
}
