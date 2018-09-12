package TestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient {
	
	public static void main(String[] args) {
		try {
			System.out.println("Step1");
			Socket clientSocket = new Socket("localhost", 40500);
			System.out.println("Step2");
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Step3");
			//ObjectOutputStream sendObjToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			//ObjectInputStream readObjFromServer = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Step4");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Im here");
	}
}
