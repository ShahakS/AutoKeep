package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationBLL;
import SessionControl.SessionManager;
import UserControl.UserBLL;
import UserControl.UserDAL;
import UserControl.UserModel;
import VehicleControl.VehicleBLL;
import exceptionsPackage.ExcaptionHandler;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	private SessionManager sessionManager;
	private UserModel user;
	private UserBLL userBLL;
	private VehicleBLL vehicleBLL;
	private ReservationBLL reservationBLL;
	
	public ClientHandler(Socket socket) throws IOException{
		this.sessionManager = SessionManager.startSession();
		this.user = new UserModel();
		this.userBLL = new UserBLL();
		this.vehicleBLL = new VehicleBLL();
		this.reservationBLL = new ReservationBLL();
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
			while(isConnected) {
				try {
					String incomingData = (String) readClientData.readObject();
					String OutgoingData = businessLogicFlow(incomingData);					
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
		userBLL.disconnect(sessionManager,user.getEmailAddress(),clientSocket);
		System.out.println("Disconnected");
	}

	public String businessLogicFlow(String incomingData) {
		String outgoingData = null;
			
		switch(interpreter.getProtocolMsg(incomingData)) {
			case SEARCH_VEHICLE:
				outgoingData = vehicleBLL.searchVehicle(incomingData); 
				break;
			case ORDER:
				outgoingData = reservationBLL.order(incomingData,user.getEmailAddress()); 
				break;
			case USER_CHANGE_PASSWORD:
				outgoingData= userBLL.changePassword(incomingData);
				break;
			
			default:				
		}

		return outgoingData;
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
					this.user = userBLL.getUserModel(user.getEmailAddress());
					Queue<String> keys = new LinkedList<>();
					Queue<String> values = new LinkedList<>();
					keys.add("user");
					values.add("{firstName:"+user.getFirstName()+",lastName:"+user.getLastName()+"}");
					authResponse = interpreter.encodeParametersToJson(ProtocolMessage.OK, keys, values);
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