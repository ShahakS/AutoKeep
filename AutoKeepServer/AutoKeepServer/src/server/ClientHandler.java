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
import classes.VehicleModel;

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
		boolean isAuthenticated = connect();
		
		
		
		if (isAuthenticated) {
			bll();
//			while(true) {
	//			try {
	//				clientCredential = (String) readClientData.readObject();
	//				System.out.println(clientCredential);
	//				String jsonString = bll(clientCredential);
	//				sendObjToClient(jsonString);
	//				
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}catch (ClassNotFoundException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
//			}
		}else{
			String errorMsg = ProtocolMessage.getStatus(ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES);
			String errorString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
			sendObjToClient(errorString);
		}
	}

	public String bll() {
		String answer = null;
		
		try {
			String incomingData = (String) readClientData();
			
			switch(interpreter.getProtocolMsg(incomingData)) {
				case SEARCH_VEHICLE:
					 VehicleModel vehicle = (VehicleModel)interpreter.decodeFromJsonToObj(ProtocolMessage.VEHICLE_MODEL, incomingData);
					 
					 break;
				
				default:
					
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return answer;
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
				String errorMsg = ProtocolMessage.getStatus(ProtocolMessage.ERROR);
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
	 * @param jsonString - Json represented by String
	 */
	private void sendObjToClient(String jsonString){
		try {
			sendClientData.reset();
			sendClientData.writeObject(jsonString);
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
		String clientData = null;
		clientData = (String) readClientData.readObject();

		return clientData;			
	}
	
}