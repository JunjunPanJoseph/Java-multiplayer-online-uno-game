package game.client.states;

import org.json.JSONObject;

import game.client.ClientPlayer;

public interface ClientModelInterface {
	public void send_newGameRoom(ClientPlayer currPlayer);
	public void send_joinGameRoom(ClientPlayer currPlayer, int roomNo);
	
	public void rec_joinGameRoom(JSONObject obj);
}
