package server;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;

import classes.ErrorLog;

public class DatabaseConnector {
	private static DatabaseConnector DBConnector = new DatabaseConnector();
	private final int DATABASE_PORT = 1433;
	private final String DATABASE_IP = "127.0.0.1";
	private final String DATABASE_NAME = "AutoKeep";
	private Connection DBConnection;
	private final String dbFilePathString = "jdbc:sqlserver://" + DATABASE_IP + ":"
			+ DATABASE_PORT + ";databaseName="
			+ DATABASE_NAME 
			+ ";integratedSecurity=true;";
	
	private DatabaseConnector(){
		//Load the DB driver
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//Establish connection to the database 
			DBConnection = DriverManager.getConnection(dbFilePathString);
			
			//Set Automatic commit as ON
			DBConnection.setAutoCommit(true);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SQLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized DatabaseConnector getDbConnectorInstance(){
		return DBConnector;
	}	
	
	/**
	 * The method execute a query without returned values
	 * @param query
	 * @param parameters
	 * @throws SQLException
	 */
	public synchronized void executeQueryWithoutReturnedValue(String query,Queue<Object> parameters) throws SQLException{
		
		try {
			PreparedStatement statement = createPreparedStatement(query, parameters);
			statement.execute();
		} catch (SQLException e) {
			ErrorLog error = new ErrorLog("Error occurred at executeQueryWithoutReturnedValue with query: " + query,e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
			throw e;
		}
	}
	
	/** 
	 * The method build PreparedStatement from the received parameters
	 * @param query
	 * @param parameters
	 * @return PreparedStatement from the parameters
	 * @throws SQLException
	 */
	private synchronized PreparedStatement createPreparedStatement(String query,Queue<Object> parameters) throws SQLException {
		PreparedStatement statement = DBConnection.prepareStatement(query);

		for(int parameterNum = 1;!parameters.isEmpty();parameterNum++)
		{
			Object parameter = parameters.poll();
			if (parameter instanceof String) {
				statement.setString(parameterNum,(String)parameter);
			}
			else if (parameter instanceof Integer) {
				statement.setInt(parameterNum,((Integer)parameter).intValue());
			}
			else if (parameter instanceof Boolean){
				statement.setBoolean(parameterNum, ((Boolean)parameter).booleanValue());
			}
		}
		return statement;
	}
	
	public synchronized boolean callRoutineReturnedScalarValue(String query,Queue<Object> parameters) throws SQLException{
		int parameterNum = 1;
		boolean returnedValue = false;
		
		try {
			CallableStatement statement = DBConnection.prepareCall(query);
			statement.registerOutParameter(parameterNum, java.sql.Types.BOOLEAN);
			
			for(parameterNum++;!parameters.isEmpty();parameterNum++)
			{
				Object parameter = parameters.poll();
				if (parameter instanceof String) {
					statement.setString(parameterNum,(String)parameter);
				}
				else if (parameter instanceof Integer) {
					statement.setInt(parameterNum,((Integer)parameter).intValue());
				}
				else if (parameter instanceof Boolean){
					statement.setBoolean(parameterNum, ((Boolean)parameter).booleanValue());
				}
			}
			statement.execute();
			returnedValue = statement.getBoolean(1);	
		} catch (SQLException e) {
			ErrorLog error = new ErrorLog("Error Calling Stored Procedure from callSpWithSingleValue with statement: "+query,e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
			throw e;
		}
		return returnedValue;
	}
}
