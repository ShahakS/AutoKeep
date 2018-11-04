package UserControl;

import java.net.Socket;
import java.sql.SQLException;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import SessionControl.SessionManager;
import exceptionsPackage.ExcaptionHandler;
import server.ClientHandler;

public class UserBLL {
	private CommunicationInterpreter interpreter;
	private UserDAL userDAL;
	public UserModel user;
	private SessionManager sessionManager;
	private int connectionRetries;
	
	public UserBLL() {
		connectionRetries = ClientHandler.connectionRetries;
		this.interpreter = new CommunicationInterpreter();
		this.userDAL = new UserDAL();
		this.sessionManager = SessionManager.startSession();;
	}
	/**
	 * Change the client password
	 * @param incomingData received by the client
	 * @return a string contains the answer and ready to be sent to the client
	 */
	public String changePassword(String incomingData) {
		String outgoingData,message;
		UserModel user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL, incomingData);
		String password = user.getPassword();
		
		try {
			userDAL.changePassword(user.getEmailAddress(),password);
			ProtocolMessage protocolMsg = ProtocolMessage.PASSWORD_CHANGED_SUCCESSFULLY;
			message = ProtocolMessage.getMessage(protocolMsg);
			outgoingData = interpreter.encodeObjToJson(protocolMsg, message);
		} catch (SQLException e) {
			message = ProtocolMessage.getMessage(ProtocolMessage.CHANGING_PASSWORD_FAILED);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}
		return outgoingData;
	}
	
	public String authenticate(String credentials,Socket clientSocket) {
		String outputData;

		try {
			if (interpreter.getProtocolMsg(credentials) != ProtocolMessage.LOGIN) {
				String message = ProtocolMessage.getMessage(ProtocolMessage.WRONG_PROTOCOL_REQUEST);
				outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
			}else {
				UserModel user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL,credentials);
				boolean isAuthenticated = userDAL.isUserCredentialValid(user);
				ProtocolMessage protocolMessage;
				
				if (isAuthenticated && sessionManager.isConnected(user.getEmailAddress())) {
					String message = ProtocolMessage.getMessage(ProtocolMessage.USER_ALREADY_CONNECTED);
					outputData = interpreter.encodeObjToJson(ProtocolMessage.USER_ALREADY_CONNECTED,message);		
				}
				else if (isAuthenticated) {
					sessionManager.userLoggedIn(clientSocket,user.getEmailAddress());
					this.user = getUserModel(user.getEmailAddress());
					outputData = interpreter.encodeObjToJson(ProtocolMessage.OK,this.user);
				}else if (connectionRetries == 1) {
					ban(clientSocket);
					protocolMessage = ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES;
					outputData = interpreter.encodeObjToJson(protocolMessage,ProtocolMessage.getMessage(protocolMessage));
					connectionRetries--;
				}else{
					 protocolMessage = ProtocolMessage.WRONG_CREDENTIAL;
					 outputData = interpreter.encodeObjToJson(protocolMessage,ProtocolMessage.getMessage(protocolMessage));		
				}
			}
		} catch (SQLException e) {
			new ExcaptionHandler("Exception authenticate user.Thrown by authenticate()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);		
		}		
		return outputData;
	}

	/**
	 * Retrieve the UserModel of that email address
	 * @param emailAddress
	 * @return UserModel
	 * @throws SQLException
	 */
	public UserModel getUserModel(String emailAddress) throws SQLException {
		UserModel userModel = userDAL.getUser(emailAddress);
		return userModel;
	}

	/**
	 * Preparing to close the connection
	 * @param sessionManager the session manager instance
	 * @param emailAddress the client's email address
	 * @param clientSocket the client's socket
	 */
	public void disconnect(Socket clientSocket) {
		if(user != null)
			sessionManager.closeSession(user.getEmailAddress(),clientSocket);			
	}
	
	public String isBanned(Socket socket) {
		String outputData = null;
		
		if (sessionManager.isBanned(socket)) {
			ProtocolMessage protocolMsg = ProtocolMessage.USER_IS_BANNED;
			String customMsg = ProtocolMessage.getMessage(protocolMsg,sessionManager.getRemainingBanTime(socket));
			outputData = interpreter.encodeObjToJson(protocolMsg,customMsg);
		}
		return outputData;
	}
	
	/**
	 * Ban the current ip address
	 * @param clientSocket
	 */
	public void ban(Socket clientSocket) {
		sessionManager.ban(clientSocket);		
	}
}