package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import classes.CommunicationInterpreter;
import classes.ErrorLog;
import classes.ProtocolMessage;
import classes.UserDAL;
import classes.UserModel;

public class ClientHandler implements Runnable{
	private Socket clientSocket;
	private DatabaseConnector db;
	private CommunicationInterpreter interpreter;
	private ObjectInputStream readClientData;
	private ObjectOutputStream sendClientData;
	
	public ClientHandler(Socket socket){
		this.clientSocket = socket;
		this.db = DatabaseConnector.getDbConnectorInstance();
		this.interpreter = new CommunicationInterpreter();
		try {
			this.readClientData = new ObjectInputStream(socket.getInputStream());
			this.sendClientData = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			ErrorLog error = new ErrorLog("Error while creating input/output stream",e.getMessage(),e.getStackTrace().toString());
			error.writeToErrorLog();
		}		
	}
	
	public void run() {
		System.out.println("Connected");
		String clientData=null;//,serverData = ProtocolMessage.OK;
		
		try {
			clientData = (String) readClientData.readObject();
			//System.out.println(clientData);
			//sendClientData.writeObject(serverData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bll(clientData);
	}
	
	public String bll(String clientResponse) {
		switch(interpreter.getProtocolMsg(clientResponse)) {
		case LOGIN:
			login((UserModel)interpreter.decodeFromJsonToObj(clientResponse));	
			//send
			break;
		}
		return clientResponse;
	}

	private boolean login(UserModel user) {
		boolean isCredentialValid = false;
		ProtocolMessage protocolMessage;
		UserDAL userDAL = new UserDAL();
		
		try {
			 isCredentialValid = userDAL.isUserCredentialValid(user);
			 
			 if (isCredentialValid) {
				protocolMessage = ProtocolMessage.OK;
				//UserModel x= userDAL.getUser(user.getUserName());
			}
			 else {
				 protocolMessage = ProtocolMessage.WRONG_CREDENTIAL;
			 }
		} catch (SQLException e) {
			protocolMessage = ProtocolMessage.ERROR;
		}
		return isCredentialValid; 
	}
}
