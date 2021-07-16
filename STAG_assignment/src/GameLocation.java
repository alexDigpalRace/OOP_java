import java.util.ArrayList;
import java.util.List;

//LOCATION == LEVEL
public class GameLocation extends GameEntity {
	private ArrayList<GameCharacter> characters;
	private ArrayList<GameArtefact> artefacts;
	private ArrayList<GameFurniture> furniture;
	private ArrayList<String> paths;

	//initialise fields
	public GameLocation(String locationName, String locationDescription) {
		super(locationName, locationDescription);
		characters = new ArrayList<>();
		artefacts = new ArrayList<>();
		furniture = new ArrayList<>();
		paths = new ArrayList<>();
	}

	//adds a character to THIS location
	public void addCharacter(String name, String description) {
		characters.add(new GameCharacter(name, description));
	}

	//adds a existing character (player)
	public void addCharacter(GameCharacter character) {
		characters.add(character);
	}

	//creates a artefact with name and description and adds it the location
	public void addArtefact(String name, String description) {
		artefacts.add(new GameArtefact(name, description));
	}

	//for adding artefact back to location (when player drops it) as its already made needed overloaded method
	public void addArtefact(GameArtefact artefact) {
		artefacts.add(artefact);
	}

	//adds a NEW furniture to location
	public void addFurniture(String name, String description) {
		furniture.add(new GameFurniture(name, description));
	}

	//adds a path name to the location
	public void addPath(String path) {
		paths.add(path);
	}

	//get specified artefact from location and remove it
	public GameArtefact getArtefact(String name) throws GameException {
		for (GameArtefact artefact : artefacts) {
			if (artefact.getName().equals(name)) {
				artefacts.remove(artefact);
				return artefact;
			}
		}
		//if item doesn't exist
		throw new GameException("Nothing happened...");
	}

	//see if a furniture exists in list
	public boolean findFurniture(String name) {
		for (GameFurniture gf : furniture) {
			if (gf.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//see if a character exists in list
	public boolean findCharacter(String name) {
		for (GameCharacter character : characters) {
			if (character.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//find entity with name (for finding unplaced items)
	public GameEntity findEntity(String name) throws GameException {
		//loop through artefacts
		for (GameArtefact a : artefacts) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		//loop through furniture
		for (GameFurniture f : furniture) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		//loop through characters
		for (GameCharacter c : characters) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		throw new GameException("Trying to find non existent item!!: " + name);
	}

	//removes furniture with NAME from location
	public void removeFurniture(String name) {
		for (GameFurniture f : furniture) {
			if (f.getName().equals(name)) {
				furniture.remove(f);
				return;
			}
		}
	}

	//removes character with NAME from location
	public void removeCharacter(String name) {
		for (GameCharacter c : characters) {
			if (c.getName().equals(name)) {
				characters.remove(c);
				return;
			}
		}
	}

	//identifies and adds an unplaced entity into the location
	public void addUnplacedEntity(GameEntity ge) throws GameException {
		switch (ge.identifyDatatype()) {
			case "character":
				characters.add((GameCharacter) ge);
				break;
			case "artefact":
				artefacts.add((GameArtefact) ge);
				break;
			case "furniture":
				furniture.add((GameFurniture) ge);
				break;
			default:
				throw new GameException("Datatype failure");
		}
	}

	//check if a path is in paths for player movement to different areas
	public boolean isPath(String name) {
		for (String path : paths) {
			if (path.equals(name)) {
				return true;
			}
		}
		return false;
	}

	//creates a string with all entities in that location
	public String playerSees(String playerName) {
		//location name and description
		String message = new String("You are in the: " + this.getName() + " - " + this.getDescription() + "\n");
		message += listFurniture("You can see: ");
		message += listArtefacts("Items to pickup include: ");
		message += listCharacters("You are being watched by: ", playerName);
		message += listPaths("There are paths to: ");
		return message;
	}

	//generate a string list for furniture
	private String listFurniture(String furnitureMessage) {
		for (GameFurniture furnitii : furniture) {
			furnitureMessage += (furnitii.getName() + ", ");
		}
		return (furnitureMessage + "\n");
	}

	//generate a string list for artefacts
	private String listArtefacts(String artefactMessage) {
		for (GameArtefact artefact : artefacts) {
			artefactMessage += (artefact.getName() + ", ");
		}
		return (artefactMessage + "\n");
	}

	//generate a string list for artefacts
	private String listCharacters(String characterMessage, String playerName) {
		for (GameCharacter character : characters) {
			if (playerName != character.getName()) {
				characterMessage += (character.getName() + ", ");
			}
		}
		return (characterMessage + "\n");
	}

	//generate a comma separated list of all paths in the location
	public String listPaths(String pathMessage) {
			for (String path : paths) {
				pathMessage += (path + ", ");
			}
			return (pathMessage + "\n");
	}
}
