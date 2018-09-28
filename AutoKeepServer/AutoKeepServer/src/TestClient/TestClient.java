package TestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import classes.Error;
import classes.UserDAL;
import classes.UserModel;

import java.lang.reflect.Type;

public class TestClient {
	
	public static void main(String[] args) {
//		try {
//			System.out.println("Step1");
//			Socket clientSocket = new Socket("localhost", 40500);
//			System.out.println("Step2");
//			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//			System.out.println("Step3");
//			//ObjectOutputStream sendObjToServer = new ObjectOutputStream(clientSocket.getOutputStream());
//			//ObjectInputStream readObjFromServer = new ObjectInputStream(clientSocket.getInputStream());
//			System.out.println("Step4");
//		}catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		

		Gson gson = new GsonBuilder().create();
		JsonObject jsonRequest = new JsonObject();
		jsonRequest.addProperty("api", 20);
		jsonRequest.add("user", gson.toJsonTree(new UserModel("ab", "123", "123", "123", "333", "222", "111", true),UserModel.class));//parser1.parse(jsonString));
		
		System.out.println("CLIENT SIDE: "+ jsonRequest.toString());
		
		//send to server -> jsonRequest.toString()
		
		String clientRequest = jsonRequest.toString();
		
		JsonObject serverJsonRequest = new JsonObject();		
		JsonParser parser = new JsonParser();
		serverJsonRequest = (JsonObject)parser.parse(clientRequest);
		System.out.println("SERVER SIDE: "+serverJsonRequest.get("api"));
		
		if(serverJsonRequest.get("api").getAsInt() == 20) {
			UserModel user = gson.fromJson(serverJsonRequest.get("user").toString(), UserModel.class);
			System.out.println(user.getUserName());
		}
		
//		ArrayList<UserModel> users = new ArrayList<>();
//		users.add(new UserModel("ab", "123", "123", "123", "333", "222", "111", true));
//		users.add(new UserModel("as", "123", "123", "123", "333", "222", "111", false));
//		String jsonStrings = gson.toJson(users);
//		
//		//send to the other side	
//		Type listType = new TypeToken<ArrayList<UserModel>>(){}.getType();
//
//		ArrayList<UserModel> users2 = (ArrayList<UserModel>)gson.fromJson(jsonStrings,listType);
//		for(int i = 0;i < users2.size();i++) {
//			System.out.println(users2.get(i).getEmailAddress());
//		}
		
		
//		UserDAL dal = new UserDAL();
		//System.out.println(dal.isUserCredentialValid(new UserModel("ab", "123", "1989-12-27", "123", "333", "222", "111", true)));
		//dal.addUser(new UserModel("ab", "123", "1989-12-27", "123", "333", "222", "111", true));
		
	}
}
