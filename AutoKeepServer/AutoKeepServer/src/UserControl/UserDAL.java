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

			parameters.add(user.getEmailAddress());
			parameters.add(user.getPassword());
			boolean isUserCredentialValid = DBconnector.callRoutineReturnedBooleanScalarValue(query, parameters);			
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
			ResultSet resultSet = DBconnector.executeSqlStatementResultSet(query, parameters);
			
			while (resultSet.next()){
				int userID = resultSet.getInt("userID");
				String password = resultSet.getString("password");
				String dateOfBirth = resultSet.getString("dateOfBirth");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String phoneNumber = resultSet.getString("phoneNumber");
				boolean IsAdministrator = resultSet.getBoolean("IsAdministrator");
				user = new UserModel(userID,emailAddress, password, dateOfBirth, firstName, lastName, phoneNumber, IsAdministrator);
			}
					
			return user;
		}

		public void changePassword(String emailAddress, String password) throws SQLException {
			String query = "{call sp_UpdateUserPassword(?,?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(emailAddress);
			parameters.add(password);
			DBconnector.executeSqlStatement(query, parameters);			
		}

		public Queue<UserModel> getActiveUsers() throws SQLException {
			String query = "SELECT * FROM fn_GetActiveUsers()";
			Queue<Object> parameters = new LinkedList<>();
			Queue<UserModel> activeUsers = new LinkedList<>();
						
			ResultSet resultSet = DBconnector.executeSqlStatementResultSet(query, parameters);
			
			while (resultSet.next()){
				int userID = resultSet.getInt("userID");
				String password = resultSet.getString("password");
				String emailAddress = resultSet.getString("emailAddress");
				String dateOfBirth = resultSet.getString("dateOfBirth");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String phoneNumber = resultSet.getString("phoneNumber");
				boolean IsAdministrator = resultSet.getBoolean("IsAdministrator");
				UserModel user = new UserModel(userID,emailAddress, password, dateOfBirth, firstName, lastName, phoneNumber, IsAdministrator);
				activeUsers.add(user);
			}
			return activeUsers;
		}

		public boolean insertNewUser(UserModel newUser) throws SQLException {
			String query = "{?= call sp_InsertNewUser(?, ?, ?, ?, ?, ?, ?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(newUser.getEmailAddress());
			parameters.add(newUser.getPassword());
			parameters.add(newUser.getFirstName());
			parameters.add(newUser.getLastName());
			parameters.add(newUser.getPhoneNumber());
			parameters.add(newUser.getDateOfBirth());
			parameters.add(newUser.IsAdministrator());
			boolean isAddedSuccessfully = DBconnector.callRoutineReturnedBooleanScalarValue(query, parameters);
			
			return isAddedSuccessfully;
		}

		public void deleteUserByEmail(String emailAddress) throws SQLException {
			String query = "{call sp_DeleteUserByEmailAddress(?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(emailAddress);
			DBconnector.executeSqlStatement(query, parameters);		
		}

		public void updateUser(UserModel user) throws SQLException {
			String query = "{call sp_UpdateUser(?,?,?,?,?,?,?)}";
			Queue<Object> parameters = new LinkedList<>();

			parameters.add(user.getEmailAddress());
			parameters.add(user.getPassword());
			parameters.add(user.getFirstName());
			parameters.add(user.getLastName());
			parameters.add(user.getPhoneNumber());
			parameters.add(user.getDateOfBirth());
			parameters.add(user.IsAdministrator());				
			DBconnector.executeSqlStatement(query, parameters);				
		}
}
