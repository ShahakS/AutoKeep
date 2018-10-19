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
		ClientHandlerBLL clientHandlerBLL = new ClientHandlerBLL();
		boolean isAuthenticated = false;
		
		clientHandlerBLL.connect();
		
		
		if (isAuthenticated) {
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
	
	public String bll(String clientResponse) {
		String answer = null;
		
		switch(interpreter.getProtocolMsg(clientResponse)) {
			case LOGIN:
						
			break;
			
			default:
				
		}
		return answer;
	}

	
}