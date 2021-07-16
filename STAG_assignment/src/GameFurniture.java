public class GameFurniture extends GameEntity {
	public GameFurniture(String name, String description) {
		super(name, description);
	}

	//for easy identification of datatype
	@Override
	public String identifyDatatype() {
		return "furniture";
	}
}
