package ClientServerProtocols;

import SessionControl.SessionManager;

public enum ProtocolMessage {
	OK,
	WRONG_CREDENTIAL,
	LOGIN,
	USER_IS_BANNED,
	USER_ALREADY_CONNECTED,
	
	SEARCH_VEHICLE,
	NO_AVAILABLE_VEHICLES,
	VEHICLES_LIST,
	DELETE_VEHICLE,
	VEHICLE_DELETED_SUCCESSFULLY,
	UPDATE_VEHICLE,
	VEHICLE_UPDATED_SUCCESSFULLY,
	CREATE_NEW_VEHICLE,
	VEHICLE_CREATED_SUCCESSFULLY,
	VEHICLE_ALREADY_EXIST,
	NO_VEHICLES,
	VEHICLE_UPDATE_FAILED,
	
	ORDER,
	ORDER_FAILED,
	ORDER_BOOKED_SUCCESSFULLY,
	
	VIEW_ORDERS,
	ACTIVE_ORDER_ALREADY_EXISTS,
	
	RESERVATION_HISTORY,
	NO_HISTORY,
	HISTORY_RESULT,
	RESERVATIONS_LIST,
	NO_RESERVATIONS,
	CREATE_NEW_RESERVATION,
	UPDATE_RESERVATION,
	RESERVATION_UPDATED_SUCCESSFULLY,
	RESERVATION_UPDATE_FAILED,
	
	USER_CHANGE_PASSWORD,
	PASSWORD_CHANGED_SUCCESSFULLY,
	CHANGING_PASSWORD_FAILED,
	CREATE_NEW_USER,
	USER_CREATED_SUCCESSFULLY,
	EMAIL_ADDRESS_ALREADY_REGISTERED,
	USERS_LIST,
	DELETE_USER,
	USER_DELETED_SUCCESSFULLY,
	USER_DELETION_FAILED,
	USER_UPDATED_SUCCESSFULLY,
	UPDATE_USER,
	USER_UPDATE_FAILED,
	
	USER_MODEL,
	RESERVATION_MODEL,
	VEHICLE_MODEL,
	USER_MODEL_LIST,
	RESERVATION_MODEL_LIST,
	VEHICLE_MODEL_LIST,
	//Error Messages - Except ERROR all other error used for messages only
	ERROR,
	TOO_MANY_AUTHENTICATION_RETRIES,	
	INTERNAL_ERROR,
	WRONG_PROTOCOL_REQUEST;
	
	public static String getMessage(ProtocolMessage protocolMessage,String ...args) {
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
				
			case USER_ALREADY_CONNECTED:
				messageString = "User already connected to the system in other device";
				break;
				
			case NO_AVAILABLE_VEHICLES:
				messageString = "There is no available vehicles for current parameters";
				break;
				
			case INTERNAL_ERROR:
				messageString = "Internal error.\nPlease contact system suppport";
				break; 
				
			case USER_IS_BANNED:
				messageString = "You have been banned due to a large number of login attempts\n"
								+ "Please try again in " +args[0]+ " seconds";
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
				
			case NO_HISTORY:
				messageString = "No reservation history from last 30 days";
				break;
				
			case WRONG_PROTOCOL_REQUEST:
				messageString = "Wrong Protocol request received";
				break;
				
			case USER_CREATED_SUCCESSFULLY:
				messageString = "The user "+ args[0] +" has been created successfully";
				break;
				
			case EMAIL_ADDRESS_ALREADY_REGISTERED:
				messageString = args[0] +" already registered as a user";
				break;
				
			case USER_DELETED_SUCCESSFULLY:
				messageString = "The user "+ args[0] +" has been deleted successfully";
				break;
				
			case USER_UPDATED_SUCCESSFULLY:
				messageString = "The user "+ args[0] +" has been updated successfully";
				break;
				
			case USER_DELETION_FAILED:
				messageString = "Failed to delete the user: "+args[0] +".\nCan't delete default account";
				break;
				
			case USER_UPDATE_FAILED:
				messageString = "Failed to update the user: "+args[0] +".\nThe user has been deleted";
				break;
				
			case VEHICLE_DELETED_SUCCESSFULLY:
				messageString = "The vehicle "+ args[0] +" has been deleted successfully";
				break;
				
			case VEHICLE_UPDATED_SUCCESSFULLY:
				messageString = "The vehicle "+ args[0] +" has been updated successfully";
				break;
				
			case VEHICLE_CREATED_SUCCESSFULLY:
				messageString = "The vehicle "+ args[0] +" has been created successfully";
				break;
				
			case VEHICLE_ALREADY_EXIST:
				messageString = "Plate number "+args[0] +" is already exists in the database";
				break;
				
			case NO_VEHICLES:
				messageString = "There are no vehicles to display";
				break;
				
			case VEHICLE_UPDATE_FAILED:
				messageString = "Failed to update the vehicle: "+args[0] +".\nThe vehicle has been deleted";
				break;
				
			case NO_RESERVATIONS:
				messageString = "There are no reservations from last 30 days to display";
				break;
				
			case RESERVATION_UPDATED_SUCCESSFULLY:
				messageString = "Reservation ID: "+ args[0] +" has been updated successfully";
				break;
				
			case RESERVATION_UPDATE_FAILED:
				messageString = "Reservation ID: "+ args[0] +" update failed";
				break;
				
			default:
				messageString = "Protocol Message case is not defined";
		}		
		return messageString;
	}
}