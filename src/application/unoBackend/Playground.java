package application.unoBackend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.unoBackend.Card.cardColor;
import application.unoBackend.Card.cardValue;
import application.unoBackend.gameStates.GameState;

public class Playground {
	private List<Player> players;
	private List<Card> cardPile;
	private GameState currState;
	
	private int latestWinner;
	private int gameRound;
	private boolean roundEnd;
	private boolean gameStart;
	public Playground() {
		this.roundEnd = true;
		this.gameStart = false;
		this.players = new ArrayList<>();
		this.cardPile = new ArrayList<>();
		this.currState = new GameState(this);
		this.gameRound = 0;
	}
	public boolean addPlayer(Player player) {
		if (gameStart || player == null) {
			return false;
		} else {
			players.add(player);
			return true;
		}
	}
	public boolean startGame() {
		if (players.size() < 2) {
			return false;
		}
		this.gameStart = true;
		//Add Card
		this.cardPileBootstrap(3, 2);
		//Randomize
		Collections.shuffle(cardPile);
		this.latestWinner = 0;
		this.roundEnd = true;
		//Start round
		this.nextRound();
		return true;
	}
	
	private boolean nextRound() {
		if (this.roundEnd == false) {
			return false;
		}
		boolean retVal = currState.startRound(latestWinner);
		if (retVal) {
			this.gameRound++;
			this.roundEnd = false;
		}
		return retVal;
	}
	/**
	 * Player move: draw card / use card / endturn
	 * @return
	 */
	public boolean playerMove() {
		// TODO: Write a class for every move.
		return false;
	}
	
	private void cardPileBootstrap(int normalCardRound, int blackCardRound) {
		for (Card.cardColor color: Card.cardColor.values()) {
			if (color == Card.cardColor.BLACK) {
				continue;
			}
			for (Card.cardValue value: Card.cardValue.values()) {
				if (!(value == Card.cardValue.COLOR_CHANGE ||
					value == Card.cardValue.PLUS_FOUR)) {
					for (int i = 0; i < normalCardRound; i++) {
						cardPile.add(new Card(color, value));
					}
				}
			}
		}
		for (int i = 0; i < blackCardRound; i++) {
			cardPile.add(new Card(Card.cardColor.BLACK, Card.cardValue.PLUS_FOUR));
			cardPile.add(new Card(Card.cardColor.BLACK, Card.cardValue.COLOR_CHANGE));
		}
	}
	public List<Player> getPlayers() {
		return players;
	}

	public List<Card> getCardPile() {
		return cardPile;
	}
	public boolean endTurn() {
		if (!checkRoundEnd()) {
			this.roundEnd = true;
			return true;
		} else {
			boolean retVal = currState.nextTurn();
			if (retVal) {
				currState.setCurrState(currState.getStartState());
			}
			return retVal;
		}
	}
	private boolean checkRoundEnd() {
		if (currState.getDrawPile().size() == 0 && 
			currState.getDiscardPile().size() <= 1) {
			//No winner
			return true;
		}
		for (Player player: players) {
			if (player.getCards().size() == 0) {
				//Set latest winner
				latestWinner = players.indexOf(player);
				return true;
			}
		}
		return false;
	}
	public int getGameRound() {
		return gameRound;
	}
	public static boolean checkUseCard(int currentPlayerIndex, Card lastCard, 
			Card.cardColor currColor, int nDraw,  
			int playerIndex, Card newCard, Card.cardColor newColor) {
		if (newCard.equals(lastCard)) {
			return true;
		}
		if (currentPlayerIndex != playerIndex) {
			return false;
		}
		if (newColor == cardColor.BLACK) {
			return false;
		}
		if (nDraw > 0) {
			switch(lastCard.getValue()) {
			case PLUS_TWO:
				if (newCard.getValue() == cardValue.PLUS_TWO) {
					return true;
				}
			case PLUS_FOUR:
				if (newCard.getValue() == cardValue.PLUS_FOUR) {
					return true;
				}
			default:
				break;
			}
			return false;
		} else {
			if (newCard.getColor() == cardColor.BLACK || 
				newCard.getColor() == currColor||
				lastCard.getValue() == newCard.getValue()) {
				return true;
			} else {
				return false;
			}
		}
	}
}
