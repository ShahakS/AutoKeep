package server;

import java.sql.SQLException;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationModel;
import UserControl.UserBLL;
import UserControl.UserModel;
import VehicleControl.VehicleDAL;
import VehicleControl.VehicleModel;
import exceptionsPackage.ExcaptionHandler;

public class ClientHandlerBLL {
	private CommunicationInterpreter interpreter;
	
	public ClientHandlerBLL() {
		interpreter = new CommunicationInterpreter();
	}

	public String bll(String incomingData) {
		String serverReply = null;
			
		switch(interpreter.getProtocolMsg(incomingData)) {
			case SEARCH_VEHICLE:
				serverReply = searchVehicle(incomingData); 
				break;
			case NEW_ORDER:
				serverReply = makeNewOrder(incomingData); 
				break;
			case USER_CHANGE_PASSWORD:
				serverReply= changePassword(incomingData);
				break;
			
			default:
				
		}

		return serverReply;
	}

	private String changePassword(String incomingData) {
		UserBLL userBLL = new UserBLL();
		String outgoingData,message;
		
		try {
			UserModel user = (UserModel)interpreter.decodeFromJsonToObj(ProtocolMessage.USER_MODEL, incomingData);
			userBLL.changePassword(user);
			ProtocolMessage protocolMsg = ProtocolMessage.PASSWORD_CHANGED_SUCCESSFULLY;
			message = ProtocolMessage.getMessage(protocolMsg);
			outgoingData = interpreter.encodeObjToJson(protocolMsg, message);
		} catch (SQLException e) {
			message = ProtocolMessage.getMessage(ProtocolMessage.CHANGING_PASSWORD_FAILED);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}
		return outgoingData;
	}

	private String searchVehicle(String incomingData) {
		VehicleDAL vehicleDAL = new VehicleDAL();
		String protocolMsg;
		
		try {
			ReservationModel reservation = (ReservationModel)interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);

			Queue<VehicleModel> vehiclesResults = vehicleDAL.searchAvailableVehicles(reservation.getReservationStartDate(),
					reservation.getReservationEndDate(),
					reservation.getVehicle().getVehicleType(),
					reservation.getVehicle().getSeatsNumber());
		
			if (vehiclesResults == null) {
				ProtocolMessage protocolMessage = ProtocolMessage.NO_AVAILABLE_VEHICLES;
				String message = ProtocolMessage.getMessage(protocolMessage);
				protocolMsg = interpreter.encodeObjToJson(protocolMessage, message);
			}
			else {
				ProtocolMessage protocolMessage = ProtocolMessage.VEHICLE_MODEL_LIST;
				protocolMsg = interpreter.encodeObjToJson(protocolMessage, vehiclesResults);
			}
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown out from SEARCH_VEHICLE case", e.getMessage(), e.getStackTrace().toString());
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			protocolMsg = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}	
		return protocolMsg;
	}
	
	private String makeNewOrder(String incomingData) {
		VehicleDAL vehicleDAL = new VehicleDAL();
		String protocolMsg;
		
		return null;
	}
}