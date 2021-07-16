package db.storage;

import java.util.ArrayList;

public class DBColumn {

	private String name;
	private ArrayList<String> column;

	public DBColumn(String name) {
		this.name = name;
		column = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public String getCell(int index) {
		return column.get(index);
	}

	public void setCell(int index, String data) { column.set(index, data); }

	public void removeCell(int index) { column.remove(index); }

	//add a cell to the column (adds a new row to the column)
	public void addCell(String data) {
		column.add(data);
	}

	//less confusing name than getColumnSize, the number of rows in this column
	public int getNumOfRows()
	{
		return column.size();
	}
}
