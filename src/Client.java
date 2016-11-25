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
		private Encryption crypt = null;
		private NeedhamSchroeder nsProto = null;
		
	public Client(String serverName, int portNumber) {
			super();
			char [] a = new char[50];
			this.serverName = serverName;
			this.portNumber = portNumber;
				try {
					FileReader fileReader = new FileReader("TOPSECRET.txt");
					try {
						fileReader.read(a);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			crypt = new Encryption(new String(a).trim());
			nsProto = new NeedhamSchroeder(0);
			try {
				sock = new Socket(this.serverName, this.portNumber);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	private void displayInterface() {
		Scanner reader = new Scanner(System.in);
		String request;
		String reply;
		System.out.println("Welcome to Needham-Schroeder Protocol simulator");
		System.out.print("----Account Login----\nEnter username: ");
		String username = reader.nextLine();
		request = "0 @" + username;
		// Sends to server, writes name and IP to file
		System.out.println("Just sent!\n\n");
		send(request);
		// Get option
		System.out.println("Enter option name: \n"
			+ "NSC: New secure connection \n"
			+ "LCSC: List current secure connections\n"
			+ "SOSC: Send over secure connection(Have Connection ID Ready");
		request = reader.nextLine();
		switch(request){
			case "NSC":
				NSC();
				break;
			case "LCSC":
				LCSC();
				break;
			case "SOSC":
				SOSC();
				break;
			default:
				System.out.println("Failed to provide a correct option");
			}
			
		}
	
	private void SOSC() {
		// TODO Auto-generated method stub
		
	}


	private void LCSC() {
		// TODO Auto-generated method stub
		
	}


	private void NSC() {
		Scanner reader = new Scanner(System.in);
		String source, target;
		int nonse;
		System.out.println("Creating new secure connection: \n"
				+ "Enter your username, who you wish to talk to"
				+ ", and your nonse.");
		source = reader.nextLine();
		target = reader.nextLine();
		nonse = reader.nextInt();
		String request = "NSC @" + source + "%" + target + "#" + nonse;
		System.out.println("Unencrypted: " + request);
		send(request);
		String reply = receive();
		String decryptedReply = crypt.decrypt(reply);
		System.out.println("Unencrypted: " + decryptedReply);
		NeScInfo info = nsProto.stage1(nonse, decryptedReply, crypt, null);
		
		if(info == null){
			System.out.println("Nonse did not match, could be an attack.");
			System.exit(0);
		}
	}
	

	private void send(String request) {
		try{
			outStream.writeUTF(request);
			outStream.flush();
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
