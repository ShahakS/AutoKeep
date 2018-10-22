package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import server.DatabaseConnector;

public class VehicleDAL {
	private DatabaseConnector DBconnector;
	
	public VehicleDAL() {
		DBconnector = DatabaseConnector.getDbConnectorInstance();
	}
	
	public Queue<VehicleModel> searchAvailableVehicles(String startDate,String endDate,String vehicleType,int seatsNumber) throws SQLException{
		String query = "SELECT * FROM fn_GetAvailableVehiclesByParameters(?,?,?,?)";
		Queue<Object> parameters = new LinkedList<>();
		VehicleModel vehicle = null;

		parameters.add(startDate);
		parameters.add(endDate);
		parameters.add(vehicleType);
		parameters.add(seatsNumber);
			
		ResultSet resultSet = DBconnector.executeSqlStatementDataTable(query, parameters);
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
