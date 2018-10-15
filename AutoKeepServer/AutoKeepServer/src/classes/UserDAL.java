package classes;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import server.DatabaseConnector;

public class UserDAL {
	DatabaseConnector DBconnectort = DatabaseConnector.getDbConnectorInstance();
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return true if the credential is valid, else return false
	 * @throws SQLException 
	 */
		public boolean isUserCredentialValid(UserModel user) throws SQLException {
			String query = "{?= call fn_IsUserExist(?, ?)}";
			Queue<Object> parameters = new LinkedList<>();
			boolean isUserCredentialValid;

			parameters.add(user.getEmailAddress());
			parameters.add(user.getPassword());
			isUserCredentialValid = DBconnectort.callRoutineReturnedScalarValue(query, parameters);

			return isUserCredentialValid;
		}
		
		public void deleteUser(UserModel user) throws SQLException {
			String query = "{call sp_DeleteUser(?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(user.getUserID());
			DBconnectort.executeQueryWithoutReturnedValue(query, parameters);
		}
		
		public void addUser(UserModel user) throws SQLException {
			String query = "{call sp_InsertNewUser(?,?,?,?,?,?,?,?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(user.getPassword());
			parameters.add(user.getFirstName());
			parameters.add(user.getLastName());
			parameters.add(user.getPhoneNumber());
			parameters.add(user.getEmailAddress());
			parameters.add(user.getDateOfBirth());
			parameters.add(user.isAdmin());
			DBconnectort.executeQueryWithoutReturnedValue(query, parameters);
		}

		public UserModel getUser(String emailAddress) {
			String query = "{?= call fn_GetUserByEmailAddress(?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(emailAddress);
			try {
				DBconnectort.callRoutineReturnedTableValue(query, parameters);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
}
