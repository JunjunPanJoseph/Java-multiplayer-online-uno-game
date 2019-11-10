package application.unoBackend;

import org.json.JSONObject;

public class Card {
	public static enum cardColor {RED, GREEN, YELLOW, BLUE, BLACK};
	public static enum cardValue {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, REVERSE, SKIP, PLUS_TWO, PLUS_FOUR, COLOR_CHANGE};
	
	private cardColor color;
	private cardValue value;
	public Card(String color, String value) {
		this.color = cardColor.valueOf(color);
		this.value = cardValue.valueOf(value);
		assert (color != null && value != null);
	}
	public Card(cardColor color, cardValue value) {
		this.color = color;
		this.value = value;
	}
	public cardColor getColor() {
		return color;
	}
	public cardValue getValue() {
		return value;
	}
	@Override
	public String toString() {
		return "Card [color=" + color + ", value=" + value + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj instanceof JSONObject)) {
			if (this.getColor().toString().equals(((JSONObject) obj).get("color"))&& 
				this.getValue().toString().equals(((JSONObject) obj).getString("value"))) {
				return true;
			} else {
				return false;
			}
		} else {
			if (this.getColor() == ((Card) obj).getColor() && 
				this.getValue() == ((Card) obj).getValue()) {
				return true;
			} else {
				return false;
			}
			
		}
	}
	public JSONObject toJSON(int id) {
		JSONObject msg = new JSONObject();
		msg.append("id", id);
		msg.append("color", getColor());
		msg.append("value", getValue());
		return msg;
	}
	
}
