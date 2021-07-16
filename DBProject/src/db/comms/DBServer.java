//basic: core test all passed? quotation marks around names may be what failed a lot of the tests 16/16
//advanced: 15/21 6 failed 2 failed do print out error but also print ok beforehand
//robustness: 5/11 all fails do print out error but print out ok beforehand
//extra robustness tests failed: 3/10 7 failed some are like above where its print out ok and error

package db.comms;

import db.exceptions.DBException;
import db.parser.Tokenizer;
import db.storage.DBManager;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class DBServer
{
	Tokenizer tokenizer;
	DBManager databases;
	BufferedWriter socketWriter;
	ArrayList<ArrayList<String>> userTable;
	DBRWDoc readWrite;
    public DBServer(int portNumber) throws IOException {
    	databases = new DBManager();
    	readWrite = new DBRWDoc(databases);
    	try {
		    //read saved databases
		    readWrite.readDatabases();
		    ServerSocket serverSocket = new ServerSocket(portNumber);
		    System.out.println("Server Listening");
		    while(true) processNextConnection(serverSocket);
	    } catch (DBException e) {
		    System.out.println(e.toString());
	    } catch (IOException ioe) {
		    System.err.println(ioe);
	    }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException {
	    String incomingCommand = socketReader.readLine();
	    System.out.println("Received message: " + incomingCommand);
	    try {
		    //Tokenize the incoming command/query
		    tokenizer = new Tokenizer(incomingCommand);
		    tokenizer.parse();
		    socketWriter.write("[OK]\n");
		    userTable = tokenizer.execute(databases);
		    if (userTable != null) {
			    this.printTable(userTable);
		    }
		    //write database out to project after action
		    readWrite.writeDatabases();
	    } catch (DBException e) {
		    socketWriter.write("[ERROR] Bad Query: " + e.toString() + "\n");
	    }

	    socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[]) throws IOException {
	    DBServer server = new DBServer(8888);
    }

    private void printTable(ArrayList<ArrayList<String>> toPrint) throws IOException {
		//loop through rows
    	for (int i = 0; i < toPrint.get(0).size(); i++) {
			//loop through columns
		    for (int j = 0; j < toPrint.size(); j++) {
		    	//write each cell spaced by ' '
				socketWriter.write(toPrint.get(j).get(i) + " ");
		    }
		    //end of row so write a '\n'
		    socketWriter.write("\n");
		}
	    System.out.println("usr table size: " + toPrint.size() + toPrint.get(0).size());
    }
}
