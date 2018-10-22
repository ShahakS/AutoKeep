package TestClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

import classes.CommunicationInterpreter;
import classes.ProtocolMessage;
import classes.ReservationModel;
import classes.UserModel;
import classes.VehicleBLL;
import classes.VehicleModel;
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
			int numOfRetries = 5;
			boolean isConnected = false;
			
			do{
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("user");
				values.add("{emailAddress:"+"shahak.shaked@gmail.com"+",password:\"@m1234\"}");
				
				String str = c.encodeParametersToJson(ProtocolMessage.LOGIN, keys, values);
				sendClientData.writeObject(str);
				serverAnswer = (String) readClientData.readObject();
				
				if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.OK) {
					isConnected = true;
					System.out.println(c.getProtocolMsg(serverAnswer));
				}
				numOfRetries--;
			}while((!(c.getProtocolMsg(serverAnswer)).equals(ProtocolMessage.OK)) && numOfRetries>0);
			
			if (isConnected) {
				UserModel user = new UserModel("sad", "", "", "", "", "", true);
				VehicleModel vehicle = new VehicleModel("1", "", "", "FAMILY", 6, 5, 1600, true, 213033, "");
				ReservationModel r = new ReservationModel(1, user, vehicle, "2018-09-09 16:58:38.947", "2018-11-09 16:58:38.947", "2018-11-09 18:58:38.947");
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("reservation");
				values.add("{reservationStartDate:"+r.getReservationStartDate()+",reservationEndDate:"+r.getReservationStartDate()+"}");
				System.out.println(values.peek());
					//	",vehicle:"+vehicle+
						//",user:"+user+"}");
				//"{emailAddress:"+"shahak.shaked@gmail.com"+",password:\"@m1234\"}"
				
				//String search = c.encodeParametersToJson(ProtocolMessage.SEARCH_VEHICLE, keys, values);
			String test = c.encodeObjToJson(ProtocolMessage.SEARCH_VEHICLE, r);
					sendClientData.writeObject(test);
				serverAnswer = (String) readClientData.readObject();
				System.out.println(c.getProtocolMsg(serverAnswer));
				
			}
			
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
