package application.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import application.scene.gameLocal.gameNode.CardNode;
import application.scene.gameLocal.gameNode.ClientCardPileNode;
import application.scene.gameLocal.gameNode.ClientPlayerNode;
import application.unoBackend.Card;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class ClientView {
	private Client client;
	private Pane gamePane;
	private Text currentDisplayMessage;
	private Text logText;
	private IntegerProperty height;
	private IntegerProperty width;
	
	private int currPlayerID;
	private int nPlayer;
	private List <ClientPlayerNode> playerNodes;
	private ClientCardPileNode cardPile;
	private ClientCardPileNode discardPile;
	public ClientView(Client client){
		this.client = client;
		this.currPlayerID = -1;
		gamePane = new Pane();
		currentDisplayMessage = new Text();
		currentDisplayMessage.setLayoutX(0);
		currentDisplayMessage.setLayoutY(100);
		logText = new Text();
		logText.setLayoutX(0);
		logText.setLayoutY(150);
		height = new SimpleIntegerProperty();
		width = new SimpleIntegerProperty();
		//initalize other objects
		playerNodes = new ArrayList<>();
		resetView();
	}
	public Pane getGamePane() {
		return gamePane;
	}
	public void bindToStage(Stage stage) {
		//Bind to stage 's size
		height.bind(stage.heightProperty());
		width.bind(stage.widthProperty());
		//Necessary to keep height to bind with height and width...
		
		stage.heightProperty().addListener(new ChangeListener <Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				//System.out.println("new height: " + newValue +"  view height: " + height.doubleValue());
			}
	    });
		stage.widthProperty().addListener(new ChangeListener <Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				//System.out.println("new width: " + newValue +"  view width: " + width.doubleValue());
			}
		});
		
	}
	public boolean setPane(Pane newPane) {
		if (newPane == null) {
			System.out.println("new game pane is null => fail");
			return false;
		}
		//set up new pane
		this.gamePane = newPane;
		return true;
	}
	public boolean resetView() {
		gamePane.getChildren().clear();
		nPlayer = 0;
		playerNodes.clear();
		cardPile = null;
		discardPile = null;
		currentDisplayMessage.setText("");
		logText.setText("log text");
		return true;
	}
	public boolean setCurrPlayer(int newCurrPlayer) {
		if (currPlayerID >= 0) {
			playerNodes.get(currPlayerID).unSetCurrentPlayer();
		}
		playerNodes.get(newCurrPlayer).setCurrentPlayer();
		currPlayerID = newCurrPlayer;
		return true;
	}
	public boolean recv_setUpGameView(ClientModel model) {
		//Reset
		List<ClientModelPlayer> players = model.getPlayers();
		currPlayerID = -1;
		this.nPlayer = players.size();
		//Add player nodes
		for (ClientModelPlayer player: players) {
			ClientPlayerNode newPlayerNode = new ClientPlayerNode(player, this);
			newPlayerNode.bind(height, width);
			playerNodes.add(newPlayerNode);
			gamePane.getChildren().add(newPlayerNode.getNode());
		}
		//Card pile
		int nCards = 100;
		cardPile = new ClientCardPileNode(nCards, 0.25, 0.5, false);
		cardPile.bind(height, width);
		EventHandler<MouseEvent> clickCardPileHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(client.send_drawCard()) {
					// TODO: reflex animation
				} else {
					// TODO: reject animation
				}
			}
		};
		cardPile.getNode().setOnMouseClicked(clickCardPileHandler);
		cardPile.setMouseTransparent(false);
	    gamePane.getChildren().add(cardPile.getNode());
		//Discard pile
		discardPile = new ClientCardPileNode(0, 0.45, 0.40, true);
		discardPile.bind(height, width);
	    gamePane.getChildren().add(discardPile.getNode());
	    //Message field
	    currentDisplayMessage.setText("Game setup finished");
	    gamePane.getChildren().add(currentDisplayMessage);
	    gamePane.getChildren().add(logText);
		return true;
	}
	public int getClientPlayerId() {
		return client.getPlayerIndex();
	}
	public int getNPlayer() {
		// TODO Auto-generated method stub
		return nPlayer;
	}
	public ClientCardPileNode getCardPile() {
		return cardPile;
	}
	private boolean setupAnimation_drawCards(List <Pair<ClientPlayerNode, Card>> drawCardQueue, int playerIndex, List<Card> cards) {
		ClientPlayerNode player = playerNodes.get(playerIndex);
		for (int i = 0; i < cards.size(); i++) {
			Card newCard;
			if (getClientPlayerId() != playerIndex) {
				newCard = null;
			} else {
				newCard = cards.get(i);
			}
			drawCardQueue.add(new Pair<ClientPlayerNode, Card>(player, newCard));		
		}
		return true;
	}
	
	private void playDrawCardQueue(List<Pair<ClientPlayerNode, Card>> drawCardQueue) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> draw = new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent t) {
        		if (drawCardQueue.size() == 0) {
        			timeline.stop();
        		} else {
        			Pair<ClientPlayerNode, Card> currPair = drawCardQueue.remove(0);
        			currPair.getKey().drawACard(currPair.getValue(), true);
        		}
            }
        };
		KeyFrame kf = new KeyFrame(Duration.millis(50), draw);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
	
	public boolean recv_setUpRound(ClientModel model) {
		List<Card> hand = model.getHand();
		List<ClientModelPlayer> players = model.getPlayers();
		int startPlayer = model.getCurrPlayerIndex();
		setCurrPlayer(startPlayer);
		List <Pair<ClientPlayerNode, Card>> drawCardQueue = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			int currPlayerIndex = (startPlayer + i) % players.size();
			setupAnimation_drawCards(drawCardQueue, currPlayerIndex, hand);
		}
		playDrawCardQueue(drawCardQueue);
	    discardPile.addCard(model.getTopCard(), null);
		return false;
	}
	public boolean playerUseCard(int newCard, Card.cardColor newColor) {
		if (client.send_playerUseCard(newCard, newColor)) {
			//Do something¡£¡£¡£
			return true;
		} else {
			//Show error msg
			return false;
		}
	}
	public boolean recv_useCard(JSONObject msg) {
		int playerId = msg.getInt("playerId");
		JSONObject card = msg.getJSONObject("card");
		boolean retVal =  playerNodes.get(playerId).useCardAnimation(card);
		// TODO: Deal with other effects
		return retVal;
	}
	public ClientCardPileNode getDiscardPile() {
		return discardPile;
	}
	public boolean recv_drawCard(JSONObject msg) {
		int playerId = msg.getInt("playerId");
		List<Card> cards = new ArrayList<>();
		JSONArray cardsJSON = msg.getJSONArray("cards");
		for (int i = 0; i < msg.getInt("n"); i++) {
			cards.add(null);
		}
		for (int i = 0; i < cardsJSON.length(); i++) {
			JSONObject cardJSON = cardsJSON.getJSONObject(i);
			System.out.println(cardJSON);
			System.out.println(new Card(cardJSON.getString("color"), cardJSON.getString("value")));
			cards.set(cardJSON.getInt("id"), new Card(cardJSON.getString("color"), cardJSON.getString("value")));
		}
		System.out.println(cards);
		List <Pair<ClientPlayerNode, Card>> drawCardQueue = new ArrayList<>();
		setupAnimation_drawCards(drawCardQueue, playerId, cards);
		
		playDrawCardQueue(drawCardQueue);
		return true;
	}
	public boolean recv_setCurrPlayer(JSONObject inputMsg) {
		return setCurrPlayer(inputMsg.getInt("playerId"));
	}
	public void roundEnd(JSONObject inputMsg) {
		JSONArray playerScores = inputMsg.getJSONArray("roundEnd");
		this.currentDisplayMessage.setText(playerScores.toString());
	}
	public void gameEnd(JSONObject inputMsg) {
		JSONArray winner = inputMsg.getJSONArray("gameEnd");
		this.currentDisplayMessage.setText("Winner is player " + winner.toString());
	}
	public void log(String string) {
		logText.setText(logText.getText() + "|\n" + string); 
	}
}
