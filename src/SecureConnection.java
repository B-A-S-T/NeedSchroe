
public class SecureConnection {
	private String Target;
	private String IP;
	private String Key;
	private String TargetData;
	private String Port;
	
	public SecureConnection(String target, String iP, String key, String port, String TargetData) {
		super();
		Target = target;
		IP = iP;
		Key = key;
		Port = port;
		this.TargetData = TargetData;
	}
	

	public String getTarget() {
		return Target;
	}


	public void setTarget(String target) {
		Target = target;
	}


	public String getTargetData() {
		return TargetData;
	}


	public void setTargetData(String targetData) {
		TargetData = targetData;
	}


	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getPort() {
		return Port;
	}
	public void setPort(String port) {
		Port = port;
	}
}
