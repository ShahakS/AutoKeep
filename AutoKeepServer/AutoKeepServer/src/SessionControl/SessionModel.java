package SessionControl;

public class SessionModel {
	private int sessionID;
	private String connectedUser;
	private String connectionTime;
	private String disconnectionTime;
	private String ipAddress;
	
	public SessionModel(String connectedUser, String connectionTime,String ipAddress) {
		super();
		this.ipAddress = ipAddress;
		this.connectedUser = connectedUser;
		this.connectionTime = connectionTime;
	}
	
	public SessionModel(int sessionID, String connectedUser, String connectionTime, String disconnectionTime) {
		super();
		this.sessionID = sessionID;
		this.connectedUser = connectedUser;
		this.connectionTime = connectionTime;
		this.disconnectionTime = disconnectionTime;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public String getConnectionTime() {
		return connectionTime;
	}

	public void setConnectionTime(String connectionTime) {
		this.connectionTime = connectionTime;
	}

	public String getDisconnectionTime() {
		return disconnectionTime;
	}

	public void setDisconnectionTime(String disconnectionTime) {
		this.disconnectionTime = disconnectionTime;
	}

	public String getConnectedUser() {
		return connectedUser;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
