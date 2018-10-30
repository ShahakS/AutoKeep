package ReservationControl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Database.DatabaseConnector;

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
			int returnedValue = DBconnector.callRoutineReturnedScalarValue(query, parameters);
			
			if (returnedValue != 0)
				isBooked = true;
			
			return isBooked;
	}
}
