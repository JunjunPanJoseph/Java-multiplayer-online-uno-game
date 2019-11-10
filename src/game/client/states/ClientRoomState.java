package game.client.states;

import org.json.JSONObject;

import game.client.ClientModel;
import game.client.ClientPlayer;
import game.client.views.RoomView;

public class ClientRoomState implements ClientModelInterface {
	private ClientModel clientModel;
	private ClientSelectState clientStartState;
	private RoomView roomView;
	public ClientRoomState(ClientModel clientModel, ClientSelectState clientStartState) {
		this.clientModel = clientModel;
		this.clientStartState = clientStartState;
	}
	
	/**
	 * @param roomView the roomView to set
	 */
	public void setRoomView(RoomView roomView) {
		this.roomView = roomView;
	}

	@Override
	public void send_newGameRoom(ClientPlayer currPlayer) {}

	@Override
	public void send_joinGameRoom(ClientPlayer currPlayer, int roomNo) {}

	@Override
	public void rec_joinGameRoom(JSONObject obj) {
		// TODO Auto-generated method stub

	}

	public boolean setRoomCurr(JSONObject obj) {
		// TODO Auto-generated method stub
		return true;
	}

	public void viewStart() {
		roomView.start();
	}

}
