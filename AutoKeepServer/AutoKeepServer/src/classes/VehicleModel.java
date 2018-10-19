package classes;

public class VehicleModel {
	private int vehicleID;
	private String manufactureName;
	private String model;
	private String vehicleType;
	private int manufactureYear;
	private int seatsNumber;
	private int engineCapacity;
	private boolean isUsable = true;
	private int kilometers;
	private String vehicleImage;
	
	public VehicleModel(int vehicleID, String manufactureName, String model, String vehicleType, int manufactureYear,
			int seatsNumber, int engineCapacity, boolean isUsable, int kilometers, String vehicleImage) {
		super();
		this.vehicleID = vehicleID;
		this.manufactureName = manufactureName;
		this.model = model;
		this.vehicleType = vehicleType;
		this.manufactureYear = manufactureYear;
		this.seatsNumber = seatsNumber;
		this.engineCapacity = engineCapacity;
		this.isUsable = isUsable;
		this.kilometers = kilometers;
		this.vehicleImage = vehicleImage;
	}

	public int getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(int vehicleID) {
		this.vehicleID = vehicleID;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(int manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public int getSeatsNumber() {
		return seatsNumber;
	}

	public void setSeatsNumber(int seatsNumber) {
		this.seatsNumber = seatsNumber;
	}

	public int getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(int engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public boolean isUsable() {
		return isUsable;
	}

	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}

	public int getKilometers() {
		return kilometers;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public String getVehicleImage() {
		return vehicleImage;
	}

	public void setVehicleImage(String vehicleImage) {
		this.vehicleImage = vehicleImage;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}
