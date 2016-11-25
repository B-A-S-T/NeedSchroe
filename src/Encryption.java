
public class Encryption {
	String key;

	public Encryption(String key) {
			this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key; 
	}

	public String encrypt(String request) {
		char encrypted[] = request.toCharArray();
		for(int i = 0; i < request.length(); i++){
			encrypted[i] = (char) ((key.charAt(i % key.length())) ^ encrypted[i]);
		}
		return new String(encrypted);
	}

	public String decrypt(String reply) {
		return encrypt(reply);
	}

}
