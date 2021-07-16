package db.parser;

import db.exceptions.DBException;
import db.exceptions.DBInvalidKeyWordException;

import java.util.NoSuchElementException;

public class DBSelect extends DBCmd{
	public DBSelect(Tokens commands) throws DBException {
		try {
			this.isWildcard(commands.getCurrentCommand());
			commands.addColNames(commands.nextCommand());
		} catch (DBInvalidKeyWordException e) {
			this.isAttributeList(commands);
		}

		this.isFrom(commands.nextCommand().toUpperCase());
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());

		//if user wants to specify a condition/s
		try {
			this.isWhere(commands.nextCommand().toUpperCase());
			this.isCondition(commands);
		} catch (IndexOutOfBoundsException e) {
			//do nothing, query finished
		}
	}
}
