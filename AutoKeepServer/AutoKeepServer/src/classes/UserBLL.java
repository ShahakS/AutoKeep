package classes;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class UserBLL {
	private CommunicationInterpreter interpreter;
	
	public UserBLL() {
		interpreter = new CommunicationInterpreter();
	}
	
	public String connect(UserModel user) throws SQLException {
		String authenticationAnswer;
		ProtocolMessage protocolMessage;		
		
		UserDAL userDAL = new UserDAL();
		boolean isCredentialValid = userDAL.isUserCredentialValid(user);
		 
		 if (isCredentialValid) {
			 protocolMessage = ProtocolMessage.OK;
			 Queue<String> keys= new LinkedList<>();
			 Queue<String> values = new LinkedList<>();
			 keys.add("user");
			 values.add("{IsAdministrator:\"false\"}");
			 authenticationAnswer = interpreter.encodeParametersToJson(protocolMessage, keys, values);
		}else {
			 protocolMessage = ProtocolMessage.WRONG_CREDENTIAL;
			 authenticationAnswer = interpreter.encodeObjToJson(protocolMessage,ProtocolMessage.getMessage(protocolMessage));		
		}
		return authenticationAnswer;
	}
}
