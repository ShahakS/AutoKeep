package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

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
		String clientCredential = null;
		boolean isAuthenticated = false;
		
		
		for(int numOfRetries = 3;!isAuthenticated && numOfRetries > 0;numOfRetries--) {
			try {
				clientCredential = readClientData();
				String answer = connect((UserModel)interpreter.decodeFromJsonToObj(clientCredential));
				
			} catch (ClassNotFoundException | IOException e) {
				interpreter.setProtocolMsg(answer)
			}
			sendObjToClient(answer);
		}
		
		while(isAuthenticated) {
			try {
				clientCredential = (String) readClientData.readObject();
				System.out.println(clientCredential);
				String jsonString = bll(clientCredential);
				sendObjToClient(jsonString);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Disconnected");
	}
	
	public String bll(String clientResponse) {
		String answer = null;
		
		switch(interpreter.getProtocolMsg(clientResponse)) {
		case LOGIN:
			answer = login((UserModel)interpreter.decodeFromJsonToObj(clientResponse));			
			break;
		}
		return answer;
	}
	
	private String connect(UserModel user) {
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
		
		 Queue<String> keys= new LinkedList<>();
		 Queue<String> values = new LinkedList<>();
		 keys.add("user");
		 values.add("{IsAdministrator:\"false\"}");
		
		return interpreter.encodeParametersToJson(protocolMessage,keys,values);
	}
	
	private void sendObjToClient(String jsonString) throws IOException {
		sendClientData.reset();
		sendClientData.writeObject(jsonString);	
	}
	
	private String readClientData() throws ClassNotFoundException, IOException {
		return (String) readClientData.readObject();
	}
}
