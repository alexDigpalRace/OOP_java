import java.util.ArrayList;
import java.util.Arrays;

public class GameCommands {
	private ArrayList<GameLocation> locations;
	private ArrayList<GameAction> actions;
	private GamePlayer player;
	private ArrayList<String> tokens;

	//initialise fields
	public GameCommands(ArrayList<GameLocation> locations, ArrayList<GameAction> actions) {
		this.locations = locations;
		this.actions = actions;
	}

	//initialise player and command field
	public String interpretCommand(GamePlayer player, String command) throws GameException {
		this.player = player;
		//split command by whitespace
		tokens = new ArrayList<>(Arrays.asList(command.toLowerCase().split("\\s")));

		//for each token try to find a key command
		for (String token : tokens) {
			switch (token) {
				//lists artefacts in inventory
				case "inventory":
				case "inv":
					return player.getInventoryList();
				//picks up artefacts and puts them in inventory
				case "get":
					return player.getArtefact(tokens);
				//drops artefact from inventory to current location
				case "drop":
					return player.dropArtefact(tokens);
				//moves from one location to another (paths)
				case "goto":
					return player.setPlayerLocation(this.checkLocationPath());
				//describe entities in the current location and lists the paths to other locations
				case "look":
					return player.getCurrentLocation().playerSees(player.getName());
				//no default!!!!!
			}
		}
		//if no key command found, check for actions
		return findTrigger();
	}

	//check if a player's current location has a path to a new location
	private GameLocation checkLocationPath() throws GameException {
		//loop through the player command tokens to find a location name
		for (String token : tokens) {
			//check the token is on current location path list
			if(player.getCurrentLocation().isPath(token)) {
				//find the location
				try {
					return this.getLocation(token);
				} catch (GameException e) {
				}
			}
		}
		//no location found
		throw new GameException("Nothing happened...");
	}

	//get a location from locations with specified name
	private GameLocation getLocation(String name) throws GameException {
		for (GameLocation location : locations) {
			if (location.getName().equals(name)) {
				return location;
			}
		}
		throw new GameException("Nothing happened...");
	}

	//no KEY commands found so below method starts of look for actions, first step to look for trigger words
	private String findTrigger() throws GameException {
		//for every token in the command
		for (String token : tokens) {
			//check with every game action if its in the triggers
			for (GameAction action : actions) {
				if (action.findTrigger(token)) {
					//found the action, now check if subjects present
					return this.checkSubjectsPresent(action);
				}
			}
		}
		//if no trigger word can be found
		return ("Nothing happened...");
	}

	//checks if subjects in the action definition are with/by the player
	private String checkSubjectsPresent(GameAction action) throws GameException {
		ArrayList<String> subjects = action.getSubjects();
		int count = 0;  //for recording accounted for subjects
		for (String subject : subjects) {
			if (player.getCurrentLocation().findFurniture(subject) || player.inInventory(subject)
					|| player.getCurrentLocation().findCharacter(subject)) {
				count++;
			}
		}
		//all subjects found so do the consumption
		if (subjects.size() == count) {
			//subjects account for so go onto consumption/production
			return this.doConsumption(action);
		}
		//subjects not present
		return "Nothing happened...";
	}

	//consumes the appropriate thing
	private String doConsumption(GameAction action) throws GameException {
		ArrayList<String> consumed = action.getConsumed();
		for (String consumable : consumed) {
			//find what it is health/artef/char/furn
			if (consumable.equals("health")) {
				player.takeDamage();
			} else if (player.getCurrentLocation().findFurniture(consumable)) {
				player.getCurrentLocation().removeFurniture(consumable);
			} else if (player.inInventory(consumable)) {
				player.removeArtefact(consumable);
			} else if (player.getCurrentLocation().findCharacter(consumable)) {
				player.getCurrentLocation().removeCharacter(consumable);
			}
		}
		//consumption done so ready to produce
		return this.doProduction(action);
	}

	//produces the appropriate thing
	private String doProduction(GameAction action) throws GameException {
		ArrayList<String> produced = action.getProduced();
		for (String product : produced) {
			//check if location name, add a path to location if so
			if (checkLocationName(product)) {
				//add a path to players current location
				player.getCurrentLocation().addPath(product);
				//check if health
			} else if (product.equals("health")) {
				player.healDamage();
				//else must be an unplaced character/artefact/furniture
			} else {
				GameLocation unplaced = this.getUnplaced();
				player.getCurrentLocation().addUnplacedEntity(unplaced.findEntity(product));
			}
		}
		return action.getNarration();
	}

	//check if product is a location name
	private boolean checkLocationName(String name) {
		for (GameLocation location : locations) {
			if (location.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//finds the unplaced location in locations and returns it
	private GameLocation getUnplaced() throws GameException {
		for (GameLocation l : locations) {
			if (l.getName().equals("unplaced")) {
				return l;
			}
		}
		throw new GameException("Unplaced not found? fatal error");
	}
}
