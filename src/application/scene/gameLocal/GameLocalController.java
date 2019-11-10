package application.scene.gameLocal;

import org.json.JSONObject;

import application.client.Client;
import application.scene.gameLocal.gameNode.CardNode;
import application.unoBackend.Card;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GameLocalController {
	private Client client;
	private Stage stage;
    @FXML
    private Pane gamePane;
	public GameLocalController(GameLocal gameLocal, Stage stage) {
		this.stage = stage;
		client = new Client();
		gamePane = new Pane();
	}
	private void testBootstrap() {
	    Circle circle = new Circle();
	    circle.setRadius(400);
	    circle.setStroke(Color.BLACK);
	    circle.setFill(Color.YELLOW);
	    circle.setLayoutX(200);
	    gamePane.getChildren().add(circle);
	    Card tmpCard = new Card(Card.cardColor.RED, Card.cardValue.NINE);
	    gamePane.getChildren().add(CardNode.getCardFront(tmpCard));        
	}
	/**
	 * Set up the game pane
	 */
	private void realBootstrap() {
		String inputMsg = "{"
				+ "'id': 0, "
				+ "'players': ["
				+ "{'id': 0, 'name': 'TestPlayer0'}, "
				+ "{'id': 1, 'name': 'TestPlayer1'},"
				+ "{'id': 2, 'name': 'TestPlayer2'},"
				+ "{'id': 3, 'name': 'TestPlayer3'},"
				+ "{'id': 4, 'name': 'TestPlayer4'},"
				+ "{'id': 5, 'name': 'TestPlayer5'},"
				+ "{'id': 6, 'name': 'TestPlayer6'},"
				+ "{'id': 7, 'name': 'TestPlayer7'},"
				+ "],"
				+ " 'playerId': 0"
				+ "}";
		client.processingMsg(new JSONObject(inputMsg));
	}
	private void testStartRound() {
		String inputMsg = "{'id': 1," 
			+ "'nCards': 9, " 
			+ "'startPlayer': 0," 
			+ "'hand': ["
			+ "{'id': 0, 'color': 'RED', 'value': 'ZERO'},"
			+ "{'id': 1, 'color': 'RED', 'value': 'SKIP'}, "
			+ "{'id': 2, 'color': 'GREEN', 'value': 'ONE'},"
			+ "{'id': 3, 'color': 'YELLOW', 'value': 'TWO'},"
			+ "{'id': 4, 'color': 'BLUE', 'value': 'THREE'},"
			+ "{'id': 5, 'color': 'BLUE', 'value': 'PLUS_TWO'},"
			+ "{'id': 6, 'color': 'GREEN', 'value': 'REVERSE'}, "
			+ "{'id': 7, 'color': 'BLACK', 'value': 'PLUS_FOUR'}, "
			+ "{'id': 8, 'color': 'BLACK', 'value': 'COLOR_CHANGE'}], "
			+ "'firstCard': {'id': 0, 'color': 'RED', 'value': 'ZERO'}"
			+ "}";
		client.processingMsg(new JSONObject(inputMsg));
	}
	private void testPlayer2UseCard() {
		String inputMsg = "{" + 
				"'id': 2, " + 
				"'playerId': 1," + 
				"'card': {'id': 0, 'color': 'RED', 'value': 'ZERO'}" + 
				"}";
		client.processingMsg(new JSONObject(inputMsg));
	}
	
	private void playerDrawCard() {
		String inputMsg = "{" + 
				"'id': 3, " + 
				"'playerId': 1," + 
				"'n': 2, " +
				"'cards': []" + 
				"}";
		client.processingMsg(new JSONObject(inputMsg));
	}
	private void currPlayerDrawCard() {
		String inputMsg = "{" + 
				"'id': 3, " + 
				"'playerId': 0," + 
				"'n': 2, " +
				"'cards': ["
				+ "{'id': 0, 'color': 'RED', 'value': 'ZERO'}, "
				+ "{'id': 1, 'color': 'BLUE', 'value': 'ZERO'}"
				+ "]" + 
				"}";
		client.processingMsg(new JSONObject(inputMsg));
	}
	private void changePlayer(int i) {
		String inputMsg = "{" + 
				"'id': 6, " + 
				"'playerId': " +
				i +
				"}";
		client.processingMsg(new JSONObject(inputMsg));
		
	}
	public void bootstrap() {
		client.connectToView(gamePane);
		client.getView().bindToStage(stage);
		client.clientStart();
		realBootstrap();
		//testBootstrap();
	}
	
    @FXML
    public void handleKeyPress(KeyEvent event) {
    	System.out.println("Key press: "+ event.getText());
    	switch(event.getCode()) {
    	case R:
    		System.out.println("testStartRound");
    		testStartRound();
    		break;
    	case U:
    		System.out.println("testPlayer2UseCard");
    		testPlayer2UseCard();
    		break;
    	case F:
    		System.out.println("playerDrawCard");
    		playerDrawCard();
    		break;
    	case G:
    		System.out.println("currPlayerDrawCard");
    		currPlayerDrawCard();
    		break;
    	case Z:
    		System.out.println("ith player");
    		changePlayer(0);
    		break;
    	case X:
    		System.out.println("ith player");
    		changePlayer(1);
    		break;
    	case C:
    		System.out.println("ith player");
    		changePlayer(2);
    		break;
    	case V:
    		System.out.println("ith player");
    		changePlayer(3);
    		break;
    	case B:
    		System.out.println("ith player");
    		changePlayer(4);
    		break;
		default:
			break;
    	}
    }
}
