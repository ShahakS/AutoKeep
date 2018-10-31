package ClientServerProtocols;

import SessionControl.SessionManager;

public enum ProtocolMessage {
	OK,
	WRONG_CREDENTIAL,
	LOGIN,
	USER_IS_BANNED,
	SEARCH_VEHICLE,
	NO_AVAILABLE_VEHICLES,
	ORDER,
	ORDER_FAILED,
	ORDER_BOOKED_SUCCESSFULLY,
	VIEW_ORDERS,
	ACTIVE_ORDER_ALREADY_EXISTS,
	USER_CHANGE_PASSWORD,
	PASSWORD_CHANGED_SUCCESSFULLY,
	CHANGING_PASSWORD_FAILED,
	USER_MODEL,
	RESERVATION_MODEL,
	VEHICLE_MODEL,
	USER_MODEL_LIST,
	RESERVATION_MODEL_LIST,
	VEHICLE_MODEL_LIST,
	//Error Messages - Except ERROR all other error used for messages only
	ERROR,
	TOO_MANY_AUTHENTICATION_RETRIES,	
	INTERNAL_ERROR;

	public static String getMessage(ProtocolMessage protocolMessage,String ...customMessage) {
		String messageString;
	
		switch (protocolMessage) {
			case OK:
				messageString = "OK";
				break;
				
			case WRONG_CREDENTIAL:
				messageString = "Email or password is incorrect";
				break;
				
			case TOO_MANY_AUTHENTICATION_RETRIES:
				messageString = "You have been tried to connect 5 times\nPlease Try again in "
														+SessionManager.BAN_DURATION + " seconds";
				break;
				
			case NO_AVAILABLE_VEHICLES:
				messageString = "There is no available vehicles for correct parameters";
				break;
				
			case INTERNAL_ERROR:
				messageString = "Internal error.\nPlease contact system suppport";
				break; 
				
			case USER_IS_BANNED:
				messageString = "You have been banned due to a large number of login attempts\n"
								+ "Please try again in " +customMessage[0]+ " seconds";
				break;
				
			case PASSWORD_CHANGED_SUCCESSFULLY:
				messageString = "Password has been changed successfully";
				break;
				
			case CHANGING_PASSWORD_FAILED:
				messageString = "Could not change password\nPlease contact system support";
				break;
				
			case ORDER_BOOKED_SUCCESSFULLY:
				messageString = "Order Booked Successfully";
				break;
				
			case ORDER_FAILED:
				messageString = "Failed to book the vehicle\nThe vehicle already booked by other client";
				break;
				
			case ACTIVE_ORDER_ALREADY_EXISTS:
				messageString = "Can't book more then 1 vehicle at a given time.\nActive order already exists";
				break;
				
			default:
				messageString = "Protocol Message case is not defined";
		}		
		return messageString;
	}
}