package db.exceptions;

public class DBLIKENOTIMPLEMENTEDEXCEPTION extends DBException {
	@Override
	public String toString() {
		return "DB LIKE NOT IMPLEMENTED, at this point ive given up " +
				"go away with your like conditions";
	}
}
