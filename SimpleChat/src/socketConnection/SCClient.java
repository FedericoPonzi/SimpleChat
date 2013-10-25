package socketConnection;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SCClient extends Thread
{
	String sentence;
	String modifiedSentence;
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	Socket clientSocket;
	String host;
	int porta;
	
	public SCClient(String host, int porta) throws UnknownHostException, IOException
	{
		super("Client Thread");
		clientSocket = new Socket(host, porta);
		start();
	}
	public void run()
	{
		DataOutputStream outToServer;
		try {
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			while (true)
			{
				sentence = inFromUser.readLine();
				outToServer.writeBytes(sentence + "\n");
				if (sentence.equals("STOP")) break;
			}
			clientSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
