package db.comms;/* Responsible for scanning a doc
 * Breaking it down into strings
 * Saving them into an appropriate table
 * Returning the table*/

import db.exceptions.DBException;
import db.storage.DBManager;
import db.storage.DBTable;
import db.storage.DBTableManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/* Reads a doc into a database*/
public class DBRWDoc {
	private File fileName;
	private BufferedReader bufferedReader;
	private Writer writer;
	private DBManager databases;
	private DBTableManager database;
	private DBTable table;

	public DBRWDoc(DBManager databases) throws IOException {
		this.databases = databases;
	}

	//shamelessly stolen from: https://stackoverflow.com/questions/4852531/find-files-in-a-folder-using-java
	//reads existing directories to access saved databases and loads them into the session
	public void readDatabases() throws DBException, IOException {
		fileName = new File("src");
		//get list of DB directories in src directory
		File[] matches = fileName.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("DB");
			}
		});
		//check if there are any DB directory files to begin with
		if (matches != null) {
			//thanking you: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java/4917359
			//loop through the files that matched
			for (int i = 0; i < matches.length; i++) {
				//get rid of DB naming convention
				String dbName = matches[i].getName().substring(2);
				File toOpen = new File("src" + File.separator + "DB" + dbName);
				//add a database with specified name
				databases.addDatabase(dbName);
				//get list of files in DB directory
				File[] tableFiles = toOpen.listFiles();
				if (tableFiles != null) {
					for (File child : tableFiles) {
						String tableName = child.getName().substring(0, child.getName().length() - 4);
						//add table with the file name
						databases.getDatabase(dbName).addTable(tableName);
						System.out.println("Creating table with name: " + tableName);
						//save table for future reference
						table = databases.getDatabase(dbName).getTable(tableName);
						this.readDbFileIntoTable(child);
					}
				}
			}
		}
	}

	/*reads a file into a DBtable*/
	private void readDbFileIntoTable(File tableFile) throws IOException {
		ArrayList<String> columnNames;
		String word = null;
		//open specified file for reading
		try {
			bufferedReader = new BufferedReader(new FileReader(tableFile));
		} catch (FileNotFoundException e) {
			//file doesn't exist so do nothing
			System.out.println("!!!!!File does not exist?");
		}
		System.out.println("reading file");
		try {
			this.getNamesFromFile();
			try {
				this.getValuesFromFile();
			} catch (IOException e) {
				System.out.println("error reading vals");
			}
		} catch (IOException e) {
			System.out.println("error reading names");
		}
		bufferedReader.close();
	}

	//regex: https://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double
	private void getNamesFromFile() throws IOException {
		String s;
		if ((s = bufferedReader.readLine()) != null) {
			String[] split = s.split("[\\t\\n]");
			for (String string : split) {
				table.addColumn(string);
			}
		}
	}

	/*read chars to string, stop at whitespace*/
	private void getValuesFromFile() throws IOException {
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			//split by all whitespace
			String[] split = s.split("[\\t\\n]");
			//loop through columns (row index)
			for (int i = 0; i < split.length; i++) {
				//go through each column, add values % by number of cols to keep alignment of values
				table.getColumnAt(i % table.getNumOfCols()).addCell(split[i]);
			}
		}
	}

	//delete old saves of ALL databases and writes them all back out
	public void writeDatabases() throws DBException, IOException {
		fileName = new File("src");
		//get a list of old databases on hard drive
		File[] oldDatabases = fileName.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("DB");
			}
		});
		//loop through them to get old files then delete files and database
		for (File oldDatabase : oldDatabases) {
			//get files in database
			fileName = new File("src" + File.separator + oldDatabase.getName());
			//list all files
			File[] oldDataFiles = fileName.listFiles();
			//go through and delete them
			for (File oldFiles : oldDataFiles) {
				oldFiles.delete();
			}
			//delete database
			oldDatabase.delete();
		}
		//loop through session (new) databases
		for (int i = 0; i < databases.getSize(); i++) {
			database = databases.getDatabase(i);
			//get the names and append naming convention
			String dirName = "DB" + database.getName();
			//create a directory for database
			File newDatabase = new File("src" + File.separator + dirName);
			newDatabase.mkdir();
			//loop through tables of database
			for (int j = 0; j < database.getSize(); j++) {
				table = database.getTable(j);
				//write tables into .txt/.tab file
				this.writeTableToFile(table, dirName);
			}
		}
	}

	//in this class as it already has the specific instance of db.storage.DBManager we want to write
	//DBdirectories are directories with the databases in....
	public void writeTableToFile(DBTable sourceTable, String dirName) throws IOException {
		String pathName = "src" + File.separator + dirName + File.separator + sourceTable.getName() + ".tab";
		try {
			writer = new FileWriter(pathName);
			this.writeNamesToFile(sourceTable);
			try {
				this.writeDataToFile(sourceTable);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("table empty, nothing to print");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	//TODO handle empty table
	private void writeNamesToFile(DBTable sourceTable) {
		//check if the table has any column names to write
		if (sourceTable.getNumOfCols() > 0) {
			try {
				//go through each column
				for (int i = 0; i < sourceTable.getNumOfCols(); i++) {
					//if not the last col print name with tab spacing
					if (i != sourceTable.getNumOfCols() - 1) {
						writer.write(sourceTable.getColumnAt(i).getName() + "\t");
						//if last col print name with new line spacing
					} else {
						writer.write(sourceTable.getColumnAt(i).getName() + "\n");
					}
				}
			} catch (IOException e) {
				System.out.println("Error in writing name to file");
			}
		}
	}

	//TODO add first values to table
	private void writeDataToFile(DBTable sourceTable) throws IndexOutOfBoundsException {
		try {
			//check if the table has any columns
			if (sourceTable.getColumnAt(0) != null) {
				//check if table has values (rows) to write
				if (sourceTable.getColumnAt(0).getNumOfRows() > 0) {
					//loop through rows
					for (int i = 0; i < sourceTable.getColumnAt(0).getNumOfRows(); i++) {
						//loop through columns of the same row
						for (int j = 0; j < sourceTable.getNumOfCols(); j++) {
							//if not the last column print data space with tab
							if (j != sourceTable.getNumOfCols() - 1) {
								writer.write(sourceTable.getColumnAt(j).getCell(i) + "\t");
								//last column so space with newline
							} else {
								writer.write(sourceTable.getColumnAt(j).getCell(i) + "\n");
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error in writing data to file");
		}
	}
}