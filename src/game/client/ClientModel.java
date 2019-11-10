package game.client;

import org.json.JSONObject;

import game.client.states.ClientModelInterface;
import game.client.states.ClientSelectState;
import javafx.stage.Stage;

public class ClientModel implements ClientModelInterface{
	private ClientView clientView;
	private MsgSender msgSender;
	private ClientModelInterface state;
	
	public ClientModel() {
	}
	public void setMsgSender(MsgSender msgSender) {
		this.msgSender = msgSender;
	}
	public boolean sendMsg(String msg) {
		if (msgSender.send(msg)) {
			return true;
		} else {
			return false;
		}
	}
	public void errorMsg(String msg) {
		clientView.errorMsg(msg);
	}
	public void setState(ClientModelInterface state) {
		this.state = state;
	}
	public void send_newGameRoom(ClientPlayer currPlayer) {
		state.send_newGameRoom(currPlayer);
	}
	public void send_joinGameRoom(ClientPlayer currPlayer, int roomNo) {
		state.send_joinGameRoom(currPlayer, roomNo);
	}
	@Override
	public void rec_joinGameRoom(JSONObject obj) {
		state.rec_joinGameRoom(obj);
	}
	public ClientView getView() {
		return clientView;
	}
	public void setView(ClientView view) {
		clientView = view;
	}
	
}
