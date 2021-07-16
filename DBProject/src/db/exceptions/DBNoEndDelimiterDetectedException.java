package db.exceptions;

public class DBNoEndDelimiterDetectedException extends DBException {
	String word;
	public DBNoEndDelimiterDetectedException(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "DBNoEndDelimiterDetectedException missing end delimiter ; near: " + word +
				"(FOR DEV: possibly commands still left to check?)";
	}
}
