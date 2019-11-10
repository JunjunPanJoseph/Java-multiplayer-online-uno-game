package application.unoBackend;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private List<Card> cards;
	private Playground playground;
	private int skiped;
	public Player(Playground playground, String name) {
		this.name = name;
		this.cards = new ArrayList<>();
		this.playground = playground;
	}
	
	public String getName() {
		return name;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void addCard(Card card){
		cards.add(card);
	}
	public Card getCard(int cardLoc) {
		if (cardLoc < 0 || cardLoc > cards.size()) {
			return null;
		}
		return cards.get(cardLoc);
	}
	public void skipedAdd() {
		skiped += 1;
	}
	public void skipedSub() {
		if (skiped > 0) {
			skiped -= 1;
		}
	}
	public boolean checkSkiped() {
		return skiped > 0;
	}

	public boolean drapACard(Card card) {
		// TODO Auto-generated method stub
		return false;
	}
	public void reset() {
		cards.clear();
		skiped = 0;
	}
}
