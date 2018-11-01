package ReservationControl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Database.DatabaseConnector;
import VehicleControl.VehicleModel;

public class ReservationDAL {
	private DatabaseConnector DBconnector;
	
	public ReservationDAL() {
		DBconnector = DatabaseConnector.getDbConnectorInstance();
	}
	
	public boolean order(String plateNumber,String emailAddress,String startingDate,String endingDate) throws SQLException {
			String query = "{?= call sp_InsertNewReservation(?, ?, ?, ?)}";
			Queue<Object> parameters = new LinkedList<>();
			boolean isBooked = false;

			parameters.add(plateNumber);
			parameters.add(emailAddress);
			parameters.add(startingDate);
			parameters.add(endingDate);
			isBooked = DBconnector.callRoutineReturnedBooleanScalarValue(query, parameters);			
			return isBooked;
	}
	
	public boolean isThereAnActiveReservation(String emailAddress) throws SQLException {
		String query = "{?= call fn_IsThereAnActiveOrder(?)}";
		boolean IsThereAnActiveOrder;
		Queue<Object> parameters = new LinkedList<>();

		parameters.add(emailAddress);
		IsThereAnActiveOrder = DBconnector.callRoutineReturnedBooleanScalarValue(query, parameters);
		
		return IsThereAnActiveOrder;
	}
	
	public Queue<ReservationModel> getReservationHistory(String emailAddress) throws SQLException {
		String query = "SELECT * FROM fn_GetLastMonthOrdersByEmailAddress(?)";
		Queue<Object> parameters = new LinkedList<>();
		Queue<ReservationModel> reservations = new LinkedList<>();
		
		parameters.add(emailAddress);			
		ResultSet resultSet = DBconnector.executeSqlStatementResultSet(query, parameters);
		
		while (resultSet.next()){
			VehicleModel vehicle = new VehicleModel();
			ReservationModel reservation = new ReservationModel();
			
			vehicle.setPlateNumber(resultSet.getString("PlateNumber"));
			vehicle.setManufactureName(resultSet.getString("ManufactureName"));
			vehicle.setModel(resultSet.getString("Model"));
			vehicle.setManufactureYear(resultSet.getInt("ManufactureYear"));
			vehicle.setSeatsNumber(resultSet.getInt("SeatsNumber"));
			vehicle.setEngineCapacity(resultSet.getInt("EngineCapacity"));
			vehicle.setIsUsable(resultSet.getBoolean("IsUsable"));
			vehicle.setKilometers(resultSet.getInt("Kilometers"));
			vehicle.setVehicleImage(resultSet.getString("VehicleImage"));
			vehicle.setVehicleType(resultSet.getString("VehicleType"));
			
			reservation.setReservationID(resultSet.getInt("ReservationID"));
			reservation.setReservationDate(resultSet.getString("OrderTime"));
			reservation.setReservationStartDate(resultSet.getString("StartingDate"));
			reservation.setReservationEndDate(resultSet.getString("EndingDate"));
			reservation.setVehicle(vehicle);
			reservations.add(reservation);			
		}				
		return reservations;
	}
}
