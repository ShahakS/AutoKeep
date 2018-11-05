package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationBLL;
import UserControl.UserBLL;
import VehicleControl.VehicleBLL;
import exceptionsPackage.ExcaptionHandler;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	private UserBLL userBLL;
	private VehicleBLL vehicleBLL;
	private ReservationBLL reservationBLL;
	public static final int connectionRetries = 5;
	
	public ClientHandler(Socket socket) throws IOException{
		System.out.println("Connected");
		this.userBLL = new UserBLL();
		this.vehicleBLL = new VehicleBLL();
		this.reservationBLL = new ReservationBLL();
		this.clientSocket = socket;
		this.interpreter = new CommunicationInterpreter();
		this.readClientData = new ObjectInputStream(clientSocket.getInputStream());
		this.sendClientData = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	public void run() {		
		boolean isConnected = true;
		boolean isAuthenticated = connect();
		
		if (isAuthenticated) {	
			if(userBLL.user.IsAdministrator()) {
				while(isConnected) {
					try {
						String incomingData = (String) readClientData();
						String OutgoingData = adminBusinessLogicFlow(incomingData);	
						System.out.println(incomingData+"\n\n"+OutgoingData);
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
			}else {
				while(isConnected) {
					try {
						String incomingData = (String) readClientData();
						String OutgoingData = userBusinessLogicFlow(incomingData);	
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
		}
		userBLL.disconnect(clientSocket);
		System.out.println("Disconnected");
	}

	public String adminBusinessLogicFlow(String incomingData) {
		String outgoingData = null;
			
		switch(interpreter.getProtocolMsg(incomingData)) {
			case USERS_LIST:
				outgoingData = userBLL.getUsers();
				break;
				
			case CREATE_NEW_USER:
				outgoingData = userBLL.creatNewUser(incomingData);
				break;
				
			case DELETE_USER:
				outgoingData = userBLL.deleteUser(incomingData);
				break;
				
			case UPDATE_USER:
				outgoingData = userBLL.updateUser(incomingData);
				break;
				
			default:
				outgoingData = userBusinessLogicFlow(incomingData);
				break;
		}
		return outgoingData;
	}	
	
	public String userBusinessLogicFlow(String incomingData) {
		String outgoingData = null;
			
		switch(interpreter.getProtocolMsg(incomingData)) {
			case SEARCH_VEHICLE:
				outgoingData = vehicleBLL.searchVehicle(incomingData); 
				break;
			case ORDER:
				outgoingData = reservationBLL.order(incomingData,userBLL.user.getEmailAddress()); 
				break;
			case USER_CHANGE_PASSWORD:
				outgoingData= userBLL.changePassword(incomingData);
				break;
			case RESERVATION_HISTORY:
				outgoingData= reservationBLL.getHistory(userBLL.user.getEmailAddress());
				break;
			default:
				String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
				outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}
		return outgoingData;
	}	
	
	/**
	 * Connect the user to the application with the given credentials
	 * @return true if credential is passed else returns false
	 */
	private boolean connect() {
		boolean isAuthenticated = false;
		String isBanned = userBLL.isBanned(clientSocket);
		
		if(isBanned != null) //null = user isn't banned
			sendObjToClient(isBanned);
		else {
			for(int numOfRetries = connectionRetries;!isAuthenticated && numOfRetries > 0;numOfRetries--) {
				try {
					String clientCredential = readClientData();
					String outputData = userBLL.authenticate(clientCredential, clientSocket);
					sendObjToClient(outputData);
					
					ProtocolMessage protocolMessage = interpreter.getProtocolMsg(outputData);
					
					if (protocolMessage == ProtocolMessage.OK) {
						isAuthenticated = true;
					}					
					else if(protocolMessage == ProtocolMessage.USER_ALREADY_CONNECTED)
						return false;
					
				} catch (ClassNotFoundException e) {
					new ExcaptionHandler("Exception connecting to server. Thrown by connect()", e);
					String errorMsg = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
					String errorJsonString = interpreter.encodeObjToJson(ProtocolMessage.ERROR,errorMsg);
					sendObjToClient(errorJsonString);				
				} catch (IOException e) {
					//Connection closed by the client
					return false;
				}
			}
		}
		return isAuthenticated;
	}
	
	/**
	 * Send a String object to the Client
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