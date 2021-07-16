package db.parser;

import db.exceptions.DBException;
import db.exceptions.DBMissingDelimiterException;

public class DBInsert extends DBCmd {
	boolean done = false;

	public DBInsert(Tokens commands) throws DBException {
		this.isInto(commands.nextCommand().toUpperCase());
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());      //saves the table name for reference on execution
		this.isValues(commands.nextCommand().toUpperCase());
		//TODO following VALUES LIST, own method as repeatable?
		this.isOpeningBracket(commands.nextCommand());
		while (!done) {
			this.isValue(commands.getCurrentCommand());
			commands.addData(commands.nextCommand());       //add to the data list
			try {
				this.isComma(commands.getCurrentCommand());
				commands.incrementIndex(1);
			} catch (DBMissingDelimiterException e) {
				this.isClosingBracket(commands.nextCommand());
				done = true;
			}
		}
	}
}
