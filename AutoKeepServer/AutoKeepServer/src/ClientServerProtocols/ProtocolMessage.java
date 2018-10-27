package ClientServerProtocols;

import SessionControl.SessionManager;

public enum ProtocolMessage {
	OK,
	WRONG_CREDENTIAL,
	LOGIN,
	USER_IS_BANNED,
	SEARCH_VEHICLE,
	NO_AVAILABLE_VEHICLES,
	NEW_ORDER,
	VIEW_ORDERS,
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
				
			default:
				messageString = "Protocol Message case is not defined";
		}		
		return messageString;
	}
}