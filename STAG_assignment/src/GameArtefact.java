public class GameArtefact extends GameEntity {

	public GameArtefact(String name, String description) {
		super(name, description);
	}

	//for easy identification of datatype
	@Override
	public String identifyDatatype() {
		return "artefact";
	}
}
