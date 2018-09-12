package Classes;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

import Server.DatabaseConnector;

public class Error extends Exception{

	private static final long serialVersionUID = 5477920981041300721L;
	private String errorTimeStamp;
	private String errorMessage;
	private String errorDetails;
	
	public Error(String errorMeassage,String errorDetails) {
		this.errorMessage = errorMeassage;
		this.errorDetails = errorDetails;
		this.errorTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
	}
	
	public void writeToErrorLog() {
		DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
		String query = "insert into ErrorLog(ErrorTime,ErrorMessage,ErrorDetails) values(?,?,?)";
		Queue<Object> parameters = new LinkedList<>();
		
		parameters.add(errorTimeStamp);
		parameters.add(errorMessage);
		parameters.add(errorDetails);
		DBconnector.execute(query,parameters);
	}
}
