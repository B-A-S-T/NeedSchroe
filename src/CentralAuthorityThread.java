import java.net.*;
import java.io.*;

public class CentralAuthorityThread extends Thread{

	private int ID = -1;
	private Socket sock = null;
	private CentralAuthority centralServ = null;
	private String request = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	
	public CentralAuthorityThread(CentralAuthority centralAuthority, Socket accept) {
		super();
		this.centralServ = centralAuthority;
		this.sock = accept;
		ID = sock.getPort();
	}
	public int getID(){
		return this.ID;
	}
	public String getIP(){
		return "" + sock.getInetAddress();
	}
	public void run(){
		boolean sessionFinished = false;
		while(!sessionFinished){
			System.out.println("Hey there in run! : qweqwe");
			try{
				request = in.readUTF();
				System.out.println("Hey there in run! : " + request);
				centralServ.processRequest(ID, request);	
			}catch(IOException exception){
				System.out.println("Failed to receive" + exception);
				centralServ.removeClient(ID);
				stop();
			}
		}
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
	
	public void send(String string) {
		// TODO Auto-generated method stub
		
	}


}
