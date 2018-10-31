package Database;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Queue;

import exceptionsPackage.ExcaptionHandler;

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
	
	/**
	 * Retrieve the instance of the database
	 * @return The DatabaseConnector instance
	 */
	public static synchronized DatabaseConnector getDbConnectorInstance(){
		return DBConnector;
	}	
	
	/**
	 * The method execute a query without returned value
	 * @param query the query to run on the DB
	 * @param parameters the parameters will be set into the query
	 * @throws SQLException
	 */
	public synchronized void executeSqlStatement(String query,Queue<Object> parameters) throws SQLException{
		
		try {
			PreparedStatement statement = createPreparedStatement(query, parameters);
			statement.execute();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * The method build and execute a statment and return the ResultSet 
	 * @param query the query to run on the DB
	 * @param parameters the parameters will be set into the query
	 * @return ResultSet
	 * @throws SQLException
	 */
	public synchronized ResultSet executeSqlStatementResultSet(String query,Queue<Object> parameters) throws SQLException{
		ResultSet resultSet = null;
		
		try {
			PreparedStatement statement = createPreparedStatement(query, parameters);
			statement.execute();
			resultSet = statement.getResultSet();
		} catch (SQLException e) {
			new ExcaptionHandler("Exception Thrown by executeSqlStatementDataTable() with query: " + query, e);
			throw e;
		}
		return resultSet;
	}
	
	/** 
	 * The method build PreparedStatement from the received parameters
	 * @param query the query to run on the DB
	 * @param parameters the parameters will be set into the query
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

	/**
	 * The method create a CallableStatement with parameters and output value
	 * @param query the query to run on the DB
	 * @param parameters the parameters will be set into the query
	 * @param outputType which output type we expect - "INEGER" or "BOOLEAN"
	 * @return CallableStatement
	 * @throws SQLException
	 */
	private synchronized CallableStatement createCallableStatement(String query,Queue<Object> parameters,String outputType) throws SQLException{
		int parameterNum = 1;
		
		CallableStatement statement = DBConnection.prepareCall(query);
		if (outputType.equals("BOOLEAN")) {
			statement.registerOutParameter(parameterNum, java.sql.Types.BOOLEAN);
		}
		else if(outputType.equals("INTEGER")){
			statement.registerOutParameter(parameterNum, java.sql.Types.INTEGER);
		}
		
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
		return statement;
	}
	
	public synchronized int callRoutineReturnedScalarValue(String query,Queue<Object> parameters) throws SQLException{
		int returnedValue = -1;
		
		try {
			CallableStatement statement = createCallableStatement(query, parameters, "INTEGER");
			statement.execute();
			returnedValue = statement.getInt(1);	
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown by callSpWithSingleValue(). with statement: " + query ,e);
			throw e;
		}
		return returnedValue;
	}
	
	public synchronized boolean callRoutineReturnedBooleanScalarValue(String query,Queue<Object> parameters) throws SQLException{
		boolean returnedValue;
		
		try {
			CallableStatement statement = createCallableStatement(query, parameters, "INTEGER");
			statement.execute();
			returnedValue = statement.getBoolean(1);	
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown by callRoutineReturnedBooleanScalarValue() with statement: "+query ,e);
			throw e;
		}
		return returnedValue;
	}
}