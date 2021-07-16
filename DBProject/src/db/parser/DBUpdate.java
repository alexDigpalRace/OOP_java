package db.parser;

import db.exceptions.*;

public class DBUpdate extends DBCmd {
	public DBUpdate(Tokens commands) throws DBException {
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());
		this.isSet(commands.nextCommand().toUpperCase());
		this.isNameValueList(commands);
		this.isWhere(commands.nextCommand().toUpperCase());
		this.isCondition(commands);
	}
}
