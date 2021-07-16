import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
	private ArrayList<GameLocation> locations;
	private ArrayList<GamePlayer> players;
	private GameCommands gameCommands;


	//constructor sets up empty lists
	public Game() {
		locations = new ArrayList<>();
		players = new ArrayList<>();
	}

	//sets game actions and triggers a new GameCommands to be build (which needed a GameActions object)
	public void setGameActions(ArrayList<GameAction> actions) {
		//can also setup a new instance for gameCommands as gameActions needed to be created first
		gameCommands = new GameCommands(locations, actions);
	}

	//adds a location to the list
	public void addLocation(GameLocation gameLocation){
		locations.add(gameLocation);
	}

	//retrieves a location on the list
	public GameLocation getGameLocation(String name) throws GameException {
		for (GameLocation location : locations) {
			if (location.getName().equals(name)) {
				return location;
			}
		}
		throw new GameException("Nothing happened...");
	}

	//user command WITH the player name
	public String userCommand(String command) throws GameException {
		//get player name and create/check player exists
		List<String> userTokens = Arrays.asList(command.split(":"));
		//player name is usertokens[0]
		this.createPlayer(userTokens.get(0));
		//pass player (usertokens[0]) and the rest of the command (usertokens[1]) to
		//be interpreted and return the message back to Server to send to client)
		return gameCommands.interpretCommand(this.getPlayer(userTokens.get(0)), userTokens.get(1).trim());
	}

	//looks if a player exists and creates if needed
	private void createPlayer(String playerName) {
		boolean found = false;
		for (GamePlayer player : players) {
			if (player.getName().equals(playerName)) {
				found = true;
			}
		}
		if(!found){
			//not found so create one for tracking, with name and starting location
			players.add(new GamePlayer(playerName, "A user controlled character", locations.get(0)));
		}
	}

	//return player object with playerName
	private GamePlayer getPlayer(String playerName) throws GameException {
		for (GamePlayer gp : players) {
			if (gp.getName().equals(playerName)) {
				return gp;
			}
		}
		throw new GameException("Player does not exist: " + playerName);
	}
}
