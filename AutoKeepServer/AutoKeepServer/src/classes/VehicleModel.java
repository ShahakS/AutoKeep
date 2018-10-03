package classes;

public class VehicleModel {
	private int carID;
	private String manufacturer;
	private int manufactureDate;
	private int seatsNumber;
	private int engineCapacity;
	private boolean isUsable = true;
	
	public VehicleModel(String manufacturer,int manufactureDate,int seatsNumber,int engineCapacity,int horsePower){
		this.manufacturer = manufacturer;
		this.manufactureDate = manufactureDate;
		this.seatsNumber = seatsNumber;
		this.engineCapacity = engineCapacity;
	}
}
