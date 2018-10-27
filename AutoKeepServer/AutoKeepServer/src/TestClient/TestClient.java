package TestClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import classes.CommunicationInterpreter;
import classes.ProtocolMessage;
import classes.ReservationModel;
import classes.UserModel;
import classes.VehicleModel;
//test
public class TestClient {
	
	public static void main(String[] args) throws InterruptedException {
		ObjectInputStream readClientData = null;
		ObjectOutputStream sendClientData = null;
		
		try {

			Socket clientSocket = new Socket("localhost", 40501);
			
			sendClientData = new ObjectOutputStream(clientSocket.getOutputStream());
			readClientData = new ObjectInputStream(clientSocket.getInputStream());
			
			CommunicationInterpreter c = new CommunicationInterpreter();
			String serverAnswer;
			int numOfRetries = 5;
			boolean isConnected = false;
			String email = "shahak.shaked@gmail.com1";
			do{
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("user");
				values.add("{emailAddress:"+email+",password:\"@m1234\"}");
				String str = c.encodeParametersToJson(ProtocolMessage.LOGIN, keys, values);
				sendClientData.writeObject(str);
				serverAnswer = (String) readClientData.readObject();
				
				if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.OK) {
					isConnected = true;
					System.out.println(c.getProtocolMsg(serverAnswer));
				}else if(c.getProtocolMsg(serverAnswer) == ProtocolMessage.USER_IS_BANNED) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.USER_IS_BANNED, serverAnswer));
					break;
				}else if(c.getProtocolMsg(serverAnswer) == ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES, serverAnswer));
				}
				System.out.println(c.getProtocolMsg(serverAnswer));
				numOfRetries--;
			}while((!(c.getProtocolMsg(serverAnswer)).equals(ProtocolMessage.OK)) && numOfRetries>0);
			
			if (isConnected) {
				UserModel user = new UserModel("sad", "", "", "", "", "", true);
				VehicleModel vehicle = new VehicleModel("1", "", "", "FAMILY", 6, 5, 1600, true, 213033, "");
				ReservationModel r = new ReservationModel(1, user, vehicle, "2018-09-09", "2018-11-09", "2018-11-09");
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("reservation");
				values.add("{reservationStartDate:"+r.getReservationStartDate()
						+",reservationEndDate:"+r.getReservationStartDate()
						+",vehicle:{vehicleType:"+vehicle.getVehicleType()+",seatsNumber:"+vehicle.getSeatsNumber()+"}}");				
				String search = c.encodeParametersToJson(ProtocolMessage.SEARCH_VEHICLE, keys, values);

				sendClientData.writeObject(search);
				serverAnswer = (String) readClientData.readObject();
				@SuppressWarnings("unchecked")
				Queue<VehicleModel> list =(Queue<VehicleModel>)c.decodeFromJsonToObj(c.getProtocolMsg(serverAnswer), serverAnswer);
				while(!list.isEmpty())
					System.out.println(list.poll().getPlateNumber());
			}
			Thread.currentThread().sleep(2000);
			clientSocket.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
