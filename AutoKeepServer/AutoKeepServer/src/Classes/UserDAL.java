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
		public boolean isUserCredentialValid(UserModel user) {
			DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
			String query = "{?= call sp_Get_IsUserExist(?, ?)}";
			Queue<Object> parameters = new LinkedList<>();
			boolean isUserCredentialValid;

			parameters.add(user.getUserName());
			parameters.add(user.getPassword());
			isUserCredentialValid = DBconnector.callSpWithSingleValue(query, parameters);

			return isUserCredentialValid;
		}
}
