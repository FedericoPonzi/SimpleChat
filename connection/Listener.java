package connection;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import simplechat.SimpleChat;

/**
 * This class is started with the main SimpleChat class. It waits for a
 * connection, and if a socket havent already been created it will create a pipe
 * with the client.
 * 
 * @version 0.0.1
 * 
 */
public class Listener extends Thread
{
	SimpleChat sc;

	public Listener()
	{
		// Create a new thread
		super("Listener Thread");
		this.sc = SimpleChat.getInstance();
		// Start the thread
		start();
	}

	@SuppressWarnings("resource")
	public void run()
	{
		try
		{
			ServerSocket listener = new ServerSocket(sc.getPort());
			
			while (!sc.isConnected())
			{
				Socket s = listener.accept();
				sc.setConnection(s);
				sc.chatLogWrite("Connected to: " + s.getInetAddress().getHostAddress());
			}
		}
		catch (BindException e)
		{
			//No more BindException C:
			e.printStackTrace();
			System.err.println("I'm using port number:" + sc.getPort());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
