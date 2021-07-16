package db.parser;

import db.exceptions.DBCommandTypeInvalidException;
import db.exceptions.DBException;
import db.exceptions.DBInvalidNameException;
import db.exceptions.DBNotKeyWordException;

public class DBCommandType {
	DBCmd command;
	public DBCommandType(Tokens commands) throws DBException {
		commands.setDbCommandType(commands.getCurrentCommand().toUpperCase());
		switch (commands.nextCommand().toUpperCase()) {
			case "USE":
				command = new DBUse(commands);
				break;
			case "CREATE":
				command = new DBCreate(commands);
				break;
			case "DROP":
				command = new DBDrop(commands);
				break;
			case "ALTER":
				command = new DBAlter(commands);
				break;
			case "INSERT":
				command = new DBInsert(commands);
				break;
			case "SELECT":
				command = new DBSelect(commands);
				break;
			case "UPDATE":
				command = new DBUpdate(commands);
				break;
			case "DELETE":
				command = new DBDelete(commands);
				break;
			case "JOIN":
				command = new DBJoin(commands);
				break;
			default:
				//bad syntax, expected a Command-type
				throw new DBCommandTypeInvalidException(commands.getCurrentCommand());
		}
	}
}
