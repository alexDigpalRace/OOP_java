package db.parser;

import db.exceptions.DBException;
import db.exceptions.DBInvalidNameException;

//expect a name to be checked and set
public class DBUse extends DBCmd {

	public DBUse(Tokens commands) throws DBInvalidNameException {
		super.isValidName(commands.getCurrentCommand());
		commands.setDbName(commands.nextCommand());
	}
}
