public class GameEntity {
	private String name;
	private String description;

	//initialise fields
	public GameEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}

	//get the entity name...
	public String getName() {
		return name;
	}

	//get the entity description...
	public String getDescription() {
		return description;
	}

	//for easy identification of datatype
	public String identifyDatatype() {
		return "entity";
	}
}
