import java.net.Socket;

public class NeedhamSchroeder {

	public NeScInfo stage1(int nonse, Socket sock, String decryptedReply) {
		NeScInfo info = new NeScInfo();
		int newNonse = 
				Integer.parseInt(decryptedReply.substring(decryptedReply.indexOf('#'), decryptedReply.length() - 5)); 
		                            
		if(newNonse != nonse){return null;}
		info.setKey(decryptedReply.subSequence(decryptedReply.indexOf('@'), decryptedReply.indexOf('%')));
		return null;
	}

}
