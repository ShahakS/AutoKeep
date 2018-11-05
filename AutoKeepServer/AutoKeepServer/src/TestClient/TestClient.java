package TestClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationModel;
import UserControl.UserModel;
import VehicleControl.VehicleModel;
//test
public class TestClient {
	
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ObjectInputStream readClientData = null;
		ObjectOutputStream sendClientData = null;
		
		try {

			Socket clientSocket = new Socket("shahak18.ddns.net", 40500);
			
			sendClientData = new ObjectOutputStream(clientSocket.getOutputStream());
			readClientData = new ObjectInputStream(clientSocket.getInputStream());
			
			CommunicationInterpreter c = new CommunicationInterpreter();
			String serverAnswer;
			//int numOfRetries = 5;
			boolean isConnected = false;
			String email = "shahak.shaked@gmail.com";
			//connect
			do{
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("user");
				values.add("{emailAddress:"+email+",password:\"@m1234\"}");
				String str = c.encodeParametersToJson(ProtocolMessage.LOGIN, keys, values);
				sendClientData.writeObject(str);
				serverAnswer = (String) readClientData.readObject();
				System.out.println(c.getProtocolMsg(serverAnswer));
				if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.OK) {
					isConnected = true;
					UserModel user = (UserModel) c.decodeFromJsonToObj(ProtocolMessage.USER_MODEL, serverAnswer);
					System.out.println("Hello "+user.getFirstName()+" "+user.getLastName());
				}else if(c.getProtocolMsg(serverAnswer) == ProtocolMessage.USER_IS_BANNED) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.USER_IS_BANNED, serverAnswer));
					break;
				}else if(c.getProtocolMsg(serverAnswer) == ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.TOO_MANY_AUTHENTICATION_RETRIES, serverAnswer));
				}else if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.USER_ALREADY_CONNECTED) {
					break;
				}
				
				//numOfRetries--;
			}while((!(c.getProtocolMsg(serverAnswer)).equals(ProtocolMessage.OK)));
			
			if (isConnected) {
				{
				//change pass
				Queue<String> keys = new LinkedList<>();
				Queue<String> values = new LinkedList<>();
				keys.add("user");
				values.add("{emailAddress:"+email+",password:\"@m1234\"}");
				String str = c.encodeParametersToJson(ProtocolMessage.USER_CHANGE_PASSWORD, keys, values);
				sendClientData.writeObject(str);
				serverAnswer = (String) readClientData.readObject();
				System.out.println(c.getProtocolMsg(serverAnswer));
				if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.PASSWORD_CHANGED_SUCCESSFULLY) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.CHANGING_PASSWORD_FAILED, serverAnswer));
				}else if(c.getProtocolMsg(serverAnswer) == ProtocolMessage.CHANGING_PASSWORD_FAILED) {
					System.out.println(c.decodeFromJsonToObj(ProtocolMessage.CHANGING_PASSWORD_FAILED, serverAnswer));
				}
				
				}
				{
				//get available vehicles
				UserModel user = new UserModel(10011,"sad", "", "", "", "", "", true);
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
				//make an order
				{
					Queue<String> keys = new LinkedList<>();
					Queue<String> values = new LinkedList<>();
					UserModel user = new UserModel(234567,"sad", "", "", "", "", "", true);
					VehicleModel vehicle = new VehicleModel("68-345-79", "", "", "FAMILY", 6, 5, 1600, true, 213033, "");
					ReservationModel r = new ReservationModel(1, user, vehicle, "2018-09-09", "2018-11-09", "2018-11-09");
					keys.add("reservation");
					values.add("{reservationStartDate:"+r.getReservationStartDate()
							+",reservationEndDate:"+r.getReservationStartDate()
							+",vehicle:{plateNumber:"+vehicle.getPlateNumber()+"}}");				
					
					String search = c.encodeParametersToJson(ProtocolMessage.ORDER, keys, values);

					sendClientData.writeObject(search);
					
					serverAnswer = (String) readClientData.readObject();
				
					System.out.println(c.decodeFromJsonToObj(c.getProtocolMsg(serverAnswer), serverAnswer));
					
				}
				{
					String str = c.setProtocolMsg(ProtocolMessage.RESERVATION_HISTORY);

					sendClientData.writeObject(str);
					
					serverAnswer = (String) readClientData.readObject();
					if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.HISTORY_RESULT) {
						@SuppressWarnings("unchecked")
						Queue<ReservationModel> res = (Queue<ReservationModel>) c.decodeFromJsonToObj
								(ProtocolMessage.RESERVATION_MODEL_LIST, serverAnswer);
					while(!res.isEmpty())
						System.out.println(res.poll().getReservationID());					
					}else if (c.getProtocolMsg(serverAnswer) == ProtocolMessage.NO_HISTORY) {
						System.out.println(c.decodeFromJsonToObj(ProtocolMessage.NO_HISTORY, serverAnswer));
					}else {
						System.out.println(c.decodeFromJsonToObj(ProtocolMessage.ERROR, serverAnswer));
					}
					//Thread.currentThread().sleep(1000000);
				}
			}
			
			clientSocket.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
