package socketConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SCServer extends Thread
{
	String clientSentence;
	String capitalizedSentence;
	int porta;
	ServerSocket serverSocket;
	
	public SCServer(int porta) throws UnknownHostException, IOException
	{
	      // Create a new, second thread
	      super("Server Thread");
	      this.porta = porta;
	      start(); // Start the thread
	      
	}
	
	public void run(){
	try {
		serverSocket = new ServerSocket(porta);
		Socket connectionSocket = serverSocket.accept();
		Scanner inFromClient = new Scanner(connectionSocket.getInputStream());	
		//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			while (true) 
			{
				clientSentence = inFromClient.nextLine();
				System.out.println("- " + clientSentence);
				//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				//outToClient.writeBytes(inFromUser.readLine() + "\n");
				if (clientSentence.equals("STOP")) break;
			}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		new SCServer(4444);
	}
}
