package application.unoBackend.gameStates;

import application.unoBackend.Player;

public class CardState extends TurnState{

	public CardState(GameState gameState) {
		super(gameState);
		// TODO Auto-generated constructor stub
	}
	public boolean endTurn(Player player) {
		return endTurn_Raw(player);
	}
}
