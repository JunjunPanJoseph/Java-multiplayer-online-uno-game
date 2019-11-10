package application.server;

import java.net.*;
import java.io.*;
 
public class GreetingClient
{
   public static void main(String [] args)
   {
      String serverName = "14.203.67.223";
      int port = 5001;
      try
      {
         System.out.println("connect to server" + serverName + "  port: " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Address£º" + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         for (int i = 0; i < 10; i++) {
        	 out.writeUTF("Text msg " + i + "  {'id': 1, 'name': test}");
         }
         out.writeUTF("stop");
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}