package server;

import java.sql.SQLException;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationModel;
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
			
			default:
				
		}

		return serverReply;
	}

	private String makeNewOrder(String incomingData) {
		VehicleDAL vehicleDAL = new VehicleDAL();
		String protocolMsg;
		
		return null;
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
}