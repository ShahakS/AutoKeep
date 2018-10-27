package UserControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Database.DatabaseConnector;

public class UserDAL {
	DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
	
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
			boolean isUserCredentialValid = false;

			parameters.add(user.getEmailAddress());
			parameters.add(user.getPassword());
			int returnedValue = DBconnector.callRoutineReturnedScalarValue(query, parameters);
			
			if (returnedValue != 0)
				isUserCredentialValid = true;
			
			return isUserCredentialValid;
		}
		
		public void deleteUser(UserModel user) throws SQLException {
			String query = "{call sp_DeleteUser(?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(user.getEmailAddress());
			DBconnector.executeSqlStatement(query, parameters);
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
			parameters.add(user.IsAdministrator());
			DBconnector.executeSqlStatement(query, parameters);
		}

		public UserModel getUser(String emailAddress) throws SQLException{
			String query = "SELECT * FROM fn_GetUserByEmailAddress(?)";
			Queue<Object> parameters = new LinkedList<>();
			UserModel user = null;
			
			parameters.add(emailAddress);			
			ResultSet resultSet = DBconnector.executeSqlStatementDataTable(query, parameters);
			
			while (resultSet.next()){
				String password = resultSet.getString("password");
				String dateOfBirth = resultSet.getString("dateOfBirth");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String phoneNumber = resultSet.getString("phoneNumber");
				boolean IsAdministrator = resultSet.getBoolean("IsAdministrator");
				user = new UserModel(emailAddress, password, dateOfBirth, firstName, lastName, phoneNumber, IsAdministrator);
			}
					
			return user;
		}
}
