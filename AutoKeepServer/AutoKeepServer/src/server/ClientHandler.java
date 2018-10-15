package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import classes.CommunicationInterpreter;
import classes.ErrorLog;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private DatabaseConnector db;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	
	public ClientHandler(Socket socket){
		this.clientSocket = socket;
		this.db = DatabaseConnector.getDbConnectorInstance();
		this.interpreter = new CommunicationInterpreter();
		try {
			this.readClientData = new ObjectInputStream(socket.getInputStream());
			this.sendClientData = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			ErrorLog error = new ErrorLog("Error while creating input/output stream",e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
		}		
	}
	
	public void run() {
		System.out.println("Connected");
		
	}
	
	public String bll(String str) {
		switch(interpreter.getProtocolMsg(str)) {
		case LOGIN:
			
			break;
		}
		return str;
	}
}
