package db.parser;

import db.exceptions.*;

public class DBDelete extends DBCmd {
	boolean done = false;

	public DBDelete(Tokens commands) throws DBException {
		this.isFrom(commands.nextCommand().toUpperCase());
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());
		this.isWhere(commands.nextCommand().toUpperCase());
		this.isACondition(commands);
	}
}
