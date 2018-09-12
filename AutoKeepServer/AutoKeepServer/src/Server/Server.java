package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Classes.Error;

public class Server {
	
	public static void main(String[] args) {
		ServerSocket listeningSocket = null;
		
		try {
			listeningSocket = new ServerSocket(40500);
			
		} catch (IOException e) {
			Error error = new Error("Error while creating the listening socket",e.getMessage());
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
				Error error = new Error("Error accepting a new client connection",e.getMessage());
				error.writeToErrorLog();
				break;
			}			
		}
		
		try {
			listeningSocket.close();
		} catch (IOException e) {
			Error error = new Error("Error while closing the listening socket",e.getMessage());
			error.writeToErrorLog();
		}
	}
}
