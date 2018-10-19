package classes;

public enum ProtocolMessage {
	OK,
	WRONG_CREDENTIAL,
	LOGIN,
	
	USER_MODEL,
	RESERVATION_MODEL,
	VEHICLE_MODEL,
	USER_MODEL_LIST,
	RESERVATION_MODEL_LIST,
	VECHILE_MODEL_LIST,
	//Error Messages - Except ERROR all other error used for messages only
	ERROR,
	TOO_MANY_AUTHENTICATION_RETRIES,	
	INTERNAL_ERROR;

	public static String getStatus(ProtocolMessage protocolMessage) {
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
				
			case INTERNAL_ERROR:
				messageString = "Internal error.\nPlease contact suppport";
				break;
				
			default:
				messageString = "";
		}		
		return messageString;
	}
}