
package common;

import java.io.IOException;

import client.ChatClient;


public abstract class UserControl 
{
	protected ChatClient chat;
	/* connections variables */
	private String ip = "192.168.1.70";// server ip
	final public static int DEFAULT_PORT = 5555;
  public abstract void checkMessage(Object message);
  
  /* this method connected between client and server */
	public void connect(){
		try {
			chat = new ChatClient(ip, DEFAULT_PORT, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}
}
