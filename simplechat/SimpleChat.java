package simplechat;

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
	
	private String senderNickname;
	private String addresseeNickname;
	private Socket connection;
	/**
	 * Constructor, made with a String Nickname.
	 * @param String nickname
	 */
	SimpleChat(String nickname)
	{
		this.senderNickname = nickname;
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
	 * Set the AddresseeNickname
	 * @param nickname
	 */
	public void setAddresseeNickname(String nickname)
    {
		addresseeNickname = nickname;
    }
	/**
	 * Get the Addressee Nickname.
	 * @return addresseeNickname
	 */
	public String getAddresseeNickname()
    {
		return addresseeNickname;
    }
	/**
	 * Get the nickname
	 * @return String nickname
	 */
	public String getSenderNickname() 
	{
		return this.senderNickname;
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
				System.out.println("Chat ended!");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
	
	@SuppressWarnings({ "resource", "unused" })
    public static void main(String[] args) throws IOException
	{
		  Scanner s = new Scanner(System.in);
		  System.out.println("Welcome to SChat. \nGive your IP to your mate \nAnd start chatting!");
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
	      /*
	       * Username Setup:
	       */
	      stream.writeBytes(sc.getSenderNickname() + "\n");
	      
	      /*
	       * Chat Setup:
	       */
	      while(true)
	      {
	    	  
	    	  send = s.nextLine();
		      stream.writeBytes(send + "\n");
		      if(send.equals("-exit"))
		      {
		    	  sc.closeConnection(); 
		    	  break;
		      }
	      }
	}




}
