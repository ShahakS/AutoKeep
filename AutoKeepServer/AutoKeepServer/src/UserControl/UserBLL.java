package UserControl;

import java.sql.SQLException;

import CommunicationManager.CommunicationInterpreter;

public class UserBLL {
	private CommunicationInterpreter interpreter;
	private UserDAL userDAL;
	
	public UserBLL() {
		interpreter = new CommunicationInterpreter();
		userDAL = new UserDAL();
	}
	
	public void changePassword(UserModel user) throws SQLException {
		String emailAddress = user.getEmailAddress();
		String password = user.getPassword();
		userDAL.changePassword(emailAddress,password);
	}
}
