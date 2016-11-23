import java.net.*;
import java.security.*;

import javax.crypto.spec.SecretKeySpec;

import java.io.*;



public class CentralAuthority implements Runnable{

	private Thread thread = null;
	private String threadName = null;
	private CentralAuthorityThread newClients[] = new CentralAuthorityThread[20];
	private int numClients;
	private ServerSocket serverSocket = null;

	public CentralAuthority(String threadName, int port) throws Exception{
		this.threadName = threadName;
		numClients = 0;
		try{
			Key key = generateKeyFromString("hey");
			serverSocket = new ServerSocket(port);
			startServerThread();
			System.out.println();
		} catch(IOException exception){
			System.out.println(exception);
		}
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
			if(newClients[i].getId() == id)
				return i;
		}
		return -1;
	}


	public synchronized void processRequest(int id, String request) {
		if(request == "quit"){
			newClients[getClientById(id)].send("Thank you.");
			removeClient(id);
		}
		
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
