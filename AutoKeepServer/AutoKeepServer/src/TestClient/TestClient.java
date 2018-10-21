package TestClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import classes.CommunicationInterpreter;
import classes.ProtocolMessage;
import classes.UserModel;
//test
public class TestClient {
	
	public static void main(String[] args) {
		ObjectInputStream readClientData = null;
		ObjectOutputStream sendClientData = null;
		
		try {
			@SuppressWarnings("resource")
			Socket clientSocket = new Socket("localhost", 40511);
			
			sendClientData = new ObjectOutputStream(clientSocket.getOutputStream());
			readClientData = new ObjectInputStream(clientSocket.getInputStream());
			
			CommunicationInterpreter c = new CommunicationInterpreter();
			String serverAnswer;
			int numOfRetries = 4;
			
			do{
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("user");
				values.add("{emailAddress:"+"shahak.shsaked@gmail.com"+",password:\"@m1234\"}");
				
				String str = c.encodeParametersToJson(ProtocolMessage.LOGIN, keys, values);
				sendClientData.writeObject(str);
				serverAnswer = (String) readClientData.readObject();
				System.out.println(c.getProtocolMsg(serverAnswer));
				numOfRetries--;
			}while((!(c.getProtocolMsg(serverAnswer)).equals(ProtocolMessage.OK)) && numOfRetries>0);
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
