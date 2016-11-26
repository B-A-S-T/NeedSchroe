import java.io.IOException;
import java.net.*;

public class ClientPassive implements Runnable{
	private ServerSocket serverSock;
	private Thread thread;
	private String Port;
	private ClientCommunicationThread[] CCs = new ClientCommunicationThread[10];
	private int numClients;
	private String serverKey;
	private NeScInfo nescInfo = null;
	
	public ClientPassive(String port, String key) {
		super();
		numClients = 0;
		try {
			this.serverSock = new ServerSocket(Integer.parseInt(port));
			startClientPassive();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		Port = port;
		serverKey = key;
		
	}
	public synchronized void verify(int id, String request) {
		NeedhamSchroeder nesc = new NeedhamSchroeder(0);
		System.out.println(request);
		if(request.contains("REQ0")){
			nescInfo = nesc.stage2(request, new Encryption(serverKey));
			
		}
		else if(request.contains("REQ1")){
			boolean confirm = nesc.stage3(nescInfo, request);
			if(!confirm){
				System.out.println("Failed to verify Nonse");
				removeClient(id);
			}
		}
	}
	public void run() {
		while(thread != null){
			try{
				newCommunicationThread(serverSock.accept(), 0, null);
			}
			catch(IOException exception){
				System.out.println("Failed to accept" + exception);
			}
		}
		
	}
	private void startClientPassive(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	public void newCommunicationThread(Socket sock, int option, String targetData) {
		CCs[numClients] = new ClientCommunicationThread(this, sock, option, targetData);
		try{
			CCs[numClients].openBuffer();
			CCs[numClients].start();
			numClients++;
		} catch(IOException exception)
			{ System.out.println("Failed to open buffer" + exception);}
	}

	
	public void removeClient(int id) {
		int clientId = getClientById(id);
		if(clientId >=0){
			ClientCommunicationThread toRemove = CCs[clientId];
			if(clientId < numClients-1){
				for(int i = clientId + 1; i < numClients; i++){
					CCs[i-1] = CCs[i];
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

	private int getClientById(int id) {
		for (int i = 0; i < numClients; i++){
			if(CCs[i].getID() == id){
				return i;
			}
		}
		return -1;
	}

	public void processRequest(int id, String request) {
		// TODO Auto-generated method stub
		
	}
}
