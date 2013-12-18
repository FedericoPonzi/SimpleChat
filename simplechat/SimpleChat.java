package simplechat;

import gui.Gui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import org.xml.sax.SAXException;

import connection.Listener;
import connection.PortSelector;
import connection.Receiver;
import connection.WeUpnp.GatewayDevice;
import connection.WeUpnp.GatewayDiscover;
/**
 * This is the main Class for the SimpleChat project.
 * 
 * @version 0.0.2
 */
public class SimpleChat
{
	// Singleton:
	private static SimpleChat sc;
	// Port used from SimpleChat:
	private int port;
	private String senderNickname;
	private String addresseeNickname;
	private Socket connection;
	private GatewayDevice gateway;
	private Gui gui;
	private String gwIp;

	/**
	 * Constructor, made with a String Nickname.
	 * 
	 * @param String
	 *            nickname
	 */
	SimpleChat(String nickname)
	{
		// Singleton:
		sc = this;
		this.senderNickname = nickname;
		// Get all the Gateways.

		try
		{
			GatewayDiscover gatewayDiscover = new GatewayDiscover();
			Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();
			if (gateways.isEmpty())
			{
				System.err.println("Seems like you are not connected to the internet");
			}
			// Select the first good gateway:
			gateway = gatewayDiscover.getValidGateway();

			if (null == gateway)
			{
				System.err.println("No active gateway device found.");
				// No active gateway device found. Error :C
			}
			setGwIp(gateway.getExternalIPAddress());
			setPort(new PortSelector(gateway).getPortSelected());
			gateway.addPortMapping(port, port, gateway.getLocalAddress()
			        .getHostAddress(), "TCP", "SimpleChat");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// If i come here, i've found a free port both on the local pc and on
		// the Gateway Device
		this.gui = new Gui(nickname);
		createListener();

	}

	public static SimpleChat getInstance()
	{
		return sc;
	}

	/**
	 * Method to connect to an Host.
	 * @param port 
	 * 
	 * @param socket
	 */

	public void connect(String host, int port)
	{
		try
		{
			if (host.length() > 4) setConnection(new Socket(host, port));
		}
		catch (UnknownHostException e)
		{
			chatLogWrite("Impossible to find your mate's host :C. Check twice his Ip :) ");
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
		this.connection = s; // Set the connection
		sendSCMessage(getSenderNickname() + "\n"); // Send your nickname
		chatLogWrite("Connection:" + isConnected()); // Print that is connected
		new Receiver(); // Set up the Receiver
		gui.setInputTextEditable(true); // Set the input text field of the GUI
		                                // editable
		gui.setDisconnectButtonEnabled(true); // Set the File->Disconnect button
		                                      // Clickable
	}

	/**
	 * Set the AddresseeNickname
	 * 
	 * @param nickName
	 */
	public void setAddresseeNickname(String nickName)
	{
		addresseeNickname = nickName; // Set your mate's nickname.
		gui.addNickname(nickName); // And add it on the GUI.
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
		return connection == null ? false : true;
	}

	/**
	 * Write in the Chat Log JPane of the GUI.
	 * 
	 * @param message
	 */
	public void chatLogWrite(String message)
	{
		if (message.equals("01-CLOSE_REQ")) closeConnection();
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
		if (isConnected())
		{
			try
			{
				getOutputStream().flush();
				connection.close();
				gui.removeNickName(getAddresseeNickname());
				createListener();
				gui.setDisconnectButtonEnabled(false);
				chatLogWrite("--- Connection has been closed. ---");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Same {@link #sendChatMessage(String)} but is used to send a SimpleChat
	 * configuration message. It will not be printed on the screen. UTF-8
	 * Encoded.
	 * 
	 * @param message
	 */
	public void sendSCMessage(String message)
	{
		try
		{
			message = new String(message.getBytes("UTF8"), "UTF8");
			DataOutputStream output = new DataOutputStream(getOutputStream());
			output.writeBytes(message + "\n");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Used to send a Chat Message. UTF-8 encoded, and message will be printed
	 * in the GUI's chatLogPane textarea.
	 * 
	 * @param message
	 */
	public void sendChatMessage(String message)
	{
		try
		{
			byte[] uft8byte = message.getBytes("UTF8");
			chatLogWrite(getSenderNickname() + ": "
			        + new String(uft8byte, "UTF8"));
			message = new String(uft8byte, "UTF8");
			DataOutputStream output = new DataOutputStream(getOutputStream());
			output.writeBytes(message + "\n");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void createListener()
	{
		new Listener();
	}

	public static void main(String[] args) throws IOException
	{
		SimpleChat sc = new SimpleChat(Gui.getNickName());
		sc.chatLogWrite("Welcome to SChat. \nGive your IP to your mate \nAnd start chatting!");
		sc.chatLogWrite("Click on File -> Connect, and Insert the host");
		sc.chatLogWrite("Or wait for a connection!");
		sc.chatLogWrite("Your IP:" + sc.getGwIp() + "; Your Port:"+ sc.getPort());
	}

	public void setGwIp(String ip)
	{
		gwIp = ip;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getGwIp()
	{
		return gwIp;
	}

	public void removeUPnPMapping()
	{
		try
		{
			gateway.deletePortMapping(port, "TCP");
		}
		catch (IOException | SAXException e)
		{
			System.err.println("Delet Port Mapping exception.");
			e.printStackTrace();
		}
	}
}
