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
		String outgoingData;
		
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

	public String getReservations() {
		String outputData;
		
		try {
			Queue<ReservationModel> reservationsList = reservationDAL.getLastMonthReservations();
			
			if (reservationsList.isEmpty())
				outputData = interpreter.encodeObjToJson(ProtocolMessage.NO_RESERVATIONS, ProtocolMessage.getMessage(ProtocolMessage.NO_RESERVATIONS));
			else			
				outputData = interpreter.encodeObjToJson(ProtocolMessage.RESERVATION_MODEL_LIST, reservationsList);
			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception getting reservation list.Thrown by getReservations()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}

	public String createNewReservation(String incomingData) {
		ReservationModel reservation = (ReservationModel) interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);
		String outputData = order(incomingData, reservation.getUser().getEmailAddress());
		return outputData;
	}

	public String updateExistingReservation(String incomingData) {
		String outputData;
		ProtocolMessage protocolMsg;
		
		try {
			ReservationModel reservation = (ReservationModel)interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);
			int reservationID = reservation.getReservationID();
			int userID = reservation.getUser().getUserID();
			String plateNumber = reservation.getVehicle().getPlateNumber();
			String startDate = reservation.getReservationStartDate();
			String endDate = reservation.getReservationEndDate();			
			
			boolean isUpdated = reservationDAL.updateReservation(reservationID,userID,plateNumber,startDate,endDate);
			
			if (isUpdated)
				protocolMsg = ProtocolMessage.RESERVATION_UPDATED_SUCCESSFULLY;
			else			
				protocolMsg = ProtocolMessage.RESERVATION_UPDATE_FAILED;
			
			outputData = interpreter.encodeObjToJson(protocolMsg, ProtocolMessage.getMessage(protocolMsg,String.valueOf(reservationID)));			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception updating reservation.Thrown by updateExistingReservation()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}

	public String cancelReservation(String incomingData) {
		String outputData;
		ProtocolMessage protocolMsg;
		
		try {
			ReservationModel reservation = (ReservationModel)interpreter.decodeFromJsonToObj(ProtocolMessage.RESERVATION_MODEL, incomingData);
			int reservationID = reservation.getReservationID();		
			
			boolean isCanceled = reservationDAL.cancelReservation(reservationID,2);
			
			if (isCanceled)
				protocolMsg = ProtocolMessage.RESERVATION_CANCELED_SUCCESSFULLY;
			else			
				protocolMsg = ProtocolMessage.RESERVATION_CANCELED_FAILED;
			
			outputData = interpreter.encodeObjToJson(protocolMsg, ProtocolMessage.getMessage(protocolMsg,String.valueOf(reservationID)));			
		} catch (SQLException e) {
			new ExcaptionHandler("Exception canceling reservation.Thrown by cancelReservation()", e);
			String message = ProtocolMessage.getMessage(ProtocolMessage.INTERNAL_ERROR);
			outputData = interpreter.encodeObjToJson(ProtocolMessage.ERROR,message);	
		}
		return outputData;
	}
}
