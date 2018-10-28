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
		String outgoingData = null;
			
		switch(interpreter.getProtocolMsg(incomingData)) {
			case SEARCH_VEHICLE:
				outgoingData = searchVehicle(incomingData); 
				break;
			case NEW_ORDER:
				outgoingData = makeNewOrder(incomingData); 
				break;
			case USER_CHANGE_PASSWORD:
				outgoingData= changePassword(incomingData);
				break;
			
			default:
				
		}

		return outgoingData;
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
		String outgoingData;
		
		try {
			ReservationModel reservation = (ReservationModel)interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);

			Queue<VehicleModel> vehiclesResults = vehicleDAL.searchAvailableVehicles(reservation.getReservationStartDate(),
					reservation.getReservationEndDate(),
					reservation.getVehicle().getVehicleType(),
					reservation.getVehicle().getSeatsNumber());
		
			if (vehiclesResults == null) {
				ProtocolMessage protocolMessage = ProtocolMessage.NO_AVAILABLE_VEHICLES;
				String message = ProtocolMessage.getMessage(protocolMessage);
				outgoingData = interpreter.encodeObjToJson(protocolMessage, message);
			}
			else {
				ProtocolMessage protocolMessage = ProtocolMessage.VEHICLE_MODEL_LIST;
				outgoingData = interpreter.encodeObjToJson(protocolMessage, vehiclesResults);
			}
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown out from SEARCH_VEHICLE case", e.getMessage(), e.getStackTrace().toString());
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}	
		return outgoingData;
	}
	
	private String makeNewOrder(String incomingData) {
		VehicleDAL vehicleDAL = new VehicleDAL();
		String protocolMsg;
		
		return null;
	}
}