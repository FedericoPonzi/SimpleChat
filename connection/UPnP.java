package connection;

import java.net.InetAddress;
import java.util.Map;

import connection.holepunching.GatewayDevice;
import connection.holepunching.GatewayDiscover;
import connection.holepunching.PortMappingEntry;

public class UPnP
{

	private static int SAMPLE_PORT = 6991;
	private static short WAIT_TIME = 10;
	private static boolean LIST_ALL_MAPPINGS = false;

	public static void main(String[] args) throws Exception{
		//Find the Gateway:
		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();
		if (gateways.isEmpty()) 
		{
			//You are not connected to the internet. err
		}
		//Select the first good gateway:
		GatewayDevice activeGW = gatewayDiscover.getValidGateway();
		if (null == activeGW) 
		{
			//No active gateway device found. Error :C
		}
		// testing PortMappingNumberOfEntries
		Integer portMapCount = activeGW.getPortMappingNumberOfEntries();
		PortMappingEntry portMapping = new PortMappingEntry();
		//Local Address:
		InetAddress localAddress = activeGW.getLocalAddress();
		//External Address:
		String externalIPAddress = activeGW.getExternalIPAddress();

		//Check if the port is already in use:
		if (!activeGW.getSpecificPortMappingEntry(SAMPLE_PORT,"TCP",portMapping)) {
			//If not, 
			// test static lease duration mapping
			if (activeGW.addPortMapping(SAMPLE_PORT,SAMPLE_PORT,localAddress.getHostAddress(),"TCP","test")) {
				//Port mapping added.
			}
		} 
		//Remove mapping:		activeGW.deletePortMapping(SAMPLE_PORT,"TCP")
		
	}
}
