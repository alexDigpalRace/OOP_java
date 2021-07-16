//javac -cp ".;./lib/dot-parser.jar;./lib/json-parser.jar" StagServer.java
//java -cp ".;./lib/dot-parser.jar;./lib/json-parser.jar" StagServer basic-entities.dot basic-actions.json

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameEntitiesParser {

	private Game newGame = new Game();
	private GameLocation gl;

	public GameEntitiesParser(String fileName) throws FileNotFoundException, ParseException, GameException {
		Parser graphParser = new Parser();                              //make a new graphviz parser object
		FileReader reader = new FileReader(fileName);                   //create a fileReader pointed at the file

		graphParser.parse(reader);                                      //parse file

		ArrayList<Graph> graphs = graphParser.getGraphs();              //overruling graph which is useless
		ArrayList<Graph> subgraphs = graphs.get(0).getSubgraphs();      //subgraphs: locations and paths
		ArrayList<Graph> subsubgraphs = subgraphs.get(0).getSubgraphs();

		//for each cluster which is a complete description of the location
		for(Graph g : subsubgraphs){
			ArrayList<Node> nodes = g.getNodes(true);
			//location: name, description, list of: characters, artefacts, furniture, paths
			//create location with name and description
			gl = new GameLocation(nodes.get(0).getId().getId(), nodes.get(0).getAttribute("description"));
			//get the subsubsubgraph which is the entity layer of the graph
			ArrayList<Graph> entityCategories = g.getSubgraphs();
			//loop through each entity category
			for (Graph entitys : entityCategories) {
				//loop through each entitys graph and create an appropriate entity
				this.createEntity(entitys);
			}
			//add location to game
			newGame.addLocation(gl);
		}
		//get list of paths (edges) in the game
		ArrayList<Edge> edges = subgraphs.get(1).getEdges();
		//loop through the paths
		for (Edge e : edges){
			//get the game location with the source name and add to its paths list the target
			newGame.getGameLocation(e.getSource().getNode().getId().getId()).addPath(e.getTarget().getNode().getId().getId());
		}
	}

	//branch to appropriate entity creation method
	private void createEntity(Graph entityCategory) throws GameException {
		switch (entityCategory.getId().getId()) {
			case "artefacts":
				this.createArtefacts(entityCategory);
				break;
			case "furniture":
				this.createFurniture(entityCategory);
				break;
			case "characters":
				this.createCharacters(entityCategory);
				break;
			default:
				throw new GameException("invalid entity category: " + entityCategory.getId().getId()
				+ "useage: artfacts/furniture/characters");
		}
	}

	//TODO Similar repeat code for entity types
	//create an artefact and add it to the game location
	private void createArtefacts(Graph entity) {
		ArrayList<Node> nl = entity.getNodes(true);
		for (Node n : nl) {
			gl.addArtefact(n.getId().getId(), n.getAttribute("description"));
		}
	}
	//create furniture and add it to the game location
	private void createFurniture(Graph entity) {
		ArrayList<Node> nl = entity.getNodes(true);
		for (Node n : nl) {
			gl.addFurniture(n.getId().getId(), n.getAttribute("description"));
		}
	}
	//create a character and add it to the game location
	private void createCharacters(Graph entity) {
		ArrayList<Node> nl = entity.getNodes(true);
		for (Node n : nl) {
			gl.addCharacter(n.getId().getId(), n.getAttribute("description"));
		}
	}

	//get the game created by the parser
	public Game getGame() {
		return newGame;
	}
}