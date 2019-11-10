package game.client;

import java.io.IOException;
import java.io.OutputStream;

public class MsgSender {
	private OutputStream outputStream;
	public MsgSender(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public boolean send(String msg) {
		try {
			
			outputStream.write((msg + "\n").getBytes());
			System.out.println("msg send: " + msg);
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	
}
