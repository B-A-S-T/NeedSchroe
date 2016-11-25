import java.net.*;
import java.security.*;

import javax.crypto.spec.SecretKeySpec;

import com.sun.javafx.geom.Line2D;

import java.io.*;



public class CentralAuthority implements Runnable{

	private Thread thread = null;
	private String threadName = null;
	private CentralAuthorityThread newClients[] = new CentralAuthorityThread[20];
	private int numClients;
	private ServerSocket serverSocket = null;
	private NeedhamSchroeder ns;
	private String userKeys[][];
	private int userCount;
	private Encryption crypt = null;

	public CentralAuthority(String threadName, int port) throws Exception{
		userCount = 0;
		userKeys = readKeys();
		this.threadName = threadName;
		ns = new NeedhamSchroeder(1);
		numClients = 0;
		try{
			serverSocket = new ServerSocket(port);
			startServerThread();
			System.out.println();
		} catch(IOException exception){
			System.out.println(exception);
		}
	}
	
	private String[][] readKeys() {
		String [][]userKeys = new String[20][2];
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("userKeys.txt"));
			line = reader.readLine();
			while(line != null){
				userKeys[userCount][0] = line.substring(0, line.indexOf(' '));
				userKeys[userCount][1] = line.substring(line.indexOf(' '), line.length());
				System.out.println("User: " + userKeys[userCount][0] + " Key: " + userKeys[userCount][1]);
				userCount++;
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userKeys;
	}

	public void run() {
		while(thread != null){
			try{
				System.out.println("Waiting for clients to connect ...");
				newClientThread(serverSocket.accept());
			}
			catch(IOException exception){
				System.out.println("Failed to accept" + exception);
			}
		}
		
	}
	
	private void newClientThread(Socket accept) {
		System.out.println("New client" + accept);
		newClients[numClients] = new CentralAuthorityThread(this, accept);
		try{
			newClients[numClients].openBuffer();
			System.out.println("Starting new Thread!\n\n");
			newClients[numClients].start();
			numClients++;
		} catch(IOException exception)
			{ System.out.println("Failed to open buffer" + exception);}
	}

	public void startServerThread(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stopServerThread(){
		if(thread != null){
			thread.stop();
			thread = null;
		}
	}
	
	public static void main(String args[]) throws Exception{
		CentralAuthority centralServer = null;
		if(args.length != 1)
			System.out.println("Failed to provide port number.\n "
					+ "Try cmd: java CentralAuthority portNumber");
		else{
			try {
				centralServer = new CentralAuthority("Main Server", Integer.parseInt(args[0]));
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getClientById(int id){
		for (int i = 0; i < numClients; i++){
			if(newClients[i].getID() == id){
				return i;
			}
		}
		return -1;
	}


	public synchronized void processRequest(int id, String request) {
		String IP = newClients[getClientById(id)].getIP();
		System.out.println("Hello there!" + IP);
		if(request == "quit"){
			newClients[getClientById(id)].send("Thank you.");
			removeClient(id);
		}
		if(request.indexOf('0') == 0){
			String username = request.substring(3, request.length());
			newClients[getClientById(id)].setUsername(username);
		}
		if(request.contains("NSC ")){
			String sourceKey = getKeyByUser(request.substring(request.indexOf('@') + 1, request.indexOf('%')));
			String targetKey = getKeyByUser(request.substring(request.indexOf('%') + 1, request.indexOf('#')));
			NeScInfo info = ns.stage1(0, request, new Encryption(sourceKey), new Encryption(targetKey));
			System.out.println(info.getServerPacket());
			newClients[getClientById(id)].send(info.getServerPacket());
		}
		if(request.contains("IP ")){
			String target = request.substring(request.indexOf('%') + 1, request.length());
			newClients[getClientById(id)].send(getIpByUser(target));
		}
	}

	
	private String getIpByUser(String target){
		for(int i = 0; i < numClients; i++){
			if(newClients[i].getUsername().contains(target)){
				return (newClients[i].getIP());
			}
		}
		return null;
	}
	
	private String getKeyByUser(String substring) {
		for(int i = 0; i < userCount; i ++){
			if(userKeys[i][0].contains(substring)){
				return userKeys[i][1].trim();
			}
		}
		return null;
	}

	synchronized void removeClient(int id) {
		int clientId = getClientById(id);
		if(clientId >=0){
			CentralAuthorityThread toRemove = newClients[clientId];
			if(clientId < numClients-1){
				for(int i = clientId + 1; i < numClients; i++){
					newClients[i-1] = newClients[i];
				}
			numClients--;
			}
		
			try{
				toRemove.close();
			} catch(IOException exception){
				System.out.println("Failed to close" + exception);
			}
				toRemove.stop();	
		}
	}
}
