package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import simplechat.SimpleChat;
/**
 * This class wait and print messages from the interlocutor.
 * @author Federico
 * @version 0.0.1
 */
public class Receiver extends Thread
{
	SimpleChat sc;
	
	public Receiver(SimpleChat sc)
	{
		super("Receiver thread");
		this.sc = sc;
		start();
	}
	
	public void run()
	{
		try 
		{
			String text = "";
			BufferedReader inFromClient =  new BufferedReader(new InputStreamReader(sc.getInputStream()));
			while (!text.equals("-exit") && sc.isConnected()) 
			{
				text = inFromClient.readLine();
				System.out.println(text);
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
}
