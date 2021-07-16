import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameActionsParser {
	private ArrayList<GameAction> actions;
	private GameAction gameAction;

	//on creation of instance automatically parses fileName
	public GameActionsParser(String fileName) throws IOException, ParseException {
		actions = new ArrayList<>();
		this.parseJSON(fileName);
	}

	//https://stackoverflow.com/questions/10926353/how-to-read-json-file-into-java-with-simple-json-library helped
	private void parseJSON(String fileName) throws IOException, ParseException {
		FileReader reader = new FileReader(fileName);
		JSONParser parser = new JSONParser();
		Object object = parser.parse(reader);
		//cast above to JSONObject
		JSONObject jObject = (JSONObject) object;
		//get the actions array of the object
		JSONArray jArray = (JSONArray) jObject.get("actions");
		//for each object in the actions array
		for (Object o : jArray) {
			//cast object to jsonobject, each object represents a distinct action
			JSONObject action = (JSONObject) o;
			//create a game action
			gameAction = new GameAction();
			//get relevant data from action object
			this.parseTriggers(action);
			this.parseSubjects(action);
			this.parseConsumed(action);
			this.parseProduced(action);
			//doesnt need its own method...
			gameAction.setNarration(action.get("narration").toString());
			//add built gameAction to actions list then onto next one
			actions.add(gameAction);
		}
	}

	//populate gameAction with triggers
	private void parseTriggers(JSONObject action) {
		//find the triggers array
		JSONArray triggers = (JSONArray) action.get("triggers");
		//loop through each
		for (Object trigger : triggers) {
			//add to gameaction trigger list
			gameAction.addTrigger(trigger.toString());
		}
	}

	//populate gameAction with subjects
	private void parseSubjects(JSONObject action) {
		//find subjects array
		JSONArray subjects = (JSONArray) action.get("subjects");
		//loop through each
		for (Object subject : subjects) {
			//add to gameaction trigger list
			gameAction.addSubject(subject.toString());
		}
	}

	//populate gameAction with consumed
	private void parseConsumed(JSONObject action) {
		//find consumed array
		JSONArray consumed = (JSONArray) action.get("consumed");
		//loop through each
		for (Object consume : consumed) {
			//add to gameaction trigger list
			gameAction.addConsumed(consume.toString());
		}
	}

	//populate gameAction with produced
	private void parseProduced(JSONObject action) {
		//find produced array
		JSONArray produced = (JSONArray) action.get("produced");
		//loop through each
		for (Object produce : produced) {
			//add to gameaction trigger list
			gameAction.addProduced(produce.toString());
		}
	}

	//gets the list of game actions built with parser
	public ArrayList<GameAction> getActions() {
		return actions;
	}
}
