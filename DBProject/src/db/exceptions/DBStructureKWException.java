package db.exceptions;

public class DBStructureKWException extends DBInvalidKeyWordException {
	public DBStructureKWException(String word) {
		super(word);
	}

	@Override
	public String toString() {
		return "DBStructureKWException: Near " + word +
				" Missing structure (DATABASE or TABLE)";
	}
}
