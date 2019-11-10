package application.unoBackend.gameStates;

import application.unoBackend.Card;
import application.unoBackend.Player;

public class StartState extends TurnState{

	public StartState(GameState gameState) {
		super(gameState);
		// TODO Auto-generated constructor stub
	}
	public boolean useCard(Player player, Card card, Card.cardColor newColor) {
		return useCard_Raw(player, card, newColor);
	}
	public boolean drawCard(Player player) {
		return drawCard_Raw(player);
	}
}