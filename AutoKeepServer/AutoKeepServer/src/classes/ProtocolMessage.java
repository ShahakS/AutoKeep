package classes;

public enum ProtocolMessage {
	LOGIN,
	VALIDATE_CREDENTIAL,
	USER_MODEL,
	RESERVATION_MODEL,
	VEHICLE_MODEL,
	USER_MODEL_LIST,
	RESERVATION_MODEL_LIST,
	VECHILE_MODEL_LIST;

	public static String getRate(ProtocolMessage protocolMessage) {
		String messageString;
	
		switch (protocolMessage) {
			case USER_MODEL:
				messageString = "The Class type is UserModel.class";
				break;
			case RESERVATION_MODEL:
				messageString = "The Class type is .class";
				break;
			case VEHICLE_MODEL:
				messageString = "The Class type is .class";
				break;
			case USER_MODEL_LIST:
				messageString = "The Class type is .class";
				break;
			case RESERVATION_MODEL_LIST:
				messageString = "The Class type is .class";
				break;
			case VECHILE_MODEL_LIST:
				messageString = "The Class type is .class";
				break;
			default:
				messageString = "Fatal error. Please contact suppport";
		}
		
		return messageString;
	}
}