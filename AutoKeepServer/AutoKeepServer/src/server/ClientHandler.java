package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import classes.CommunicationInterpreter;
import classes.ErrorLog;
import classes.ProtocolMessage;
import classes.UserBLL;
import classes.UserModel;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	
	public ClientHandler(Socket socket) throws IOException{
		this.clientSocket = socket;
		this.interpreter = new CommunicationInterpreter();
		this.readClientData = new ObjectInputStream(clientSocket.getInputStream());
		this.sendClientData = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	public void run() {
		System.out.println("Connected");	
		
		boolean isConnected = true;
		boolean isAuthenticated = connect();
		
		if (isAuthenticated) {
			ClientHandlerBLL clientHandlerBLL = new ClientHandlerBLL();
			
			while(isConnected) {
				try {
					String incomingData = (String) readClientData.readObject();
					String OutgoingData = clientHandlerBLL.bll(incomingData);
					sendObjToClient(OutgoingData);
					
				}catch (IOException e) {
					new ErrorLog("Exception Thrown by ClientHandler while reading client's data", e.getMessage(), e.getStackTrace().toString());
					isConnected = false;
				}catch (ClassNotFoundException e) {
					new ErrorLog("Exception Thrown by ClientHandler while casting", e.getMessage(), e.getStackTrace().toString());
					String errorMsg = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
					String errorJsonString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
					sendObjToClient(errorJsonString);	
				}
			}
		}else{
			String errorMsg = ProtocolMessage.getMessage(ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES);
			String errorString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
			sendObjToClient(errorString);
		}
		System.out.println("Disconnected");
	}

	/**
	 * Connect the user to the application with the given credentials
	 * @return true if credential is passed else returns false
	 */
	private boolean connect() {
		UserBLL userBLL = new UserBLL();
		boolean isAuthenticated = false;
		
		for(int numOfRetries = 5;!isAuthenticated && numOfRetries > 0;numOfRetries--) {
			try {
				String clientCredential = readClientData();
				UserModel user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL,clientCredential);				
				String authResponse = userBLL.connect(user);
				
				if (interpreter.getProtocolMsg(authResponse).equals(ProtocolMessage.OK)) {
					isAuthenticated = true;
				}
				sendObjToClient(authResponse);				
			} catch (ClassNotFoundException | SQLException e) {
				new ErrorLog("Exception while authenticate user", e.getMessage(), e.getStackTrace().toString());
				String errorMsg = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
				String errorJsonString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
				sendObjToClient(errorJsonString);				
			} catch (IOException e) {
				//Connection closed
				return false;
			}
		}
		return isAuthenticated;
	}
	
	/**
	 * Send a String object to Client
	 * @param outgoingString - JsonObj represented by String
	 */
	private void sendObjToClient(String outgoingString){
		try {
			sendClientData.reset();
			sendClientData.writeObject(outgoingString);
		} catch (IOException e) {
			//Connection closed
			new ErrorLog("sendObjToClient()", e.getMessage(), e.getStackTrace().toString());
		}		
	}
	
	/**
	 * Read the data sent from the current client
	 * @return String represents the received data
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private String readClientData() throws IOException, ClassNotFoundException  {		
		String clientData = (String) readClientData.readObject();
		return clientData;			
	}
	
}