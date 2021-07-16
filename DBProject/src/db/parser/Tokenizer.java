package db.parser;

import db.exceptions.DBException;
import db.exceptions.DBNoEndDelimiterDetectedException;
import db.storage.DBManager;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Tokenizer {
	private Tokens commands;
	private BuiltCommandsExecute builtCommandsExecute;
	private boolean done = false;

	public Tokenizer(String query) throws DBNoEndDelimiterDetectedException {
		commands = new Tokens();
		String command = new String();
		boolean inString = false;
		int count = 0;
		//loop through each character of query
		for (int i = 0; i < query.length(); i++) {
			Character character = query.charAt(i);
			//doesnt work with \n or \r, if whitespace full word found so add to commands
			if ((character == ' ' || character == '\t') && !inString) {
				//work around out of bounds exceptions
				if (i != 0) {
					//if leading char was a delimiter or whitespace no need to add + reset
					if (query.charAt(i - 1) != '(' && query.charAt(i - 1) != ')' &&
						query.charAt(i - 1) != ',' && query.charAt(i - 1) != '*' &&
						query.charAt(i - 1) != ' ' && query.charAt(i - 1) != '\t') {
						if (command.length() > 0) {
							commands.addCommand(command);
						}
						command = new String();
					}
				}
			}
			//delimiter hit, two branches depending on trailing char being non/whitespace
			else if (character == '(' || character == ')' || character == ',' || character == '*') {
				//whitespace or \' before char, means command before has already been sent
				if (query.charAt(i - 1) == ' ' || query.charAt(i - 1) == '\t') {
					commands.addCommand(character.toString());
					//no whitespace so send off word before, reset word, add self, reset again
				} else {
					commands.addCommand(command);
					commands.addCommand(character.toString());
				}
				command = new String();
			}
			//throw exception if last char isnt a ; dont send semi colon into commands
			else if (i == query.length() - 1) {
				if (character == ';') {
					//check character before endQuery char isnt whitespace or delim
					if (query.charAt(i - 1) != '(' && query.charAt(i - 1) != ')' &&
						query.charAt(i - 1) != ',' && query.charAt(i - 1) != '*' &&
						query.charAt(i - 1) != ' ' && query.charAt(i - 1) != '\t') {
						commands.addCommand(command);
					}
				} else {
					throw new DBNoEndDelimiterDetectedException(command);
				}
			}
			//for string literals
			else if (character == '\'') {
				if (count == 0) {
					inString = true;
					count++;
				} else {
					count = 0;
					inString = false;
				}
				command += character;
			} else {
				if (character != '\n') {
					command += character;
				}
			}
		}
	}

	public void parse() throws DBException {
		DBCommandType start = new DBCommandType(commands);
	}

	public ArrayList<ArrayList<String>> execute(DBManager databases) throws DBException {
		builtCommandsExecute = new BuiltCommandsExecute(databases, commands.getBuiltCommands());
		return builtCommandsExecute.getUserTable();
	}
}