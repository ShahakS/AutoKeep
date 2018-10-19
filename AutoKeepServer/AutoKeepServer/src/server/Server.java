package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import classes.ErrorLog;

public class Server {
	private final int THREAD_NUMBER = 20;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
	
	/**
	 * Listen for new connections and open thread that handle the client's request
	 */
	public void run() {
		ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
		ServerSocket listeningSocket = null;		
		
		try {
			listeningSocket = new ServerSocket(40511);
			
		} catch (IOException e) {
			ErrorLog error = new ErrorLog("Error while creating the listening socket",e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
			System.exit(1);
		}
		
 		while (true){
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for Connection . . .");
				clientSocket = listeningSocket.accept();
				threadPool.execute(new ClientHandler(clientSocket));
			} catch (IOException e) {
				new ErrorLog("Error accepting a new client connection - Server.java",e.getMessage(),e.getStackTrace().toString());
				break;
			}			
		}
		
		try {
			listeningSocket.close();
		} catch (IOException e) {
			ErrorLog error = new ErrorLog("Error while closing the listening socket",e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
		}finally {
			threadPool.shutdown();
		}		
	}
}
