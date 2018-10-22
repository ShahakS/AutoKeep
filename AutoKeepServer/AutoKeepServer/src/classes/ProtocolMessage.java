package classes;

public enum ProtocolMessage {
	OK,
	WRONG_CREDENTIAL,
	LOGIN,
	SEARCH_VEHICLE,
	NO_AVAILABLE_VEHICLES,
	NEW_ORDER,
	VIEW_ORDERS,
	USER_CHANGE_PASSWORD,
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

	public static String getMessage(ProtocolMessage protocolMessage) {
		String messageString;
	
		switch (protocolMessage) {
			case OK:
				messageString = "OK";
				break;
				
			case WRONG_CREDENTIAL:
				messageString = "Email or password is incorrect";
				break;
				
			case TOO_MANY_AUTHENTICATION_RETRIES:
				messageString = "You have been tried to connect 5 times.\nPlease Try again later";
				break;
				
			case NO_AVAILABLE_VEHICLES:
				messageString = "There is no available vehicles for correct parameters";
				break;
				
			case INTERNAL_ERROR:
				messageString = "Internal error.\nPlease contact suppport";
				break;
				
			default:
				messageString = "Protocol Message case is not defined";
		}		
		return messageString;
	}
}