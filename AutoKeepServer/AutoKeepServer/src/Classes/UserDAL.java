package Classes;

import java.util.LinkedList;
import java.util.Queue;

import Server.DatabaseConnector;

public class UserDAL {
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return true if the credential is valid, else return false
	 */
		public boolean isUserCredentialValid(String userName, String password) {
			DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
			String query = "{?= call isUserExist(?, ?)}";
			Queue<Object> parameters = new LinkedList<>();
			boolean isUserCredentialValid;

			parameters.add(userName);
			parameters.add(password);
			isUserCredentialValid = DBconnector.callSpWithSingleValue(query, parameters);

			return isUserCredentialValid;
		}
}
