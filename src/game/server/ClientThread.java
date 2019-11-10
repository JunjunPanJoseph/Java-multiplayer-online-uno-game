package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClientThread implements Runnable{
	private String threadName;
	private Socket clientSocket;
	private InputStream inputStream;
	private OutputStream outputstream;
	private Thread thread;
	
	public ClientThread(String threadName, Socket clientSocket) {
		this.threadName = threadName;
		this.clientSocket = clientSocket;
	}
	
	public void start() {
		log("Thread: " + threadName + " start.");
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
	private void log(String string) {
		System.out.println(string);
	}
	private boolean setIOStream() {
		try {
			inputStream = clientSocket.getInputStream();
			outputstream = clientSocket.getOutputStream();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public void run() {
		System.out.println("Thread running");
		if (!setIOStream()) {
			//Thread end
			System.out.println("fail in set IO stream");
			return;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		while(true) {
			String str;
			try {
				str = reader.readLine();
				System.out.println("Receive: "+ str);
				JSONObject obj = new JSONObject(str);
				recv_JSONmsg(obj);
			} catch (IOException e1) {
				System.out.println("IO exception - stream may be closed. Exit the thread");
				break;
			}
		}
	}

	private void recv_JSONmsg(JSONObject obj) {
		try {
			int id = obj.getInt("id");
			switch (id) {
			case 100:
				System.out.println("RECV: 100 make new room");
				JSONObject msg = new JSONObject();
				msg.put("id", 102);
				msg.put("roomNo", -1);
				msg.put("playerId", -1);
				JSONArray players = new JSONArray();
				msg.put("players", players);
				outputstream.write((msg.toString() + "\n").getBytes());
				break;
			}
		} catch (Exception e) {
			//Nothing
		}
	}

}

