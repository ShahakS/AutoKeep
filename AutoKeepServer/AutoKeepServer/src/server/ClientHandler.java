package server;

import java.net.Socket;

import classes.CommunicationInterpreter;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private DatabaseConnector db;
	private CommunicationInterpreter interpreter;
	
	public ClientHandler(Socket socket){
		clientSocket = socket;
		db = DatabaseConnector.getDbConnectorInstance();
		interpreter = new CommunicationInterpreter();
	}
	
	public void run() {
		System.out.println("Connected");
		//bll("TEST");
	}
	
	public String bll(String str) {
		switch(interpreter.getProtocolMsg(str)) {
		case RESERVATION_MODEL:
			break;
		}
		return str;
	}
}
