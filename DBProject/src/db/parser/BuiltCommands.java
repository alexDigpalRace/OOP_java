package db.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class BuiltCommands {
	private String commandType;
	private String commandTypeSpecifier;
	private String dbName;
	private List<String> tableNames;
	private List<String> colNames;
	private List<DBCondition> conditions;
	private List<String> condChainModifier; //AND / OR
	private List<String> data;
	private String dataType;

	public BuiltCommands() {
		tableNames = new ArrayList<String>();
		colNames = new ArrayList<String>();
		conditions = new ArrayList<DBCondition>();
		data = new ArrayList<String>();
		condChainModifier = new ArrayList<String>();
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName(int index) {
		return tableNames.get(index);
	}

	public void addTableName(String tableName) {
		tableNames.add(tableName);
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<String> getColNames() { return colNames; }

	public String getColNames(int index) {
		return colNames.get(index);
	}

	public void addColNames(String colNames) {
		this.colNames.add(colNames);
	}

	public int getColNamesSize() { return colNames.size(); }

	public String getData(int index) {
		return data.get(index);
	}

	public int getDataListSize() { return data.size(); }

	public void addData(String data) {
		this.data.add(data);
	}

	public DBCondition getConditions(int index) {
		return conditions.get(index);
	}

	public void addConditions(DBCondition condition) {
		this.conditions.add(condition);
	}

	public int conditionSize() { return conditions.size(); }

	public String getCondChainMod(int index) {
		return condChainModifier.get(index);
	}

	public void addCondChainMod(String condChainMod) {
		condChainModifier.add(condChainMod);
	}

	public String getCommandTypeSpecifier() {
		return commandTypeSpecifier;
	}

	public void setCommandTypeSpecifier(String word) {
		commandTypeSpecifier = word;
	}
}
