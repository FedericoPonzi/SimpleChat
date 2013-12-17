package connection;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;

import org.xml.sax.SAXException;

import connection.WeUpnp.GatewayDevice;
import connection.WeUpnp.PortMappingEntry;


/**
 * This class search for a free port on the local pc. Then, ask for the gateway
 * device if there are any ips mapping with that port. Once found a port, it
 * will save in portSelected and it will be available from getPortSelected();
 * 
 * @author Federico
 * 
 */
public class PortSelector
{
	//Dynamic ports range:
	private static final int MIN_PORT_NUMBER = 49152;
	private static final int MAX_PORT_NUMBER = 65535;
	int portSelected;
	GatewayDevice gateway;
	public PortSelector(GatewayDevice gateway)
	{
		this.gateway = gateway;
		portSelected();
	}
	private void portSelected()
	{
			//Pick a random int between 49152 and 65535.
			portSelected = new Random().nextInt((MAX_PORT_NUMBER - MIN_PORT_NUMBER) + 1) + MIN_PORT_NUMBER;
			//If local pc's port and UPnP Port entry are not available, then go recursive
			if(!available(portSelected) && !(UPnPAvailable(portSelected))) portSelected();
	}
	/**
	 * Nice implementation from Apache camel
	 * @param port
	 * @return
	 */
	private boolean available(int port)
	{
		if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER)
		{
			throw new IllegalArgumentException("Invalid start port: " + port);
		}
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try
		{
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		}
		catch (IOException e)
		{
		}
		finally
		{
			if (ds != null)
			{
				ds.close();
			}

			if (ss != null)
			{
				try
				{
					ss.close();
				}
				catch (IOException e)
				{
					/* should not be thrown */
				}
			}
		}

		return false;
	}
	
	private boolean UPnPAvailable(int port)
	{
		PortMappingEntry portMapping = new PortMappingEntry();
		try
        {
	        return gateway.getSpecificPortMappingEntry(port ,"TCP",portMapping);
        }
        catch (IOException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        catch (SAXException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return false;
	}
	public int getPortSelected()
	{
		return portSelected;
	}
	
}
