package game.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import javafx.application.Platform;

public class MsgReceiver implements Runnable{
	private String threadName = "client msg receiver";
	private InputStream inputStream;
	private ClientModel model;
	private Thread thread;
	public MsgReceiver(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public void setModel(ClientModel model) {
		this.model = model;		
	}
	@Override
	public void run() {
		System.out.println("MsgReceiver running");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		while(true) {
			try {
				String str = reader.readLine();
				System.out.println("msg get: " + str);
				JSONObject obj = new JSONObject(str);
				processJSONmsg(obj);
			} catch (Exception e) {
				
			}
		}
		
	}
	public void start() {
		if (thread == null) {
			System.out.println("thread start");
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
	private void processJSONmsg(JSONObject obj) {
		try {
			int id = obj.getInt("id");
			System.out.println(id);
			
			switch (id) {
			case 102:
				//Javafx's interface is single thread. 
				//Need this line to ensure thread safe.
				//Also, without this line, this thread will be hanged forever... until eclipse closed.
				Platform.runLater(() -> {
					model.rec_joinGameRoom(obj);
				});
				break;
			
			}
		} catch (Exception e) {
			
		}
	}

}
