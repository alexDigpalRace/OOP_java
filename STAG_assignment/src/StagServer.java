import com.alexmerz.graphviz.ParseException;

import java.io.*;
import java.net.*;

class StagServer
{
	GameEntitiesParser gep;
	GameActionsParser gap;
	Game game;

	public static void main(String args[])
	{
		if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
		else new StagServer(args[0], args[1], 8888);
	}

	public StagServer(String entityFilename, String actionFilename, int portNumber)
	{
		try {
			ServerSocket ss = new ServerSocket(portNumber);
			//parses and builds a game based on dot file
			gep = new GameEntitiesParser(entityFilename);
			//set game to the game graph parser just created
			game = gep.getGame();
			//parse game actions
			gap = new GameActionsParser(actionFilename);
			//add game actions from the parser
			game.setGameActions(gap.getActions());
			System.out.println("Server Listening");
			while(true) acceptNextConnection(ss);
		} catch(IOException | ParseException | GameException | org.json.simple.parser.ParseException e) {
			System.err.println(e);
		}
	}

	private void acceptNextConnection(ServerSocket ss)
	{
		try {
			// Next line will block until a connection is received
			Socket socket = ss.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			processNextCommand(in, out);
			out.close();
			in.close();
			socket.close();
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
	}

	private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
	{
		//read player command, save it into line
		String line = in.readLine();
		try {
			out.write(game.userCommand(line));
		} catch (GameException e) {
			out.write(e.toString());
		}
	}
}