package db.parser;

import java.util.ArrayList;
import java.util.List;

public class Tokens {
	private List<String> commands;
	private int currentCommand;
	private BuiltCommands builtCommands;

	public Tokens() {
		commands = new ArrayList<>();
		currentCommand = 0;
		builtCommands = new BuiltCommands();
	}

	public void addCommand(String cmd) {
		commands.add(cmd);
	}

	public int getSize() {
		return commands.size();
	}

	public String nextCommand() {
		System.out.println(commands.get(currentCommand));
		return commands.get(currentCommand++);
	}

	public String getCommandAt(int index) {
		return commands.get(index);
	}

	public void setCommandAt(int index, String value) {
		commands.set(index, value);
	}

	public String getCurrentCommand() {
		System.out.println(commands.get(currentCommand));
		return commands.get(currentCommand);
	}

	public int getCurrentIndex() {
		return currentCommand;
	}

	//can also be used to go backwards with e.g. -1
	public void incrementIndex(int i) {
		currentCommand += i;
	}

	public void setDbName(String name) {
		builtCommands.setDbName(name);
	}

	public void setDbCommandType(String commandType) {
		builtCommands.setCommandType(commandType);
	}

	public void addTableName(String tableName) {
		builtCommands.addTableName(tableName);
	}

	public void addColNames(String colNames) {
		builtCommands.addColNames(colNames);
	}

	public void addData(String data) {
		builtCommands.addData(data);
	}

	public void addConditions(DBCondition condition) {
		builtCommands.addConditions(condition);
	}

	public void addCondChainMod(String word) {
		builtCommands.addCondChainMod(word);
	}

	public BuiltCommands getBuiltCommands() {
		return builtCommands;
	}

	public void setCommandTypeSpecifier(String word) {
		builtCommands.setCommandTypeSpecifier(word);
	}
}
