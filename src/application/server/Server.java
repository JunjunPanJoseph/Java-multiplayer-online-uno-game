package application.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
public class Server {
	private ServerSocket serverSocket; 
	
	public Server(){
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
			try {
	           System.out.println("Waiting for connect,  Port=" + serverSocket.getLocalPort() + "...");
	           //Will block the server
	           Socket server = serverSocket.accept();
	           System.out.println("Client address" + server.getRemoteSocketAddress());
	           DataInputStream in = new DataInputStream(server.getInputStream());
	           DataOutputStream out = new DataOutputStream(server.getOutputStream());
	           while(true) {
	        	   String str = in.readLine();
	        	   System.out.println(str);
	        	   out.writeUTF("Msg received");
	        	   if (false) {
	        		   break;
	        	   }
	           }
	           //This stage, only consider one player with multiple robots
	           //ServerModel currServer = new ServerModel(in, out);
	           //currServer.startServerModel();
	           server.close();
	        } catch(SocketTimeoutException s) {
	           System.out.println("Socket timed out!");
	           break;
	        } catch(IOException e) {
	           e.printStackTrace();
	           break;
	        }
		}
	}
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
		Server testServer = new Server();
		testServer.serverStart();
		
		log("Address: " + testServer.getAddress());
		log("Port: " + testServer.getPort());
		log("Server running...");
		testServer.run();
	}
}
