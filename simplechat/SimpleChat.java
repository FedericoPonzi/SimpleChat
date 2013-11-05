package simplechat;
//Flagdizero was here
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import connection.Listener;
import connection.Receiver;
/**
 * This is the main Class for the SimpleChat project.
 * @version 0.0.1
 */
public class SimpleChat 
{	
	//Port used from SimpleChat:
	private final int port = 4455;
	/**For debugging purpose:
	 */
	//private int port = (int) ( 4454 + (Math.random() * 100));
	
	private String nickname;
	private Socket connection;
	/**
	 * Constructor, made with a String Nickname.
	 * @param String nickname
	 */
	SimpleChat(String nickname)
	{
		this.nickname = nickname;
		System.out.print(port);
		new Listener(this);
	}
	
	/**
	 * Method to connect to an Host.
	 * @param socket
	 */
	private void connect(String host)
	{
		try 
		{
			if(host.length()>4) setConnection(new Socket(host, port));
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	/**
	 * Set the connection.
	 * @param Socket s
	 */
	public void setConnection(Socket s)
	{
		this.connection = s;
	}
	/**
	 * Get the nickname
	 * @return String nickname
	 */
	public String getNickname() 
	{
		return this.nickname;
	}
	/**
	 * Return output stream
	 * @return
	 * @throws IOException
	 */
	private OutputStream getOutputStream() throws IOException 
	{
		return connection.getOutputStream();
	}

	/**
	 * Get the Port Number
	 * @return int Port
	 */
	public int getPort() 
	{
		return this.port;
	}
	/**
	 * Check if the socket is connected
	 * @return boolean
	 */
	public boolean isConnected() 
	{
		if(connection != null) return connection.isConnected();
		else return false;
	}
	/**
	 * Return the inputStream of the socket.
	 * @return InputStream Socket's InputStream
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException 
	{
		return connection.getInputStream();
	}
	/**
	 * When called this, the connection will be closed.
	 */
	public void closeConnection() 
	{
			try 
			{
				connection.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws IOException
	{
		  Scanner s = new Scanner(System.in);
		  System.out.println("Welcome to SChat. \n Give your IP and Portnumber to your mate \n And start chatting!");
	      System.out.println("Insert your nickname:");
	      SimpleChat sc = new SimpleChat(s.nextLine());
	      while(!sc.isConnected())
	      {
	    	  System.out.println("Insert the host or wait for a connection:");
	    	  sc.connect(s.nextLine());
	      }
	      System.out.println("Connessione:" + sc.isConnected());
	      //If i come there, then my pipe is setted and i can start exchange messages:
	      Receiver r = new Receiver(sc);
	      //OutputStream:
	      DataOutputStream stream = new DataOutputStream(sc.getOutputStream());
	      String send = "";
	      while(true)
	      {
	    	  
	    	  send = s.nextLine();
		      stream.writeBytes(sc.getNickname() + ": " + send + "\n");
		      if(send.equals("-exit")) break;
	      }
	}
}
