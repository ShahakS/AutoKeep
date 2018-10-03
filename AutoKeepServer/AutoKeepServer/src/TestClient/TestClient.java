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

public class TestClient {
	
	public static void main(String[] args) {
		try {
			System.out.println("Step1");
			Socket clientSocket = new Socket("localhost", 40500);
			System.out.println("Step2");
//			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Step3");
//			ObjectOutputStream sendObjToServer = new ObjectOutputStream(clientSocket.getOutputStream());
//			ObjectInputStream readObjFromServer = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Step4");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		

//		CommunicationInterpreter c = new CommunicationInterpreter();
//		
//		Queue<UserModel> test = new LinkedList<>();
//		test.add(new UserModel("ab", "123", "123", "123", "333", "222", "111", true));
//		test.add(new UserModel("qwe", "123", "123", "123", "333", "222", "111", true));
//		try {
//		String str = c.encodeToJson(ProtocolMessage.USER_MODEL_LIST, test);
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
