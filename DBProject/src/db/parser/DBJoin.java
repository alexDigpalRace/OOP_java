package db.parser;

import db.exceptions.DBException;

public class DBJoin extends DBCmd {
	public DBJoin(Tokens commands) throws DBException {
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());
		this.isAnd(commands.nextCommand().toUpperCase());
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());
		this.isOn(commands.nextCommand().toUpperCase());
		this.isValidString(commands.getCurrentCommand());
		commands.addColNames(commands.nextCommand());
		this.isAnd(commands.nextCommand().toUpperCase());
		this.isValidString(commands.getCurrentCommand());
		commands.addColNames(commands.nextCommand());
	}
}
