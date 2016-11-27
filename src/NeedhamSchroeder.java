import java.net.Socket;
import java.util.Random;

public class NeedhamSchroeder {
	
	public NeedhamSchroeder(int type) {
		super();
		this.type = type;
	}

	private int type;

	public NeScInfo stage1(int nonse, String decryptedReply, Encryption CS, Encryption TS) {
		NeScInfo info = new NeScInfo();
		//Client side stage one
		if(type == 0){
			info.setSource(decryptedReply.substring(1,decryptedReply.indexOf('%')));
			info.setTarget(decryptedReply.substring(decryptedReply.indexOf('%') + 1,decryptedReply.indexOf('#')));
			info.setNonse(
					Integer.parseInt(decryptedReply.substring(decryptedReply.indexOf('#') + 1, decryptedReply.indexOf('|')))); 
			info.setKey(decryptedReply.substring(decryptedReply.indexOf('|') + 1, decryptedReply.indexOf('~')));
			info.setTargetData(decryptedReply.substring(decryptedReply.indexOf('~') + 1, decryptedReply.length()));
			System.out.println(info.getSource() + " " + info.getTarget() + " " 
			+ info.getNonse()+ " " + info.getKey() + " " + info.getTargetData());
		} 
		//Server Stage one
		if(type == 1){
			System.out.println(CS.getKey() + "   " + TS.getKey());
			info.setSource(decryptedReply.substring(decryptedReply.indexOf('@') + 1, decryptedReply.indexOf('%')));
			info.setTarget(decryptedReply.substring(decryptedReply.indexOf('%') + 1, decryptedReply.indexOf('#')));
			info.setNonse(Integer.parseInt(decryptedReply.substring(decryptedReply.indexOf('#') + 1, decryptedReply.length())));
			info.setKey(generateKey());
			//Encrypt the with the Target's Client/Server Key
			info.setTargetData(TS.encrypt("@"+info.getSource() + "|" + info.getKey()));
			String reply = "@" + info.getSource() + "%" + info.getTarget() + "#" + info.getNonse() + "|" + info.getKey() + TS.decrypt(info.getTargetData());
			System.out.println("Unencrypted: " + reply);
			reply = "@" + info.getSource() + "%" + info.getTarget() + "#" + info.getNonse() + "|" + info.getKey() + "~" + info.getTargetData();
			//Encrypt with the Cient/Server Key
			info.setServerPacket(CS.encrypt(reply));
		}
		return info;
	}

	public NeScInfo stage2(String request, Encryption encryption) {
		System.out.println(request + "\n\n");
		String unencrypted = encryption.decrypt(request.substring(5, request.length()));
		System.out.println(unencrypted + "\n\n" + unencrypted);
		NeScInfo info = new NeScInfo();
		info.setSource(unencrypted.substring(unencrypted.indexOf('@') + 1, unencrypted.indexOf('|')));
		info.setKey(unencrypted.substring(unencrypted.indexOf('|') + 1, unencrypted.length()));
		Encryption newEncryption = new Encryption(info.getKey());
		info.setNonse(generateNonse());
		info.setServerPacket(newEncryption.encrypt("" + info.getNonse()));
		
		return info;
	}
	public String stage3(String key, String request) {
		Encryption encryption = new Encryption(key);
		String unencrypted = encryption.decrypt(request.substring(4, request.length()));
		int newNonse = (Integer.parseInt(unencrypted)) - 1;
		return encryption.encrypt("" + newNonse);
	}
	
	public boolean stage4(String key, String request, int nonse) {
		Encryption encryption = new Encryption(key);
		String unencrypted = encryption.decrypt(request.substring(4, request.length()));
		if((nonse - 1) == Integer.parseInt(unencrypted)){
			return true;
		}
		return false;
	}
	
	private int generateNonse() {
		Random rand = new Random();
		int l = 1;
		int h = 10000;
		return (rand.nextInt(h-l) + l);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	private String generateKey(){
		Random rand = new Random();
		String alphabet = "abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQURSTVWXYZ0123456789";
		int length = alphabet.length();
		String newKey = new String();
		for(int i = 0; i < 5; i++){
			newKey += "" + alphabet.charAt(rand.nextInt(length));
		}
		return newKey;
	}
}
