package exceptionsPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

import Database.DatabaseConnector;

public class ExcaptionHandler{
	private String errorTimeStamp;
	private String customMeassage;
	private String errorMessage;
	private String stackTrace;
	
	public ExcaptionHandler(String customMeassage,Exception exception) {
		this.errorTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
		this.customMeassage = customMeassage;
		this.errorMessage = exception.getMessage();
		this.stackTrace = getExceptionStackTrace(exception);
		
		writeToErrorLog();
	}
	
	public ExcaptionHandler(Exception exception) {
		writeLogToLocalMachine(exception);
	}

	private String getExceptionStackTrace(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		String stackTrace = stringWriter.toString();
		return stackTrace;
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
			writeLogToLocalMachine(e);
		}
	}
	
	public void writeLogToLocalMachine(Exception exception) {
		String userDirectory = System.getProperty("user.dir");
		new File(userDirectory+"/Logs").mkdirs();
		String fileDirectory = userDirectory + "\\Logs\\Error_"+System.currentTimeMillis()+".log";
		try {
			try(FileWriter fileWriter = new FileWriter(fileDirectory)){
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				exception.printStackTrace(printWriter);
				fileWriter.write(stringWriter.toString());
			}				
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
