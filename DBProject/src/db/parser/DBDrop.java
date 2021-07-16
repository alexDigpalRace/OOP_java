package db.parser;

import db.exceptions.DBInvalidNameException;

//delete a table or database
public class DBDrop extends DBCmd {
	public DBDrop(Tokens commands) throws DBInvalidNameException {
		commands.setCommandTypeSpecifier(commands.getCurrentCommand().toUpperCase());
		switch (commands.nextCommand().toUpperCase()) {
			case "DATABASE":
				this.isValidName(commands.getCurrentCommand());
				commands.setDbName(commands.nextCommand());
				break;
			case "TABLE":
				this.isValidName(commands.getCurrentCommand());
				commands.addTableName(commands.nextCommand());
				break;
			default:
		}
	}
}
