package application.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerModel {
	private DataInputStream in;
	private DataOutputStream out;
	public ServerModel(DataInputStream in, DataOutputStream out) {
		this.in = in;
		this.out = out;
	}
	public void startServerModel() {
		while (true) {
			String str;
			try {
				str = in.readUTF();
				System.out.println("==<[" + str + ">]==");
			} catch (IOException e) {
			}
		}
	}
	public void stopServerModel() {
	}

}
