package application.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONObject;

import application.unoBackend.Card;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Client {
	private ClientModel model;
	private ClientView view;
	
	private Socket client;
	private DataInputStream inStream;
	private OutputStream outStream;
	public Client(){
		model = new ClientModel();
		view = new ClientView(this);
	}
	public void connectToView(Pane gamePane) {
		view.setPane(gamePane);
		view.resetView();
	}
	public boolean clientStart() {
		String serverName = "14.203.67.223";
		int port = 5001;
		try {
			System.out.println("connect to server" + serverName + "  port: " + port);
			client = new Socket(serverName, port);
			System.out.println("Address£º" + client.getRemoteSocketAddress());
			InputStream inFromServer = client.getInputStream();
			inStream = new DataInputStream(inFromServer);
			OutputStream outToServer = client.getOutputStream();
			outStream = new DataOutputStream(outToServer);
			System.out.println("=====  Client start!  =====");
			class RunnableDemo implements Runnable {
				private Thread t;
				private String threadName;
				RunnableDemo( String name) {
					threadName = name;
					System.out.println("Creating " +  threadName );
				}
				public void run() {
					System.out.println("Running " +  threadName );
					while(true) {
						
						String str;
						try {
							str = inStream.readLine();
							view.log("receive msg:" + str);
							JSONObject inputMsg = new JSONObject(str);
							processingMsg(inputMsg);
							
						} catch (Exception e) {
						}
						
					}
				}
				public void start () {
					System.out.println("Starting " +  threadName );
					if (t == null) {
						t = new Thread (this, threadName);
						t.start ();
					}
    	 		}
			}
			RunnableDemo R1 = new RunnableDemo( "Thread-1");
         	R1.start();
		} catch(IOException e) {
			e.printStackTrace();
	    }
		return true;
	}
	public boolean clientEnd() {
		try {
			client.close();
		} catch (Exception e){
			
		}
        return true;
	}
	public int getPlayerIndex() {
		// TODO Auto-generated method stub
		return model.getCurrPlayerIndex();
	}
	public boolean changeCurrPlayer(int currPlayerID) {
		if (model.setCurrPlayer(currPlayerID)) {
			view.setCurrPlayer(currPlayerID);
			return true;
		}
		return false;
	}
	public ClientView getView() {
		return view;
	}
	

	public void processingMsg(JSONObject inputMsg) {
		// TODO Auto-generated method stub
		int msgId = inputMsg.getInt("id");
		switch (msgId) {
		case 0:
			//Setup client
			System.out.println("Setup client");
			if (model.recv_setUpClientModel(inputMsg)) {
				view.recv_setUpGameView(model);
			} else {
				System.out.println("ERROR in set up client model.");
			}
			break;
		case 1:
			//Start round
			if (model.recv_startRound(inputMsg)) {
				view.recv_setUpRound(model);
			}
			break;
		case 2:
			//Use card
			if (model.recv_useCard(inputMsg)) {
				System.out.println("Model use success");
				
				view.recv_useCard(inputMsg);
			}
			break;
		case 3:
			//Draw card
			if (model.recv_drawCard(inputMsg)) {
				System.out.println("Model draw success");
				view.recv_drawCard(inputMsg);
			}
			break;
		case 6:
			//Change round
			if (model.recv_setCurrPlayer(inputMsg)) {
				System.out.println("model recv_setCurrPlayer");
				view.recv_setCurrPlayer(inputMsg);
			}
			break;
		}
		if (inputMsg.has("roundEnd")) {
			//End round
			if (model.roundEnd()) {
				view.roundEnd(inputMsg);
			}
		}
		if (inputMsg.has("gameEnd")) {
			if (model.gameEnd()) {
			view.gameEnd(inputMsg);
			}
		}
		
		
	}
	public JSONObject getServerMsgDump() {
		System.out.println("Enter server msg: ");
		
		Scanner sc=new Scanner(System.in);
		String str=new String();
		while (true) {
			str=sc.nextLine();
			try {
				JSONObject object = new JSONObject(str);
				sc.close();
				return object;
			} catch (Exception e) {
				System.out.println("Error: can not make json obj from input.");
				System.out.println("Enter server msg: ");
			}
		}
	}
	public boolean sendServerMsg(JSONObject msg) {
		//System.out.println("Sending msg: " + msg);
		try {
			outStream.write((msg.toString()+"\n").getBytes("UTF-8"));
			System.out.println("Success send: " +  msg.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public JSONObject makeServerMsg(int id) {
		//Meaning of id: see msgJSON.json
		JSONObject msg = new JSONObject();
		msg.append("id", id);
		msg.append("playerId", getPlayerIndex());
		return msg;
	}
	public boolean send_playerUseCard(int newCard, Card.cardColor newColor) {
		if (model.checkplayerUseCard(newCard, newColor)) {
			//Tell server to use the card			
			JSONObject msg = makeServerMsg(2);
			JSONObject card =  model.getHand().get(newCard).toJSON(newCard);
			msg.append("card", card);
			
			return sendServerMsg(msg);
		} else {
			return false;
		}
	}
	public boolean send_drawCard() {
		if (model.getCurrTurnPlayerIndex() == model.getCurrPlayerIndex()) {
			JSONObject msg = makeServerMsg(3);
			System.out.println("Send draw card message");
			return sendServerMsg(msg);
		}
		return false;
	}
	public boolean send_startGameACK() {
		JSONObject msg = makeServerMsg(7);
		System.out.println("Send startGameACK");
		return sendServerMsg(msg);
	}
	public boolean send_UNO() {
		JSONObject msg = makeServerMsg(4);
		System.out.println("Send UNO");
		return sendServerMsg(msg);
	}
	public boolean send_Catch(int targetId) {
		JSONObject msg = makeServerMsg(5);
		msg.append("targetId", targetId);
		System.out.println("Send Catch");
		return sendServerMsg(msg);
	}

}
