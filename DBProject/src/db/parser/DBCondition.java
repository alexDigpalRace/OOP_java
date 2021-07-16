package db.parser;

public class DBCondition {
	private String colName;
	private String operator;
	private String value;

	public DBCondition(String colName, String operator, String value) {
		this.colName = colName;
		this.operator = operator;
		this.value = value;
	}

	public String getColumnName() {
		return colName;
	}

	public String getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}
}
