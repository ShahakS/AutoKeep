package TestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import classes.CommunicationInterpreter;
import classes.ErrorLog;
import classes.ProtocolMessage;
import classes.ReservationModel;
import classes.UserDAL;
import classes.UserModel;

import java.lang.reflect.Type;
//test
public class TestClient {
	
	public static void main(String[] args) {
		ObjectInputStream readObjFromClient = null;
		ObjectOutputStream sendObjToClient = null;
		
		try {
			Socket clientSocket = new Socket("localhost", 40500);
			//readObjFromClient = new ObjectInputStream(clientSocket.getInputStream());
			//sendObjToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			//{"protocolMsg":"USER_MODEL","user":{"userName":"qwe","password":"123"}}
			
			UserModel user1 = new UserModel("qwe", "123", "123", "123", "333", "222", "111", true);
			
			CommunicationInterpreter c = new CommunicationInterpreter();
			Queue<String> keys = new LinkedList<>();
			Queue<String> values = new LinkedList<>();
			keys.add("user");
			keys.add("cars");
			values.add("{userName:\"sshaked\",password:\"123\"}");
			values.add("{userName:\"sshaked\",password:\"123\"}");
			
			String str = c.encodeParametersToJson(ProtocolMessage.USER_MODEL, keys, values);
			
			//sending
			
			UserModel user=  (UserModel) c.decodeFromJsonToObj(str);
			System.out.println(user.getUserName()+" "+user.getDateOfBirth()+"\n"+str);
			
			
			//sendObjToClient.writeObject();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Queue<UserModel> test = new LinkedList<>();
//		test.add(new UserModel("ab", "123", "123", "123", "333", "222", "111", true));
//		test.add(new UserModel("qwe", "123", "123", "123", "333", "222", "111", true));
//		try {
//		
//		Queue<UserModel> users=  (Queue<UserModel>) c.decodeFromJson(str);
//		
//		for (UserModel user:  users) {
//			System.out.println(user.getUserName());
//		}
//		
//		}catch(ClassCastException e) {
//			System.out.println("ERROR");
//		}		
	}
}
