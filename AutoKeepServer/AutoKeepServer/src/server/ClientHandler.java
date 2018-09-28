package server;

import java.net.Socket;

public class ClientHandler extends Thread{
	private Socket clientSocket;
	private DatabaseConnector db = DatabaseConnector.getDbConnectorInstance();
	
	public ClientHandler(Socket socket){
		clientSocket = socket; 
	}
	
	public void run() {
		System.out.println("Connected");
	}
}
