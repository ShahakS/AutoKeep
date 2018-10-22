package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

import server.DatabaseConnector;

public class ErrorLog{
	private String errorTimeStamp;
	private String customMeassage;
	private String errorMessage;
	private String stackTrace;
	
	public ErrorLog(String customMeassage,String errorMessage,String stackTrace) {
		this.customMeassage = customMeassage;
		this.errorMessage = errorMessage;
		this.stackTrace = stackTrace;
		this.errorTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
		writeToErrorLog();
	}
	
	public void writeToErrorLog() {
		DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
		String query = "insert into ErrorLog(ErrorTime,ErrorMessage,ErrorDetails,StackTrcae) values(?,?,?,?)";
		Queue<Object> parameters = new LinkedList<>();
		
		parameters.add(errorTimeStamp);
		parameters.add(customMeassage);
		parameters.add(errorMessage);
		parameters.add(stackTrace);
		
		try {
			DBconnector.executeSqlStatement(query,parameters);
		} catch (SQLException e) {
			String fileDirectory = System.getProperty("user.dir") + "\\Error_"+LocalDate.now()+".log";
			try {
				try(FileWriter fileWriter = new FileWriter(fileDirectory)){
					fileWriter.write(e.getStackTrace().toString());
				}				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
