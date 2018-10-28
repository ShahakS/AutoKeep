package CommunicationManager;

import java.lang.reflect.Type;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import ClientServerProtocols.ProtocolMessage;
import ReservationControl.ReservationModel;
import UserControl.UserModel;
import VehicleControl.VehicleModel;
import exceptionsPackage.ExcaptionHandler;


public class CommunicationInterpreter {
	private Gson gson;
	private JsonParser parser;
	
	public CommunicationInterpreter() {
		super();
		this.gson = new GsonBuilder().create();
		this.parser = new JsonParser();
	}
	
	/**
	 * The method interpret the string and returns the received protocol message
	 * @param jsonString - the received string to be interpreted
	 * @return the protocol message
	 */
	public ProtocolMessage getProtocolMsg(String jsonString) {
		JsonObject jsonObj = (JsonObject)parser.parse(jsonString);
		return gson.fromJson(jsonObj.get("protocolMsg").toString(), ProtocolMessage.class);
	}
	
	public String setProtocolMsg(ProtocolMessage protocolMsg){
		JsonObject jsonObj = new JsonObject();
		jsonObj.add("protocolMsg", gson.toJsonTree(protocolMsg,ProtocolMessage.class));
		return jsonObj.toString();
	}
	
	/**
	 * The method return a string of json that represents the data
	 * @param protocolMsg - the protocol rule
	 * @param obj - the data to be converted
	 * @return a string of json, if protocolMsg is not supported return null
	 * @throws ClassCastException - if the protocolMsg and obj are not coordinated
	 */
	public String encodeObjToJson(ProtocolMessage protocolMsg,Object obj) throws ClassCastException{
		JsonObject jsonObj = new JsonObject();
		jsonObj.add("protocolMsg", gson.toJsonTree(protocolMsg,ProtocolMessage.class));
		Type listType;
		
		try {
			switch(protocolMsg) {
			case USER_MODEL:
				jsonObj.add("user", gson.toJsonTree((UserModel)obj,UserModel.class));
				return jsonObj.toString();
			case SEARCH_VEHICLE:
			case RESERVATION_MODEL:
				jsonObj.add("reservation", gson.toJsonTree((ReservationModel)obj,ReservationModel.class));
				return jsonObj.toString();
				
			case VEHICLE_MODEL:
				jsonObj.add("vehicle", gson.toJsonTree((VehicleModel)obj,VehicleModel.class));
				return jsonObj.toString();
				
			case USER_MODEL_LIST:	
				listType = new TypeToken<Queue<UserModel>>(){}.getType();
				jsonObj.add("users", gson.toJsonTree(obj,listType));
				return jsonObj.toString();
				
			case RESERVATION_MODEL_LIST:
				listType = new TypeToken<Queue<ReservationModel>>(){}.getType();
				jsonObj.add("reservations", gson.toJsonTree(obj,listType));
				return jsonObj.toString();
				
			case VEHICLE_MODEL_LIST:		
				listType = new TypeToken<Queue<VehicleModel>>(){}.getType();
				jsonObj.add("vechiles", gson.toJsonTree(obj,listType));
				return jsonObj.toString();
				
			default:
				jsonObj.add("message", gson.toJsonTree((String)obj,String.class));				
				return jsonObj.toString();
			}
		}catch(ClassCastException e) {
			ExcaptionHandler error = new ExcaptionHandler("Mismatched obj and protocolMsg - couldn't cast",e);
			error.writeToErrorLog();
			throw e;
		}		
	}	
	
	/**
	 * The method interpret the json string and returns a MODEL or String object
	 * @param jsonString - the received string to be interpreted
	 * @return a MODEL or String wrapped by Object class
	 */
	public Object decodeFromJsonToObj(ProtocolMessage protocolMsg ,String jsonString){
			JsonObject jsonObj = (JsonObject)parser.parse(jsonString);
			Type listType;
			
			switch(protocolMsg) {
			case USER_MODEL:
				UserModel user = gson.fromJson(jsonObj.get("user").toString(), UserModel.class);
				return user;

			case RESERVATION_MODEL:
				ReservationModel reservation = gson.fromJson(jsonObj.get("reservation").toString(), ReservationModel.class);
				return reservation;
				
			case VEHICLE_MODEL:
				VehicleModel vechile = gson.fromJson(jsonObj.get("vechile").toString(), VehicleModel.class);
				return vechile;
				
			case USER_MODEL_LIST:
				listType = new TypeToken<Queue<UserModel>>(){}.getType();  	
				Queue<UserModel> users = gson.fromJson(jsonObj.get("users").toString(),listType);
				return users;
				
			case RESERVATION_MODEL_LIST:
				 listType = new TypeToken<Queue<ReservationModel>>(){}.getType();  	
				Queue<ReservationModel> reservations = gson.fromJson(jsonObj.get("reservations").toString(),listType);
				return reservations;
				
			case VEHICLE_MODEL_LIST:
				listType = new TypeToken<Queue<VehicleModel>>(){}.getType();  	
				Queue<VehicleModel> vechiles = gson.fromJson(jsonObj.get("vechiles").toString(),listType);
				return vechiles;
				
			default:
				listType = new TypeToken<String>(){}.getType();  	
				String message = gson.fromJson(jsonObj.get("message").toString(),listType);
				return message;
			}
	}
	
	/**
	 * The method get parameters and build a json string out of it
	 * @param protocolMsg
	 * @param keys
	 * @param values
	 * @return json String
	 */
	public String encodeParametersToJson(ProtocolMessage protocolMsg,Queue<String> keys,Queue<String> values) {
		JsonObject jsonObj = new JsonObject();
		jsonObj.add("protocolMsg", gson.toJsonTree(protocolMsg,ProtocolMessage.class));
				
		while(!keys.isEmpty()) {
			JsonObject jsonObjValue = (JsonObject)parser.parse(values.poll());
			jsonObj.add(keys.poll(), gson.toJsonTree(jsonObjValue));
		}
		return jsonObj.toString();
	}
}