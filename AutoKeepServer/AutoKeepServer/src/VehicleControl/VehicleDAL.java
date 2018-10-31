package VehicleControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Database.DatabaseConnector;

public class VehicleDAL {
	private DatabaseConnector DBconnector;
	
	public VehicleDAL() {
		DBconnector = DatabaseConnector.getDbConnectorInstance();
	}
	
	public Queue<VehicleModel> searchAvailableVehicles(String startDate,String endDate,String vehicleType,int seatsNumber) throws SQLException{
		Queue<Object> parameters = new LinkedList<>();
		VehicleModel vehicle = null;
		String query;
		
		parameters.add(startDate);
		parameters.add(endDate);
		
		if (vehicleType.equals("")){
			if(seatsNumber == -1) {
				query = "SELECT * FROM fn_GetAvailableVehiclesByDate(?,?)";
			}else{
				query = "SELECT * FROM fn_GetAvailableVehiclesByDateAndSeatsNumber(?,?,?)";
				parameters.add(seatsNumber);
			}	
		}else {
			if(seatsNumber == -1) {
				query = "SELECT * FROM fn_GetAvailableVehiclesByDateAndVehicleType(?,?,?)";
				parameters.add(vehicleType);
			}else{
				query = "SELECT * FROM fn_GetAvailableVehiclesByParameters(?,?,?,?)";
				parameters.add(vehicleType);
				parameters.add(seatsNumber);
			}	
		}			
		ResultSet resultSet = DBconnector.executeSqlStatementResultSet(query, parameters);
		Queue<VehicleModel> vehicles = new LinkedList<>();		
		
		while (resultSet.next()){
			String plateNumber = resultSet.getString("PlateNumber");
			String manufactureName = resultSet.getString("ManufactureName");
			String model = resultSet.getString("Model");
			String vehicleType1 = resultSet.getString("VehicleType");
			int manufactureYear = resultSet.getInt("ManufactureYear");
			int seatsNumber1 = resultSet.getInt("SeatsNumber");
			int engineCapacity = resultSet.getInt("EngineCapacity");
			boolean isUsable = resultSet.getBoolean("IsUsable");
			int kilometers = resultSet.getInt("Kilometers");
			String vehicleImage = resultSet.getString("VehicleImage");
			vehicle = new VehicleModel(plateNumber, manufactureName, model, vehicleType1, manufactureYear, seatsNumber1, engineCapacity, isUsable, kilometers, vehicleImage);
			vehicles.add(vehicle);
		}				
		return vehicles;
	}
}
