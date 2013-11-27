package simplechat;

import gui.Gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import connection.Listener;
import connection.Receiver;

/**
 * This is the main Class for the SimpleChat project.
 * 
 * @version 0.0.2
 */
public class SimpleChat
{
	// Port used from SimpleChat:
	private final int port = 4455;
	private String senderNickname;
	private String addresseeNickname;
	private Socket connection;
	private Gui gui;

	/**
	 * Constructor, made with a String Nickname.
	 * 
	 * @param String
	 *            nickname
	 */
	SimpleChat(String nickname)
	{
		this.senderNickname = nickname;
		this.gui = new Gui(nickname, this);
		new Listener(this);
	}

	/**
	 * Method to connect to an Host.
	 * 
	 * @param socket
	 */

	public void connect(String host)
	{
		try
		{
			if (host.length() > 4) setConnection(new Socket(host, port));
		}
		catch (UnknownHostException e)
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
	 * 
	 * @param Socket
	 *            s
	 */
	public void setConnection(Socket s)
	{
		this.connection = s;
		sendMessage(getSenderNickname() + "\n");
		chatLogWrite("Connection:" + isConnected());
		new Receiver(this);
		gui.setInputTextEditable(true);
	}

	/**
	 * Set the AddresseeNickname
	 * 
	 * @param nickName
	 */
	public void setAddresseeNickname(String nickName)
	{
		addresseeNickname = nickName;
		gui.addNickname(nickName);
	}

	/**
	 * Get the Addressee Nickname.
	 * 
	 * @return addresseeNickname
	 */
	public String getAddresseeNickname()
	{
		return addresseeNickname;
	}

	/**
	 * Get the nickname
	 * 
	 * @return String nickname
	 */
	public String getSenderNickname()
	{
		return this.senderNickname;
	}

	/**
	 * Return output stream
	 * 
	 * @return
	 * @throws IOException
	 */
	private OutputStream getOutputStream() throws IOException
	{
		return connection.getOutputStream();
	}

	/**
	 * Get the Port Number
	 * 
	 * @return int Port
	 */
	public int getPort()
	{
		return this.port;
	}

	/**
	 * Check if the socket is connected
	 * 
	 * @return boolean
	 */
	public boolean isConnected()
	{
		if (connection != null)
			return connection.isConnected();
		else
			return false;
	}

	public void chatLogWrite(String message)
	{
		gui.chatLogWrite(message);
	}

	/**
	 * Return the inputStream of the socket.
	 * 
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

	public void sendMessage(String message)
	{
		try
        {
	        DataOutputStream output = new DataOutputStream(getOutputStream());
			output.writeBytes(message + "\n");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		String nickName = Gui.getNickName();
		SimpleChat sc = new SimpleChat(nickName);
		sc.chatLogWrite("Welcome to SChat. \nGive your IP to your mate \nAnd start chatting!");
		sc.chatLogWrite("Click on File -> Connect, and Insert the host");
		sc.chatLogWrite("Or wait for a connection!");
	}
}
