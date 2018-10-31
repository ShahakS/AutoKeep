package ReservationControl;

import java.sql.SQLException;
import java.util.Queue;

import ClientServerProtocols.ProtocolMessage;
import CommunicationManager.CommunicationInterpreter;
import exceptionsPackage.ExcaptionHandler;

public class ReservationBLL {
	private ReservationDAL reservationDAL;
	private CommunicationInterpreter interpreter;
	
	public ReservationBLL() {
		this.reservationDAL =  new ReservationDAL();
		this.interpreter =  new CommunicationInterpreter();
	}
	
	public String order(String incomingData,String emailAddress) {
		String outgoingData = null;
		
		try {
			boolean isThereAnActiveReservation= reservationDAL.isThereAnActiveReservation(emailAddress);
			
			if (isThereAnActiveReservation) {
				String message = ProtocolMessage.getMessage(ProtocolMessage.ACTIVE_ORDER_ALREADY_EXISTS);
				outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ORDER_FAILED, message);
				return outgoingData;				
			}else {
				ReservationModel requiredReservation = (ReservationModel)interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);
				String startingDate = requiredReservation.getReservationStartDate();
				String endingDate = requiredReservation.getReservationEndDate();
				String plateNumber = requiredReservation.getVehicle().getPlateNumber();
				boolean isBookedSuccessfully = reservationDAL.order(plateNumber,emailAddress, startingDate, endingDate);		
				
				if (isBookedSuccessfully) {
					ProtocolMessage protocolMessage = ProtocolMessage.ORDER_BOOKED_SUCCESSFULLY;
					String message = ProtocolMessage.getMessage(protocolMessage);
					outgoingData = interpreter.encodeObjToJson(protocolMessage, message);
				}
				else {
					ProtocolMessage protocolMessage = ProtocolMessage.ORDER_FAILED;
					String message = ProtocolMessage.getMessage(protocolMessage);
					outgoingData = interpreter.encodeObjToJson(protocolMessage, message);
					//TODO CHECK IF ORDER IS ATOMIC
				}	
			}				
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown out from ReservationBLL.order()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}
		return outgoingData;
	}

	public String getHistory(String emailAddress) {
		String outgoingData;
		
		try {
			Queue<ReservationModel> reservationHistory = reservationDAL.getReservationHistory(emailAddress);
			
			if (reservationHistory.isEmpty()) {
				ProtocolMessage protocolMessage = ProtocolMessage.NO_HISTORY;
				String message = ProtocolMessage.getMessage(protocolMessage);
				outgoingData = interpreter.encodeObjToJson(protocolMessage, message);
			}
			else {
				outgoingData = interpreter.encodeObjToJson(ProtocolMessage.HISTORY_RESULT, reservationHistory);
			}
		} catch (SQLException e) {
			new ExcaptionHandler("Exception thrown out from ReservationBLL.getHistory()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outgoingData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);
		}		
		return outgoingData;
	}
}
