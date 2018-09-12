package Server;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;

import Classes.Error;

public class DatabaseConnector {
	private static DatabaseConnector DBConnector = new DatabaseConnector();
	private final int DATABASE_PORT = 1544;
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
	
	public void execute(String query,Queue parameters){
		try {
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
			statement.execute();
		} catch (SQLException e) {
			Error error = new Error("Error executing Prepared Statement with query: "+query,e.getMessage());
			error.writeToErrorLog();
		}
	}
	
	public boolean callSpWithSingleValue(String query,Queue parameters){
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
			Error error = new Error("Error Calling Stored Procedure with query: "+query,e.getMessage());
			error.writeToErrorLog();
		}
		return returnedValue;
	}
}
