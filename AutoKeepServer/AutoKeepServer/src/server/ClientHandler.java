package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import SessionControl.SessionManager;
import UserControl.UserDAL;
import UserControl.UserModel;
import exceptionsPackage.ExcaptionHandler;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	private UserModel user;
	private SessionManager sessionManager;
	
	public ClientHandler(Socket socket) throws IOException{
		this.sessionManager = SessionManager.startSession();
		user = new UserModel();
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
					isConnected = false;
				}catch (ClassNotFoundException e) {
					new ExcaptionHandler("Exception Thrown by ClientHandler while casting",e);
					String errorMsg = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
					String errorJsonString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
					sendObjToClient(errorJsonString);	
				}
			}
		}
		disconnect();
		System.out.println("Disconnected");
	}

	private void disconnect() {
		sessionManager.closeSession(user.getEmailAddress(),clientSocket);
	}

	/**
	 * Connect the user to the application with the given credentials
	 * @return true if credential is passed else returns false
	 */
	private boolean connect() {
		UserDAL userDAL = new UserDAL();
		boolean isAuthenticated = false;
		String authResponse;
		
		if (sessionManager.isBanned(clientSocket)) {
			ProtocolMessage protocolMsg = ProtocolMessage.USER_IS_BANNED;
			String customMsg = ProtocolMessage.getMessage(protocolMsg,sessionManager.getRemainingBanTime(clientSocket));
			String errorJsonString = interpreter.encodeObjToJson(protocolMsg,customMsg);
			sendObjToClient(errorJsonString);
			return false;
		}
		
		for(int numOfRetries = 5;!isAuthenticated && numOfRetries > 0;numOfRetries--) {
			try {
				String clientCredential = readClientData();
				user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL,clientCredential);

				isAuthenticated = userDAL.isUserCredentialValid(user);
				ProtocolMessage protocolMessage;
				
				if (isAuthenticated) {
					sessionManager.userLoggedIn(clientSocket,user.getEmailAddress());
					protocolMessage = ProtocolMessage.OK;
					authResponse = interpreter.encodeObjToJson(protocolMessage, ProtocolMessage.getMessage(protocolMessage));
					isAuthenticated = true;
				}else if (numOfRetries == 1) {
					sessionManager.ban(clientSocket);
					protocolMessage = ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES;
					authResponse = interpreter.encodeObjToJson(protocolMessage,ProtocolMessage.getMessage(protocolMessage));
				}else{
					 protocolMessage = ProtocolMessage.WRONG_CREDENTIAL;
					 authResponse = interpreter.encodeObjToJson(protocolMessage,ProtocolMessage.getMessage(protocolMessage));		
				}				
				sendObjToClient(authResponse);				
			} catch (ClassNotFoundException | SQLException e) {
				new ExcaptionHandler("Exception while authenticate user", e);
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
			new ExcaptionHandler("sendObjToClient()",e);
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