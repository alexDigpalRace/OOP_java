import java.util.ArrayList;

public class GamePlayer extends GameCharacter {

	private ArrayList<GameArtefact> inventory;
	private GameLocation currentLocation;
	private GameLocation startingLocation;
	private int health = 3;

	//initialise fields
	public GamePlayer(String name, String description, GameLocation startingLocation) {
		super(name, description);
		inventory = new ArrayList<>();
		this.setPlayerLocation(startingLocation);
		this.startingLocation = startingLocation;
	}

	//returns a message with the inventory in it
	public String getInventoryList() {
		String message = new String("You currently have: ");
		for (GameArtefact ga : inventory) {
			message += (ga.getName() + ",");
		}
		return message;
	}

	//return the players current location
	public GameLocation getCurrentLocation() {
		return currentLocation;
	}

	//set the players current location
	public String setPlayerLocation(GameLocation location) {
		if (currentLocation != null) {
			//remove player from character list in current location
			this.currentLocation.removeCharacter(this.getName());
		}
		//add player to new location character list (duplicates the player as a character, cheaty but oh well)
		location.addCharacter(this);
		//change current location
		currentLocation = location;
		return ("Going to: " + currentLocation.getName());
	}

	//removes artefact from inventory and places it back in location
	public String dropArtefact(ArrayList<String> commands) {
		//for each command in commands
		for (String command : commands) {
			//loop through inventory to see if command is in there
			for (GameArtefact artefact : inventory) {
				if (artefact.getName().equals(command)) {
					currentLocation.addArtefact(artefact);
					inventory.remove(artefact);
					return ("You dropped: " + command);
				}
			}
		}
		//if the artefact isn't in inventory
		return "Nothing happened...";
	}

	//removes artefact from location and puts it in player inventory
	public String getArtefact(ArrayList<String> commands) {
		//go through each word of player command
		for (String command : commands) {
			try {
				inventory.add(currentLocation.getArtefact(command));
				return ("You now have the: " + command);
			} catch (GameException e) {
			}
		}
		//artefact not in location so report nothing happened
		return "Nothing happened...";
	}

	//true if artefact with name is in player inventory
	public boolean inInventory(String name) {
		for (GameArtefact artefact : inventory) {
			if (artefact.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//removes artefact from player inventory
	public void removeArtefact(String name) {
		for (GameArtefact a : inventory) {
			if (a.getName().equals(name)) {
				inventory.remove(a);
				return;
			}
		}
	}

	//decrements players health by one, if it drops it zero inventory is dropped in current location and player spawned
	//back at starting location
	public void takeDamage() throws GameException {
		if (health > 1) {
			health--;
		} else {
			//reset health
			health = 3;
			//drop inventory
			this.dropAllArtefacts();
			//respawn back at starting location
			currentLocation = startingLocation;
		}
	}

	//drops all items in inventory in current location
	private void dropAllArtefacts() throws GameException {
		//create a ArrayList to send through dropArtefacts, will only ever have one item
		ArrayList<String> itemName = new ArrayList<>();
		//add one element
		itemName.add("");
		for (int i = inventory.size() - 1; i >= 0; i--) {
			itemName.set(0, inventory.get(i).getName());    //set the element to the inventory items each time
			this.dropArtefact(itemName);                    //drop that item
		}
	}

	//restore health
	public void healDamage() {
		if (health < 3) {
			health++;
		}
	}
}
