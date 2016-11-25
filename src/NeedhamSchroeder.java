import java.net.Socket;

public class NeedhamSchroeder {
	
	public NeedhamSchroeder(int type) {
		super();
		this.type = type;
	}

	private int type;

	public NeScInfo stage1(int nonse, String decryptedReply) {
		if(type == 0){
			NeScInfo info = new NeScInfo();
			info.setSource(decryptedReply.substring(0,decryptedReply.indexOf('%') -1));
			info.setTarget(decryptedReply.substring(decryptedReply.indexOf('%'),decryptedReply.indexOf('#') -1));
			info.setNonse(
					Integer.parseInt(decryptedReply.substring(decryptedReply.indexOf('#'), decryptedReply.indexOf('|') - 1))); 
			info.setKey(decryptedReply.substring(decryptedReply.indexOf('|'), decryptedReply.indexOf('~') - 1));
			info.setTargetData(decryptedReply.substring(decryptedReply.indexOf('~'), decryptedReply.length() - 1));
		}
		if(type == 1){
			
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
