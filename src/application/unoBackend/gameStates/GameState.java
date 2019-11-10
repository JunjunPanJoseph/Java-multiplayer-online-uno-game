package application.unoBackend.gameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.unoBackend.Card;
import application.unoBackend.Card.cardColor;
import application.unoBackend.Player;
import application.unoBackend.Playground;

public class GameState {
	private TurnState currState;
	private TurnState startState;
	private TurnState drawState;
	private TurnState cardState;
	
	private Playground playground;

	private List<Card> drawPile;
	private List<Card> discardPile;
	private cardColor currentColor;

	private int currPlayer;
	private int drawCardNum;
	private boolean playerSeqClockwise;
	
	public GameState(Playground playground) {
		this.playground = playground;
		this.startState = new StartState(this);
		this.drawState = new DrawState(this);
		this.cardState = new CardState(this);
		this.drawPile = new ArrayList<>();
		this.discardPile = new ArrayList<>();
	}
	
	public boolean useCard(Player player, Card card, Card.cardColor newColor) {
		boolean retVal = currState.useCard(player, card, newColor);
		return retVal;
	}
	public boolean drawCard(Player player) {
		boolean retVal = currState.drawCard(player);
		return retVal;
	}
	public boolean endTurn(Player player) {
		boolean retVal = currState.endTurn(player);
		return retVal;
	}
	

	public boolean startRound(int startPlayer) {
		//Player
		for (Player player: playground.getPlayers()) {
			player.reset();
		}
		this.currPlayer = startPlayer;
		this.drawCardNum = 0;
		this.playerSeqClockwise = true;
		
		drawPile.clear();
		discardPile.clear();
		
		drawPile.addAll(playground.getCardPile());
		Collections.shuffle(drawPile);
		for (Card card: drawPile) {
			if (card.getColor() != cardColor.BLACK) {
				discardPile.add(card);
				drawPile.remove(card);
				break;
			}
		}
		return true;
	}
	public boolean nextTurn() {
		List<Player> players = playground.getPlayers();
		while(true) {
			int currPlayerIndex = getNextPlayerIndex(currPlayer);
			if (players.get(currPlayerIndex).checkSkiped()) {
				players.get(currPlayerIndex).skipedSub();
			} else {
				break;
			}
		}
		return true;
	}
	
	/**
	 * @return the currState
	 */
	public TurnState getCurrState() {
		return currState;
	}
	/**
	 * @param currState the currState to set
	 */
	public void setCurrState(TurnState currState) {
		this.currState = currState;
	}
	/**
	 * @return the startState
	 */
	public TurnState getStartState() {
		return startState;
	}
	/**
	 * @return the drawState
	 */
	public TurnState getDrawState() {
		return drawState;
	}
	/**
	 * @return the cardState
	 */
	public TurnState getCardState() {
		return cardState;
	}
	/**
	 * @return the playground
	 */
	public Playground getPlayground() {
		return playground;
	}
	
	/**
	 * @return the drawPile
	 */
	public List<Card> getDrawPile() {
		return drawPile;
	}

	/**
	 * @return the discardPile
	 */
	public List<Card> getDiscardPile() {
		return discardPile;
	}

	public boolean setCurrPlayer(Player player) {
		if (!playground.getPlayers().contains(player)) {
			return false;
		} else {
			this.currPlayer = playground.getPlayers().indexOf(player);
			return true;
		}
	}
	public Card drawACard() {
		if (drawPile.size() == 0) {
			if (discardPile.size() < 1) {
				//Game Over: End of cards
				System.out.println("End of round");
				return null;
			} else {
				Card lastCard = discardPile.remove(discardPile.size() - 1);
				drawPile.addAll(discardPile);
				Collections.shuffle(drawPile);
				discardPile.clear();
				discardPile.add(lastCard);
			}
		}
		if (drawCardNum > 0) {
			drawCardNum --;
		}
		return drawPile.remove(0);
	}
	public void addCardToDiscardPile(Card card) {
		discardPile.add(card);
	}
	public cardColor getCurrColor() {
		return this.currentColor;
	}
	public Card getLatestCard() {
		//Need ensure discard pile not empty
		assert (discardPile.size() != 0);
		return discardPile.get(discardPile.size() - 1);
	}
	public void reversePlayerSeq() {
		playerSeqClockwise = !playerSeqClockwise;		
	}
	private int getNextPlayerIndex(int playerIndex) {
		if (playerSeqClockwise) {
			return (playerIndex + 1) / playground.getPlayers().size();
		} else {
			return (playground.getPlayers().size() + playerIndex - 1) / playground.getPlayers().size();
		}
	}
	public void skipNext(Player player) {
		List<Player> players = playground.getPlayers();
		int playerIndex = players.indexOf(player);
		int nextPlayerIndex = getNextPlayerIndex(playerIndex);
		Player nextPlayer = players.get(nextPlayerIndex);
		nextPlayer.skipedAdd();
	}
	public void drawCardAdd(int i) {
		assert(i >= 0);
		drawCardNum += i;
	}
	public void changeColor(cardColor newColor) {
		currentColor = newColor;
	}

	public boolean isCurrPlayer(Player player) {
		return currPlayer == playground.getPlayers().indexOf(player);
	}

	public boolean drawCards(Player player) {
		Card newCard;
		if (drawCardNum == 0) {
			drawCardAdd(1);
		}
		while (drawCardNum > 0) {
			newCard = drawACard();
			if (newCard == null) {
				return false;
			}
			player.addCard(newCard);
		}
		return true;
	}
	public int getDrawCardNum() {
		return drawCardNum;
	}
	public Playground getPlayground(Player player) {
		// TODO Auto-generated method stub
		return playground;
	}
}
