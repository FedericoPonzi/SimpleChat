package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import simplechat.SimpleChat;

/**
 * This class wait and print messages from the interlocutor.
 * 
 * @author Federico
 * @version 0.0.1
 */
public class Receiver extends Thread
{
	SimpleChat sc;

	public Receiver()
	{
		super("Receiver thread");
		this.sc = SimpleChat.getInstance();
		start();
	}

	public void run()
	{
		try
		{
			String text = "";
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			sc.setAddresseeNickname(inFromClient.readLine());
			//TODO: Fix this:
			Integer i = 0; //Dunno how to fix. Without this, the addreesseeNickname from the previous statement will be printed :C
			while (sc.isConnected())
			{
				text = inFromClient.readLine();
				if(text.equals("01-CLOSE_REQ")) sc.closeConnection();
				else
				{
					if(i != 0) sc.chatLogWrite(sc.getAddresseeNickname() + ": " + text);
					else i++;
				}
			}
			sc.closeConnection();
		}
		catch (SocketException e)
		{
			System.err.println("SocketException! :C");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
