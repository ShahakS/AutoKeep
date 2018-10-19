package server;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import classes.CommunicationInterpreter;
import classes.ErrorLog;
import classes.ProtocolMessage;
import classes.UserBLL;
import classes.UserDAL;
import classes.UserModel;

public class ClientHandlerBLL {
	private CommunicationInterpreter interpreter;
	
	public ClientHandlerBLL() {
		interpreter = new CommunicationInterpreter();
	}
	
	for(int numOfRetries = 5;!isAuthenticated && numOfRetries > 0;numOfRetries--) {
		try {
			String clientCredential = readClientData();
			UserModel user = (UserModel)interpreter.decodeFromJsonToObj(clientCredential);
			UserBLL userBLL = new UserBLL();
			String authResponse = userBLL.connect(user);
			
			if (interpreter.getProtocolMsg(authResponse).equals(ProtocolMessage.OK)) {
				isAuthenticated = true;
			}
			sendObjToClient(authResponse);
			
		} catch (ClassNotFoundException | SQLException e) {
			new ErrorLog("Exception while authenticate user", e.getMessage(), e.getStackTrace().toString());
			String errorMsg = ProtocolMessage.getStatus(ProtocolMessage.ERROR);
			String errorJsonString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
			sendObjToClient(errorJsonString);				
		}
	}
	
	
}
