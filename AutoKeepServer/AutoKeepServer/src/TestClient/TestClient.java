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
import java.lang.reflect.Type;
import Classes.UserModel;

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
//		System.out.println("Im here");
		
		Gson gson = new GsonBuilder().create();
		ArrayList<UserModel> users = new ArrayList<>();
		users.add(new UserModel("ab", "123", "123", "123", "333", "222", "111", true));
		users.add(new UserModel("as", "123", "123", "123", "333", "222", "111", false));
		String jsonStrings = gson.toJson(users);
		
		//send to the other side
		System.out.println(jsonStrings);
		
		Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
		//System.out.println(listType);
		ArrayList<Object> users2 = gson.fromJson(jsonStrings,listType);
		
		if (users2.get(0) instanceof UserModel) {
			UserModel x = (UserModel)users2.get(0);
			System.out.println(x.getEmailAddress());
		}
		//ArrayList<UserMODEL> users2 = gson.fromJson(jsonStrings,listType);
		//System.out.println(users2.get(0).getDateOfBirth());
		
	
	
	}
}
