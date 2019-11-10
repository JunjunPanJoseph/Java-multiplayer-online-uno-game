package application.unoBackend.gameStates;

import application.unoBackend.Card;
import application.unoBackend.Player;
import application.unoBackend.Playground;
import application.unoBackend.Card.cardColor;

public abstract class TurnState {
	protected GameState gameState;
	protected boolean endTurn_Raw(Player player) {
		return gameState.getPlayground(player).endTurn();

	}
	protected boolean drawCard_Raw(Player player) {
		return gameState.drawCards(player);
	}
	protected boolean useCard_Raw(Player player, Card card, Card.cardColor newColor) {
		if (!checkPlayerMove(player, card, newColor)) {
			return false;
		}
		if (player.drapACard(card)) {
			assert(gameState.setCurrPlayer(player));
			gameState.addCardToDiscardPile(card);
			switch(card.getValue()) {
			case REVERSE:
				gameState.reversePlayerSeq();
				break;
			case SKIP:
				gameState.skipNext(player);
				break;
			case PLUS_TWO:
				gameState.drawCardAdd(2);
				break;
			case PLUS_FOUR:
				//BLACK card: need decide color
				gameState.drawCardAdd(4);
			case COLOR_CHANGE:
				gameState.changeColor(newColor);
				break;
			default:
				//Otherwise, do nothing
				break;
			}
			gameState.setCurrState(gameState.getCardState());
			return true;
		} else {
			return false;
		}
	}
	public boolean checkPlayerMove(Player player, Card card, Card.cardColor newColor) {
		if (!gameState.isCurrPlayer(player) && 
			!(card.getColor() == gameState.getLatestCard().getColor() &&
			card.getValue() == gameState.getLatestCard().getValue())) {
			//Not your turn
			return false;
		}
		//Check whether player own the card
		if(player == null || card == null) {
			return false;
		} 
		//Check whether player is skipped.
		if (player.checkSkiped()) {
			return false;
		}
		if (!player.getCards().contains(card)) {
			return false;
		}
		//Check whether the card is valid
		boolean checkResult = Playground.checkUseCard(0, gameState.getLatestCard(), gameState.getCurrColor(),
				gameState.getDrawCardNum(), 0, card, newColor);
		if(!checkResult) {
			return false;
		}
		return true;
	}
	public TurnState(GameState gameState){
		this.gameState = gameState;
	}
	public boolean useCard(Player player, Card card, Card.cardColor newColor) {
		return false;
	}
	public boolean drawCard(Player player) {
		return false;
	}
	public boolean endTurn(Player player) {
		return false;
	}
}
