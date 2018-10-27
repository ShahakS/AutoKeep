package VehicleControl;

import java.sql.SQLException;
import java.util.Queue;

import CommunicationManager.CommunicationInterpreter;

public class VehicleBLL {
	private CommunicationInterpreter interpreter;
	private VehicleDAL vehicleDAL;
	
	public VehicleBLL() {
		interpreter = new CommunicationInterpreter();
		vehicleDAL = new VehicleDAL();
	}

	public Queue<VehicleModel> searchVehicles(VehicleModel vehicle,String startDate,String endDate) throws SQLException {
		String vehicleType = vehicle.getVehicleType();
		int seatsNumber = vehicle.getSeatsNumber();
		Queue <VehicleModel> vehicles = vehicleDAL.searchAvailableVehicles(startDate, endDate, vehicleType, seatsNumber);
		
		return vehicles;
	}
	
	
}
