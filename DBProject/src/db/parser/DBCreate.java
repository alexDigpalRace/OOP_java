package db.parser;

import db.exceptions.*;

import java.util.NoSuchElementException;

public class DBCreate extends DBCmd {
	boolean done = false;
	public DBCreate(Tokens commands) throws DBException {
		commands.setCommandTypeSpecifier(commands.getCurrentCommand().toUpperCase());

		switch (commands.nextCommand().toUpperCase()) {
			case "DATABASE":
				this.isValidName(commands.getCurrentCommand());
				commands.setDbName(commands.nextCommand());
				break;
			case "TABLE":
				this.isValidName(commands.getCurrentCommand());
				commands.addTableName(commands.nextCommand());
				try {
					this.isOpeningBracket(commands.nextCommand());
					this.isAttributeList(commands);
					this.isClosingBracket(commands.nextCommand());
				} catch (IndexOutOfBoundsException e) {
					//end of user query, do nothing...
				}
				break;
			default:
				throw new DBStructureKWException(commands.getCurrentCommand());
		}
	}
}
