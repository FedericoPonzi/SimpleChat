import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import socketConnection.SCClient;
import socketConnection.SCServer;
public class SimpleChat {
   public static void main(String args[]) throws UnknownHostException, IOException {
	  int portacaso = (int) (4000+ Math.random() *10);
      Scanner s = new Scanner(System.in);
      
	  System.out.println(portacaso);
	  SCServer server = new SCServer(portacaso);
	  
      System.out.println("Inserisci l' host:");
      String host = s.nextLine();
      System.out.println("Inserisci la porta:");
      int porta = s.nextInt();
      SCClient client = new SCClient(host,porta);
      }
}