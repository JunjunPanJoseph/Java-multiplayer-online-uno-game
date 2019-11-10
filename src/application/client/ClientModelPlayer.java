package application.client;

import org.json.JSONObject;

public class ClientModelPlayer{
	//Static information
	private int id;
	private String name;
	
	//Dynamic information
	private int nCards;
	
	public ClientModelPlayer(JSONObject playerJSONObject) {
		this.id = playerJSONObject.getInt("id");
		this.name = playerJSONObject.getString("name");
		this.reset();
	}
	private void reset() {
		this.nCards = 0;		
	}
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public double getRelID() {
		return id;
	}
	public int getRelLoc(int clientPlayerId, int totalPlayersNumber) {
		return (id + totalPlayersNumber - clientPlayerId) % totalPlayersNumber;
	}
	public String getName() {
		return name;
	}
	public boolean drawCards(int nCards) {
		this.nCards += nCards;
		return true;
	}
	public boolean useCard() {
		if (nCards > 0) {
			nCards --;
			return true;
		}
		return false;
	}
	public int getNCards() {
		// TODO Auto-generated method stub
		return nCards;
	}
}