package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Database.DatabaseConnector;
import exceptionsPackage.ExcaptionHandler;

public class Server {
	private boolean isRunning = true;
	private final int THREAD_NUMBER = 20;
	private final int LISTENING_PORT = 40501;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
	
	/**
	 * Listen for new connections and open thread that handle the client's request
	 */
	/**
	 * 
	 */
	public void run() {
		ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
		ServerSocket listeningSocket = null;		
		
		//Checking if there is a connection to the Database
		if(DatabaseConnector.getDbConnectorInstance() != null) {		
			try {
				listeningSocket = new ServerSocket(LISTENING_PORT);			
			} catch (IOException e) {
				new ExcaptionHandler("Error while creating the listening socket",e);
				isRunning = false;
			}
			
	 		while (isRunning){
				try {
					System.out.println("Waiting for Connection . . .");
					Socket clientSocket = listeningSocket.accept();
					threadPool.execute(new ClientHandler(clientSocket));
				} catch (IOException e) {
					new ExcaptionHandler("Error accepting a new client connection - Server.java",e);
					isRunning= false;
				}			
			}
			
			try {
				if(!isRunning)
					listeningSocket.close();
			} catch (IOException e) {
				new ExcaptionHandler("Error while closing the listening socket",e);
			}
			threadPool.shutdown();
		}
	}
}
