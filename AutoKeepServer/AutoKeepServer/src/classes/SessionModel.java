package classes;

public class SessionModel {
	int sessionID;
	String connectedUser;
	String connectionTime;
	String disconnectionTime;
	
	public SessionModel(String connectedUser) {
		super();
		this.connectedUser = connectedUser;
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
}
