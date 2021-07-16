package db.parser;

import db.exceptions.DBException;

public class DBAlter extends DBCmd {
	public DBAlter(Tokens commands) throws DBException {
		this.isTable(commands.nextCommand().toUpperCase());
		this.isValidName(commands.getCurrentCommand());
		commands.addTableName(commands.nextCommand());
		this.isAlterationType(commands.getCurrentCommand().toUpperCase());
		commands.setCommandTypeSpecifier(commands.nextCommand()); 	//save whether Alter should ADD or DROP
		this.isValidName(commands.getCurrentCommand());
		commands.addColNames(commands.nextCommand());
	}
}
