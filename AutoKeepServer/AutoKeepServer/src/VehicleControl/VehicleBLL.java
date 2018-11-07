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

	public String getVehicles() {
		String outputData;
		
		try {
			Queue<VehicleModel> vehiclesList = vehicleDAL.getAllVehicles();
			if (vehiclesList.isEmpty()) {
				ProtocolMessage protocolMsg = ProtocolMessage.NO_VEHICLES;
				outputData = interpreter.encodeObjToJson(protocolMsg, ProtocolMessage.getMessage(protocolMsg));
			}else
				outputData = interpreter.encodeObjToJson(ProtocolMessage.VEHICLE_MODEL_LIST, vehiclesList);
			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception getting users list.Thrown by getVehicles()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}
	
	public String deleteVehicle(String incomingData) {
		String outputData;
		
		try {
			VehicleModel vehicle = (VehicleModel) interpreter.decodeFromJsonToObj(ProtocolMessage.VEHICLE_MODEL, incomingData);
			vehicleDAL.deleteVehicleByPlateNumber(vehicle.getPlateNumber());
			
			String message = ProtocolMessage.getMessage(ProtocolMessage.VEHICLE_DELETED_SUCCESSFULLY,vehicle.getPlateNumber());
			outputData = interpreter.encodeObjToJson(ProtocolMessage.VEHICLE_DELETED_SUCCESSFULLY,message);			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception deleting user.Thrown by deleteVehicle()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}

	public String updateVehicle(String incomingData) {
		String outputData;
		ProtocolMessage protocolMsg;
		try {
			VehicleModel vehicle = (VehicleModel) interpreter.decodeFromJsonToObj(ProtocolMessage.VEHICLE_MODEL, incomingData);
			boolean isUpdated = vehicleDAL.updateVehicle(vehicle);
			
			if (isUpdated)
				protocolMsg = ProtocolMessage.VEHICLE_UPDATED_SUCCESSFULLY;
			else
				protocolMsg = ProtocolMessage.VEHICLE_UPDATE_FAILED;
			
			outputData = interpreter.encodeObjToJson(protocolMsg,ProtocolMessage.getMessage(protocolMsg,vehicle.getPlateNumber()));			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception Updating user.Thrown by updateVehicle()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}

	public String creatNewVehicle(String incomingData) {
	String outputData;
	ProtocolMessage protocolMsg;
	
		try {
			VehicleModel newVehicle = (VehicleModel) interpreter.decodeFromJsonToObj(ProtocolMessage.VEHICLE_MODEL, incomingData);
			boolean isSucceeded = vehicleDAL.insertNewVehicle(newVehicle);
			
			if (isSucceeded)
				protocolMsg = ProtocolMessage.VEHICLE_CREATED_SUCCESSFULLY;
			else 
				protocolMsg = ProtocolMessage.VEHICLE_ALREADY_EXIST;

			outputData = interpreter.encodeObjToJson(protocolMsg,ProtocolMessage.getMessage(protocolMsg,newVehicle.getPlateNumber()));
		} catch (SQLException e) {
			new ExcaptionHandler("Exception Creating a new user.Thrown by creatNewUser()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}
	
}
