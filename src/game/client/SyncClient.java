package game.client;

import java.net.Socket;


public class SyncClient {
	private String serverName = "14.203.67.223";
	private int port = 5001;
	
	private Socket clientSocket;
	private ClientModel model;
	
	public SyncClient() {
		
	}
	public boolean connectToServer() {
		try {
			clientSocket = new Socket(serverName, port);
			log("connect to " + serverName + ":" + port);
			MsgSender msgSender = new MsgSender(clientSocket.getOutputStream());
			MsgReceiver msgReceiver = new MsgReceiver(clientSocket.getInputStream());
			model = new ClientModel();
			model.setMsgSender(msgSender);
			msgReceiver.setModel(model);
			msgReceiver.start();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public ClientModel getModel() {
		return model;
	}
	private static void log(String string) {
		System.out.println(string);
	}
}
