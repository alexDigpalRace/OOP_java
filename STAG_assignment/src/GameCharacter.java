public class GameCharacter extends GameEntity {
	public GameCharacter(String name, String description) {
		super(name, description);
	}

	//for easy identification of datatype
	@Override
	public String identifyDatatype() {
		return "character";
	}
}
