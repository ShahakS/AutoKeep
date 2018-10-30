package UserControl;

import java.net.Socket;
import java.sql.SQLException;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import SessionControl.SessionManager;

public class UserBLL {
	private CommunicationInterpreter interpreter;
	private UserDAL userDAL;
	
	public UserBLL() {
		this.interpreter = new CommunicationInterpreter();
		this.userDAL = new UserDAL();
	}
	
	public String changePassword(String incomingData) {
		String outgoingData,message;
		UserModel user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL, incomingData);
		String emailAddress = user.getEmailAddress();
		String password = user.getPassword();
		try {
			userDAL.changePassword(emailAddress,password);
			ProtocolMessage protocolMsg = ProtocolMessage.PASSWORD_CHANGED_SUCCESSFULLY;
			message = ProtocolMessage.getMessage(protocolMsg);
			outgoingData = interpreter.encodeObjToJson(protocolMsg, message);
		} catch (SQLException e) {
			message = ProtocolMessage.getMessage(ProtocolMessage.CHANGING_PASSWORD_FAILED);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}
		return outgoingData;
	}

	public UserModel getUserModel(String emailAddress) throws SQLException {
		UserModel userModel = userDAL.getUser(emailAddress);
		return userModel;
	}

	public void disconnect(SessionManager sessionManager,String emailAddress, Socket clientSocket) {			
		sessionManager.closeSession(emailAddress,clientSocket);			
	}
}
