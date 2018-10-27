package classes;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import server.DatabaseConnector;

public class SessionDAL {
	DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();	
	
	public int addSessionToDB(String emailAddress,String connectionTime,String disconnectionTime,String ipAddress) throws SQLException {
		String query = "{?= call sp_InsertNewSession(?,?,?,?)}";
		Queue<Object> parameters = new LinkedList<>();
		
		parameters.add(emailAddress);
		parameters.add(connectionTime);	
		parameters.add(disconnectionTime);	
		parameters.add(ipAddress);		
		
		return DBconnector.callRoutineReturnedScalarValue(query, parameters);
	}
}