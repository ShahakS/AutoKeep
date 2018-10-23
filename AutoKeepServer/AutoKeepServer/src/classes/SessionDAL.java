package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import server.DatabaseConnector;

public class SessionDAL {
	DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();	
	
	public SessionModel initializeNewSession(String connectedUser) throws SQLException {
		String query = "{?= call sp_InsertNewSession(?)}";
		Queue<Object> parameters = new LinkedList<>();

		parameters.add(connectedUser);
		int sessionID = DBconnector.callRoutineReturnedScalarValue(query, parameters);
		
		return getCorrectSession(sessionID);
	}
	
	private SessionModel getCorrectSession(int sessionID) throws SQLException {
		String query = "SELECT * FROM fn_GetSeesionBySessionID(?)";
		Queue<Object> parameters = new LinkedList<>();
		SessionModel newSession = null;
		
		parameters.add(sessionID);
		ResultSet resultSet = DBconnector.executeSqlStatementDataTable(query, parameters);

		if (resultSet.next()){
			String connectedUser = resultSet.getString("ConnectedUser");
			String connectionTime = resultSet.getString("ConnectionTime");
			String disconnectionTime = resultSet.getString("DisconnectionTime");
			newSession = new SessionModel(sessionID, connectedUser, connectionTime, disconnectionTime);
		}
		return newSession;
	}
	
	public void closeCorrectSession(int emailAddress) throws SQLException {
		try {
			String query = "{call sp_UpdateDisconnectionTimeSession(?)}";
			Queue<Object> parameters = new LinkedList<>();
			parameters.add(emailAddress);
			
			DBconnector.executeSqlStatement(query, parameters);
		} catch (SQLException e) {
			new ErrorLog("Exception Thrown by closeCorrectSession()", e.getMessage(), e.getStackTrace().toString());
			throw e;
		}
	}
}
