package application.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import application.unoBackend.Card;
import application.unoBackend.Card.cardColor;
import application.unoBackend.Card.cardValue;
import application.unoBackend.Playground;

public class ClientModel {
	public enum state{NOT_SETUP, NOT_RUNNING, RUNNING};
	private List<ClientModelPlayer> players;
	private List<Card> hand;

	private state currState; 
	private int round;
	private int currPlayerIndex;
	private Card topCard;
	private Card.cardColor currColor;
	private int nDraw;
	private int currTurnPlayerIndex;
	
	public ClientModel() {
		players = new ArrayList<>();
		hand = new ArrayList<>();
		currState = state.NOT_SETUP;
	}
	public void resetClientModel() {
		players.clear();
		hand.clear();
		currState = state.NOT_SETUP;
		round = 0;
		currPlayerIndex = -1;
		topCard = null;
		nDraw = 0;
		currColor = cardColor.BLACK;
	}
	public boolean recv_setUpClientModel(JSONObject msg) {
		if (currState != state.NOT_SETUP) {
			return false;
		}
		JSONArray playersJSONArray = msg.getJSONArray("players");
		//Player of client
		int tmpCurrPlayer = msg.getInt("playerId");
		if (tmpCurrPlayer < 0 || tmpCurrPlayer >= playersJSONArray.length()) {
			return false;
		}
		ClientModelPlayer[] playersArray = new ClientModelPlayer[playersJSONArray.length()];
		for (int i = 0; i < playersJSONArray.length(); i++) {
			playersArray[i] = null;
		}
		for (int i = 0; i < playersJSONArray.length(); i++) {
			JSONObject playerJSONObject = playersJSONArray.getJSONObject(i);
			ClientModelPlayer newPlayer = new ClientModelPlayer(playerJSONObject);
			if (playersArray[newPlayer.getId()] == null) {
				playersArray[newPlayer.getId()] = newPlayer;
			} else {
				//Repeated id
				return false;
			}
		}
		//Set up content
		resetClientModel();
		this.currPlayerIndex = tmpCurrPlayer;
		for (int i = 0; i < playersArray.length; i++) {
			players.add(playersArray[i]);
		}
		currState = state.NOT_RUNNING;
		return true;
	}
	public int getCurrPlayerIndex() {
		// TODO Auto-generated method stub
		return currPlayerIndex;
	}
	public List<ClientModelPlayer> getPlayers(){
		return players;
	}
	public List<Card> getHand(){
		return hand;
	}
	public boolean recv_startRound(JSONObject msg) {
		if (currState != state.NOT_RUNNING) {
			System.out.println("Error: state is not NOT_RUNNING, please check with the code");
			return false;
		}
		int nCard = msg.getInt("nCards");
		int startPlayer = msg.getInt("startPlayer");
		JSONArray handJSONArray = msg.getJSONArray("hand");
		JSONObject firstCard = msg.getJSONObject("firstCard");
		if (nCard != handJSONArray.length()) {
			System.out.println("Error: nCard != handJSONArray.length()");
			return false;
		}
		if (!validPlayerId(startPlayer)) {
			System.out.println("Error: invalid start player");
			return false;
		}
		round++;
		currTurnPlayerIndex = startPlayer;
		currState = state.RUNNING;
		topCard = new Card(firstCard.getString("color"), firstCard.getString("value"));
		currColor = topCard.getColor();
		nDraw = 0;
		for (ClientModelPlayer player: players) {
			player.drawCards(nCard);
		}
		JSONObject[] tmpCardList = new JSONObject[handJSONArray.length()];
		for (int i = 0; i < handJSONArray.length(); i++) {
			tmpCardList[i] = null;
		}
		for (int i = 0; i < handJSONArray.length(); i++) {
			JSONObject card = handJSONArray.getJSONObject(i);
			if (tmpCardList[card.getInt("id")] != null) {
				System.out.println("Error: repeated card id");
				return false;
			} else {
				tmpCardList[card.getInt("id")] = card;
			}
		}
		for (int i = 0; i < handJSONArray.length(); i++) {
			hand.add(new Card(tmpCardList[i].getString("color"), tmpCardList[i].getString("value")));
		}
		return true;
	}
	public Card getTopCard() {
		// TODO Auto-generated method stub
		return topCard;
	}

	public boolean checkplayerUseCard(int newCardIndex, Card.cardColor newColor) {
		Card newCard = hand.get(newCardIndex);
		if (newCard == null) {
			return false;
		}
		boolean checkVal = Playground.checkUseCard(currTurnPlayerIndex, topCard, currColor, nDraw, currPlayerIndex, newCard, newColor);
		return checkVal;
	}
	public int getCurrTurnPlayerIndex() {
		// TODO Auto-generated method stub
		return currTurnPlayerIndex;
	}
	public boolean recv_useCard(JSONObject msg) {
		int playerId = msg.getInt("playerId");
		if (!validPlayerId(playerId)) {
			return false;
		}
		ClientModelPlayer player = players.get(playerId);
		JSONObject cardJSON = msg.getJSONObject("card");
		if (player.getNCards() <= 0) {
			return false;
		}
		if (currPlayerIndex == playerId) {
			if (!useHandJSON(cardJSON)) {
				return false;
			}
		}
		return player.useCard();
	}
	public boolean checkHandJSON(JSONObject obj) {
		int cardId = obj.getInt("id");
		if (cardId < 0 || cardId >= hand.size()) {
			return false;
		}
		Card card = hand.get(cardId);
		return card.equals(obj);
	}
	private boolean useHandJSON(JSONObject cardJSON) {
		if (!checkHandJSON(cardJSON)) {
			return false;
		}
		int cardId = cardJSON.getInt("id");
		hand.remove(cardId);
		return true;
	}
	public boolean validPlayerId(int playerId) {
		// TODO Auto-generated method stub
		return (playerId >= 0 && playerId < players.size());
	}
	public boolean recv_drawCard(JSONObject msg) {
		int playerId = msg.getInt("playerId");
		if (!validPlayerId(playerId)) {
			return false;
		}
		ClientModelPlayer player = players.get(playerId);
		
		if (currPlayerIndex == playerId) {
			JSONArray cards = msg.getJSONArray("cards");
			Card[] cardsList = new Card[cards.length()];
			for(int i = 0; i < cards.length(); i++) {
				cardsList[i] = null;
			}
			for(int i = 0; i < cards.length(); i++) {
				JSONObject cardJSON = cards.getJSONObject(i);
				int id = cardJSON.getInt("id");
				if (i < 0 || id >= cards.length() || cardsList[id] != null) {
					return false;
				}
				cardsList[i] = new Card(cardJSON.getString("color"), cardJSON.getString("value"));
			}
			for(int i = 0; i < cards.length(); i++) {
				hand.add(cardsList[i]);
			}
		}
		return  player.drawCards(msg.getInt("n"));
	}
	public boolean setCurrPlayer(int currPlayerID) {
		currTurnPlayerIndex = currPlayerID;
		return true;
	}
	public boolean recv_setCurrPlayer(JSONObject inputMsg) {
		return setCurrPlayer(inputMsg.getInt("playerId"));
	}
	public boolean roundEnd() {
		if (currState != state.RUNNING) {
			return false;
		}
		currState = state.NOT_RUNNING;
		return true;
	}
	public boolean gameEnd() {
		if (currState == state.NOT_SETUP) {
			return false;
		}
		currState = state.NOT_SETUP;
		return true;
	}
	public int getRound() {
		return round;
	}
}
