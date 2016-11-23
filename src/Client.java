import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;

	public class Client {
		private String serverName;
		private int portNumber;
		private Socket sock = null;
		private DataInputStream inStream = null;
		private DataOutputStream outStream = null;
		private Key ClientCentralKey;
		
	public Client(String serverName, int portNumber) {
			super();
			this.serverName = serverName;
			this.portNumber = portNumber;
			ClientCentralKey = getKey();
		}
		

	private Key getKey() {
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\TOPSECRET.txt")))
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	private void displayInterface() {
		Scanner reader = new Scanner(System.in);
		String request;
		String reply;
		System.out.println("Welcome to Needham-Schroeder Protocol simulator, new user? 1 if so 0 if not");
			int usertype = reader.nextInt();
		if(usertype == 1){
			System.out.print("----Create New Account----\nEnter a username: ");
			String username = reader.nextLine();
			System.out.print("Enter a password: ");
			String password = reader.nextLine();
			request = "1 @" + username + "&" + password;
			send(request);
			receive();
		}
		else{
			System.out.print("----Account Login----\nEnter username: ");
			String username = reader.nextLine();
			System.out.print("Enter password: ");
			String password = reader.nextLine();
			request = "0 @" + username + "&" + password;
			send(request);
			if(!receive().contains(":^)")){
				System.out.println("Invalid user...Exiting");
				System.exit(0);
			}
			
			System.out.println("Enter option name: \n"
					+ "NSC: New secure connection \n"
					+ "LCSC: List current secure connections\n"
					+ "SOSC: Send over secure connection(Have Connection ID Ready");
			request = reader.nextLine();
			switch(request){
			case "NSC":
				System.out.println("");
				break;
			case "LCSC":
				
				break;
			case "SOSC":
				
				break;
			default:
				System.out.println("Failed to provide a correct option");
			
			}
			
		}
	}

	private void send(String request) {
		try{
			outStream.writeUTF(request);
		}catch(IOException exception){
			System.out.println("Failed to send request" + exception);
		}
	}

	private String receive() {
		String reply = null;
		try{
			reply = inStream.readUTF();
		}catch(IOException exception){
			System.out.println("Failed to receive reply" + exception);
		}
		return reply;
	}

	public void openBuffers() throws IOException{
		inStream = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		outStream = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
	}
	
	public static void main(String args[]){
		String serverName = args[0];
	    int port = Integer.parseInt(args[1]);
	    try {
	    	Client newClient = new Client(serverName, port);
	    	newClient.openBuffers();
	    	newClient.displayInterface();
	    }catch(IOException e) {
	    	e.printStackTrace();
	     }
	}
	
}
