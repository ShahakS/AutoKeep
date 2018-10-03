package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import classes.ErrorLog;

public class Server {
	
	public static void main(String[] args) {
		ServerSocket listeningSocket = null;
		
		try {
			listeningSocket = new ServerSocket(40500);
			
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
				new ClientHandler(clientSocket).start();
				
			} catch (IOException e) {
				ErrorLog error = new ErrorLog("Error accepting a new client connection",e.getMessage(),e.getStackTrace().toString());
				error.writeToErrorLog();
				break;
			}			
		}
		
		try {
			listeningSocket.close();
		} catch (IOException e) {
			ErrorLog error = new ErrorLog("Error while closing the listening socket",e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
		}
	}
}
