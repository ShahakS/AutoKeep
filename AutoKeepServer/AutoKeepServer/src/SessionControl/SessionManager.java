package SessionControl;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import exceptionsPackage.ExcaptionHandler;

public class SessionManager {
	private static SessionManager sessionManager = new SessionManager();
	private ArrayList<SessionModel> sessions;
	private Map<String,String> blockedIps;
	public static final long BAN_DURATION = 60; 
	
	private SessionManager() {
		sessions = new ArrayList<>();
		blockedIps = new HashMap<>();
	}
	
	/**
	 * Clean old blocked ip addresses and return a reference to the sessionManager
	 * @return the SessionManager reference
	 */
	public synchronized static SessionManager startSession() {
		sessionManager.cleanOldSessions();
		return sessionManager;
	}
	
	/**
	 * Clean old blocked ip addresses that passed the BAN_DURATION time
	 */
	private void cleanOldSessions() {
		Iterator<Entry<String, String>> iterator = blockedIps.entrySet().iterator();
		
	    while (iterator.hasNext()) {
	        Map.Entry<String, String> ipDatePair = (Map.Entry<String, String>)iterator.next();
	        long timeDifference = sessionManager.getTimeDifference(ipDatePair.getValue().toString());
	        
			if (timeDifference >= BAN_DURATION)
				iterator.remove();
	    }
	}


	/**
	 * Get the current date and time in Sql Format
	 * @return the date and time String
	 */
	private String getCurrentTime() {
		Date javaDateTime = new Date();
		SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentSqlDateTime = sqlDateTimeFormat.format(javaDateTime);
		
		return currentSqlDateTime;
	}
	
	/**
	 * Get the difference time in seconds between the ban time and the current time 
	 * @param bannedTime - the ban date time in String
	 * @return the time difference in seconds
	 * @throws ParseException 
	 */
	private long getTimeDifference(String bannedTime){
		String currentTime = getCurrentTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long timeDifference = -1;
		long milliseconds;
		
		try {
			Date bannedTimeDate = format.parse(bannedTime);
			Date currentTimeDate = format.parse(currentTime);
			milliseconds = currentTimeDate.getTime() - bannedTimeDate.getTime();
			timeDifference = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
		} catch (ParseException e) {
			new ExcaptionHandler("Cannot parse String.Thrown By getTimeDifference()", e);
		}	
		
		return timeDifference;
	}

	/**
	 * The method returns the time left to the user to wait in order to reconnect
	 * @param socket the user's socket
	 * @return the remaining time until the user is unblocked
	 */
	public String getRemainingBanTime(Socket socket) {
		long remainingBanTime = 0;
		String clientIpAddress = extractIpFromSocket(socket);	
		String bannedTime = blockedIps.get(clientIpAddress);
		
		remainingBanTime = BAN_DURATION - getTimeDifference(bannedTime);
		
		return String.valueOf(remainingBanTime);
	}
	
	/**
	 * Check if the user is banned from the server
	 * @param socket the socket of the client
	 * @return true if the user is banned else false
	 */
	public synchronized boolean isBanned(Socket socket){		
		String clientIpAddress = extractIpFromSocket(socket);
		return blockedIps.containsKey(clientIpAddress);
	}
	
	/**
	 * Get the client's ip address from the socket Object
	 * @param the client's socket
	 * @return client's ip address
	 */
	private String extractIpFromSocket(Socket socket) {
		InetSocketAddress socketAddress= (InetSocketAddress)socket.getRemoteSocketAddress();
		return socketAddress.getAddress().getHostAddress();
	}

	/**
	 * Return the client's session model from the list
	 * @param emailAddress the user email
	 * @param clientIpAddress the client's ip address
	 * @return the client's session else return null
	 */
	private synchronized SessionModel getSessionModel(String emailAddress,String clientIpAddress) {
		SessionModel sessionModel = null;
		
		for(SessionModel session : sessions)	
			if (session.getIpAddress().equals(clientIpAddress) && session.getConnectedUser().equals(emailAddress)) {
				sessionModel = session;
				break;
		}
		return sessionModel;
	}
	
	/**
	 * Removes the session from the list
	 * @param sessionModel to be removed
	 */
	private synchronized void removeSessionModel(SessionModel sessionModel) {		
		sessions.remove(sessionModel);
	}
	
	/**
	 * register the client's session
	 * @param socket - client's socket
	 * @param emailAddress client's email address
	 * @throws SQLException
	 */
	public void userLoggedIn(Socket socket ,String emailAddress) throws SQLException {
		String ipAddress = extractIpFromSocket(socket);
		String connectionTime = getCurrentTime();	
		SessionModel sessionModel = new SessionModel(emailAddress,connectionTime,ipAddress);
		sessions.add(sessionModel);		
	}
	
	public boolean isConnected(String emailAddress) throws SQLException {
		for(SessionModel session : sessions)	
			if (session.getConnectedUser().equals(emailAddress))
				return true;
		return false;	
	}

	/**
	 * Ban the user from connecting to the server
	 * @param socket client's socket
	 */
	public synchronized void ban(Socket socket) {
		String ip = extractIpFromSocket(socket);
		String blockTime = getCurrentTime();
		blockedIps.put(ip, blockTime);		
	}
	
	/**
	 * Close the client's session if there is one
	 * @param clientSocket client's Socket Object
	 * @param emailAddress  client's email address
	 */
	public synchronized void closeSession(String emailAddress,Socket clientSocket) {
		String clientIpAddress = extractIpFromSocket(clientSocket);
		boolean isBlocked = blockedIps.containsKey(clientIpAddress);
		
		if (!isBlocked && emailAddress != null) {
			SessionModel sessionModel = getSessionModel(emailAddress,clientIpAddress);
			if (sessionModel != null) {
				String connectionTime = sessionModel.getConnectionTime();
				String disconnectionTime = getCurrentTime();
				 
				try {
					new SessionDAL().addSessionToDB(emailAddress,connectionTime,disconnectionTime, clientIpAddress);
				} catch (SQLException e) {
					new ExcaptionHandler("Exception while trying to disconnect.Thrown by closeSession()", e);
				}
				removeSessionModel(sessionModel);
			}			
		}		
	}
}