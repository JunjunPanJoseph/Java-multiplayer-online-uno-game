package game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class SyncServer {
	private ServerSocket serverSocket; 
	
	public SyncServer(){
		serverSocket = null;
	}
	public boolean serverStart() {
		if (serverSocket != null) {
			log("Error: server already started");
			return false;
		}
		try {
			serverSocket = new ServerSocket(5001);
			return true;
		} catch (IOException e) {
			log("Error: make serverSocket failed (IOException)");
			serverSocket = null;
			return false;
		}
	}
	public void run() {
		while(true) {
			int i = 0; 
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("accept socket");
				
				ClientThread clientThread = new ClientThread("client " + i, clientSocket);
				i++;
				clientThread.start();
				
			} catch (SocketTimeoutException s){
				
			} catch (IOException e) {
				
			}
		}
	}
	/**
	 * Getter
	 * @return
	 */
	public InetAddress getAddress() {
		if (serverSocket == null) {
			return null;
		}
		return serverSocket.getInetAddress();
	}
	public int getPort() {
		if (serverSocket == null) {
			return -1;
		}
		return serverSocket.getLocalPort();
	}
	private static void log(String string) {
		System.out.println(string);
	}
	
	public static void main(String[] args) {
		SyncServer syncServer = new SyncServer();
		syncServer.serverStart();
		
		log("Address: " + syncServer.getAddress());
		log("Port: " + syncServer.getPort());
		log("Server running...");
		syncServer.run();
	}
}
