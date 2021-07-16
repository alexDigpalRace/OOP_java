import java.util.ArrayList;

public class GameAction {
	private ArrayList<String> triggers;
	private ArrayList<String> subjects;
	private ArrayList<String> consumed;
	private ArrayList<String> produced;
	private String narration;

	//initialise fields
	public GameAction() {
		triggers = new ArrayList<>();
		subjects = new ArrayList<>();
		consumed = new ArrayList<>();
		produced = new ArrayList<>();
	}

	//adds a trigger word
	public void addTrigger(String trigger) {
		triggers.add(trigger);
	}

	//adds a subject word
	public void addSubject(String subject) {
		subjects.add(subject);
	}

	//return the list of subject words
	public ArrayList<String> getSubjects() { return subjects; }

	//adds a consumed word
	public void addConsumed(String consume) {
		consumed.add(consume);
	}

	//returns the list of consumed words
	public ArrayList<String> getConsumed() { return consumed; }

	//adds a product word
	public void addProduced(String produce) {
		produced.add(produce);
	}

	//return the product list
	public ArrayList<String> getProduced() { return produced; }

	//return the narration to be printed to the player
	public String getNarration() {
		return narration;
	}

	//sets the narration
	public void setNarration(String narration) {
		this.narration = narration;
	}

	//finds a specific trigger in the trigger list
	public boolean findTrigger(String trigger) {
		for (String s : triggers) {
			if (s.equals(trigger)) {
				return true;
			}
		}
		return false;
	}
}
