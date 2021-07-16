package db.parser;

import db.exceptions.*;

import java.util.NoSuchElementException;

public abstract class DBCmd {
	//check alphanumeric
	public void isValidName(String name) throws DBInvalidNameException {
		//thanks: https://stackoverflow.com/questions/8248277/how-to-determine-if-a-string-has-non-alphanumeric-characters
		//check between start and end for any chars not in specified ranges
		if (name.matches("^.*[^a-zA-Z0-9'].*$")) {
			throw new DBInvalidNameException(name);
		}
	}

	//check string literal
	public void isValidString(String name) throws DBInvalidNameException {
		//thanks: https://stackoverflow.com/questions/8248277/how-to-determine-if-a-string-has-non-alphanumeric-characters
		//check between start and end for any chars not in specified ranges
		if (name.matches("^.*[\t\'].*$")) {
			throw new DBInvalidNameException(name);
		}
	}

	public void isInto(String word) throws DBInvalidKeyWordException {
		if (!word.equals("INTO")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isValues(String word) throws DBInvalidKeyWordException {
		if (!word.equals("VALUES")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isOpeningBracket(String word) throws DBMissingBracketException {
		if (!word.equals("(")) {
			throw new DBMissingBracketException(word);
		}
	}

	public boolean isClosingBracket(String word) throws DBInvalidKeyWordException {
		if (!word.equals(")")) {
			throw new DBInvalidKeyWordException(word);
		}
		return true;
	}

	public void isComma(String word) throws DBMissingDelimiterException {
		//find any chars specified in square brackets, for practicing regex
		if (word.matches("^.*[^,].*$")) {
			throw new DBMissingDelimiterException(word);
		}
	}

	public void isSet(String word) throws DBInvalidKeyWordException {
		if (!word.equals("SET")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isWhere(String word) throws DBInvalidKeyWordException {
		if(!word.equals("WHERE")){
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isEq(String word) throws DBInvalidOperatorException {
		if(!word.equals("=")){
			throw new DBInvalidOperatorException(word);
		}
	}

	public void isValue(String word) throws DBInvalidDataValueException {
		if(!(this.isStringLiteral(word) || this.isBooleanLiteral(word) ||
			 this.isFloatLiteral(word) || this.isIntegerLiteral(word))){
			throw new DBInvalidDataValueException(word);
		}
	}

	//TODO throw different exception
	public boolean isStringLiteral(String word) {
		System.out.println("checking string");
		try {
			if (word.charAt(0) == '\'' && word.charAt(word.length()-1) == '\'') {
				String newWord = word.substring(1, (word.length()-1));
				this.isValidString(newWord);
				return true;
			}
			return false;
		} catch (DBInvalidNameException e) {
			return false;
		}
	}

	public boolean isBooleanLiteral(String word) {
		return (word.equals("true") || word.equals("false"));
	}

	//TODO test if something like word="hel23.2asd" gets rejected
	public boolean isFloatLiteral(String word) {
		try {
			Float f = Float.parseFloat(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	//TODO test if it blocks bad ints e.g. word="34k"
	public boolean isIntegerLiteral(String word) {
		try {
			Integer i = Integer.parseInt(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void isFrom(String word) throws DBInvalidKeyWordException {
		if (!word.equals("FROM")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isOp(String word) throws DBException {
		switch (word) {
			case "==":
			case ">":
			case "<":
			case ">=":
			case "<=":
			case "!=":
				break;
			case "LIKE":
				throw new DBLIKENOTIMPLEMENTEDEXCEPTION();
			default :
				throw new DBInvalidOperatorException(word);
		}
	}

	public void isAnd(String word) throws DBInvalidKeyWordException {
		if (!word.equals("AND")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isOr(String word) throws DBInvalidKeyWordException {
		if (!word.equals("OR")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isAndOr(String word) throws DBInvalidKeyWordException {
		try {
			this.isAnd(word);
		} catch (DBInvalidKeyWordException e) {
			this.isOr(word);
		}
	}

	public void isOn(String word) throws DBInvalidKeyWordException {
		if (!word.equals("ON")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isDatabase(String word) throws DBStructureKWException {
		if (!word.equals("DATABASE")) {
			throw new DBStructureKWException(word);
		}
	}

	public void isTable(String word) throws DBStructureKWException {
		if (!word.equals("TABLE")) {
			throw new DBStructureKWException(word);
		}
	}

	public void isAlterationType(String word) throws DBInvalidKeyWordException {
		if ((!word.equals("ADD")) && (!word.equals("DROP"))) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	public void isWildcard(String word) throws DBInvalidKeyWordException {
		if (!word.equals("*")) {
			throw new DBInvalidKeyWordException(word);
		}
	}

	//list of names separated by commas
	public void isAttributeList(Tokens commands) throws DBException {
		boolean done = false;
		while (!done) {
			this.isValidString(commands.getCurrentCommand());
			commands.addColNames(commands.nextCommand());
			try {
				this.isComma(commands.getCurrentCommand());
				commands.incrementIndex(1);
			} catch (DBMissingDelimiterException e) {
				done = true;
				//TODO could be missed/mispelled comma, closing bracket would be end of query
			}
		}
	}

	public void isACondition(Tokens commands) throws DBException {
		String colName;
		String operator;
		String value;

		this.isValidName(commands.getCurrentCommand());
		colName = commands.nextCommand();
		this.isOp(commands.getCurrentCommand());
		operator = commands.nextCommand();
		this.isValue(commands.getCurrentCommand());
		value = commands.nextCommand();

		commands.addConditions(new DBCondition(colName, operator, value));
	}
	/* TODO scenario: opening bracket missing so only one condition checked when there could
	    be multiple...*/
	public void isCondition(Tokens commands) throws DBException {
		boolean done = false;
		//check command for brackets (multiple conditions)
		try {
			while (!done) {
				this.isOpeningBracket(commands.getCurrentCommand());
				commands.incrementIndex(1);
				this.isACondition(commands);
				this.isClosingBracket(commands.nextCommand());
				//don't go forward so can evaluate OR, catch exception
				try {
					this.isAndOr(commands.getCurrentCommand());
					commands.addCondChainMod(commands.nextCommand());
				} catch (NoSuchElementException e) {
					done = true;
				}
			}
		} catch (DBMissingBracketException e) {
			//ONLY ONE condition
			this.isACondition(commands);
		}
	}

	public void isNameValuePair(Tokens commands) throws DBException {
		this.isValidName(commands.getCurrentCommand());
		commands.addColNames(commands.nextCommand());
		this.isEq(commands.nextCommand());
		this.isValue(commands.getCurrentCommand());
		commands.addData(commands.nextCommand());
	}

	public void isNameValueList(Tokens commands) throws DBException {
		boolean done = false;
		while (!done) {
			this.isNameValuePair(commands);
			try {
				this.isComma(commands.getCurrentCommand());
				commands.incrementIndex(1);
			} catch (DBMissingDelimiterException e) {
				done = true;
			}
		}
	}
}
