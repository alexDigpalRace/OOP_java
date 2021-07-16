package db.exceptions;

public class DBNumFormatException extends DBException {
	@Override
	public String toString() {
		return "DB Num Format Exception - cannot convert string to float/int";
	}
}
