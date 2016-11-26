import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientCommunicationThread extends Thread{
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private Socket sock = null;
	private ClientPassive passive = null;
	private int id;
	private String username;
	private int option;
	private ChatGUI gui = null;
	private String targetData;

	public void run(){
		boolean sessionFinished = false;
		String request = null;
		if(option == 1){
			request = "REQ0 " + targetData;
			gui.send(request);
		}
		System.out.println(request + "\n\n");
		while(!sessionFinished){
			try{
				request = in.readUTF();
				if(request.contains("REQ")){
					passive.verify(id, request);
				}
				gui.appendMessage(request);	
			}catch(IOException exception){
				System.out.println("Failed to receive" + exception);
				passive.removeClient(id);
				stop();
			}
		}
	}
	public int getID(){
		return id;
	}
	public ClientCommunicationThread(ClientPassive clientPassive, Socket sock, int option, String targetData) {
		id = sock.getPort();
		passive = clientPassive;
		this.sock = sock;
		gui = new ChatGUI(this, sock);
		this.option = option;
		this.targetData = targetData;
	}

	public void openBuffer() throws IOException {
		in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
	}
	public void close() throws IOException {
		if(sock != null){ sock.close();}
		if(in != null){in.close();}
		if(out != null){out.close();}
	}
	public void remove(){
		passive.removeClient(id);
	}

}
