package VehicleControl;

import java.sql.SQLException;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import ReservationControl.ReservationModel;
import exceptionsPackage.ExcaptionHandler;

public class VehicleBLL {
	private CommunicationInterpreter interpreter;
	private VehicleDAL vehicleDAL;
	
	public VehicleBLL() {
		this.interpreter = new CommunicationInterpreter();
		this.vehicleDAL = new VehicleDAL();
	}
	
	public String searchVehicle(String incomingData) {
		String outgoingData;
		
		try {
			ReservationModel reqReservation = (ReservationModel)interpreter.
					decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);
			
			Queue<VehicleModel> vehiclesResults = vehicleDAL.searchAvailableVehicles(
					reqReservation.getReservationStartDate(),
					reqReservation.getReservationEndDate(),
					reqReservation.getVehicle().getVehicleType(),
					reqReservation.getVehicle().getSeatsNumber());
		
			if (vehiclesResults.isEmpty()) {
				ProtocolMessage protocolMessage = ProtocolMessage.NO_AVAILABLE_VEHICLES;
				String message = ProtocolMessage.getMessage(protocolMessage);
				outgoingData = interpreter.encodeObjToJson(protocolMessage, message);
			}
			else {
				ProtocolMessage protocolMessage = ProtocolMessage.VEHICLE_MODEL_LIST;
				outgoingData = interpreter.encodeObjToJson(protocolMessage, vehiclesResults);
			}
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown out from SEARCH_VEHICLE case", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}	
		return outgoingData;
	}
	
}
