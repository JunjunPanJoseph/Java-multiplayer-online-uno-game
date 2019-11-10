package game.client.states;

import org.json.JSONObject;

import game.client.ClientModel;
import game.client.ClientPlayer;
import game.client.views.RoomView;
import game.client.views.SelectView;

public class ClientSelectState implements ClientModelInterface{
	private ClientModel clientModel;
	private ClientRoomState clientRoomState;
	private SelectView selectView;
	
	public ClientSelectState(ClientModel clientModel) {
		this.clientModel = clientModel;
		this.clientRoomState = new ClientRoomState(clientModel, this);
	}
	
	public void setSelectView(SelectView selectView) {
		this.selectView = selectView;
	}

	public SelectView getSelectView() {
		return selectView;
	}

	@Override
	public void send_newGameRoom(ClientPlayer currPlayer) {
		JSONObject msg = new JSONObject("{'id': 100}");
		JSONObject playerJSON = currPlayer.toJSON();
		msg.put("player", playerJSON);
		//Can add more details to the msg, like the number of card draws,
		//Player number, and so on
		clientModel.sendMsg(msg.toString());
	}
	
	
	@Override
	public void send_joinGameRoom(ClientPlayer currPlayer, int roomNo) {
		
		JSONObject msg = new JSONObject("{'id': 101}");
		JSONObject playerJSON = currPlayer.toJSON();
		msg.put("player", playerJSON);
		msg.put("roomNo", roomNo);
		clientModel.sendMsg(msg.toString());
	}

	@Override
	public void rec_joinGameRoom(JSONObject obj) {
		int playerId = obj.getInt("playerId");
		if (playerId < 0) {
			clientModel.errorMsg("Join game room failed. Please check room num / network");
		} else {
			if (clientRoomState.setRoomCurr(obj)) {
				RoomView roomView = new RoomView(clientModel.getView(), clientModel.getView().getStage());
				roomView.setSelectView(selectView);
				clientRoomState.setRoomView(roomView);
				clientModel.setState(clientRoomState);
				clientRoomState.viewStart();
			} else {
				clientModel.errorMsg("Fail in set up play room state");
			}
		}
	}

}
