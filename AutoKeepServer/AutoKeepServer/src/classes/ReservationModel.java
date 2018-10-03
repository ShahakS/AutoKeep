package classes;

public class ReservationModel {
	private int reservationID;
	private UserModel user;
	private String reservationDate;
	private String reservationStart;
	private String reservationEnd;
	private VehicleModel vehicle;

	public ReservationModel(int reservationID, UserModel user, String reservationDate, String reservationStart,
			String reservationEnd,VehicleModel vehicle) {
		super();
		this.user = user;
		this.vehicle = vehicle;
		this.reservationID = reservationID;		
		this.reservationDate = reservationDate;
		this.reservationStart = reservationStart;
		this.reservationEnd = reservationEnd;
	}

	public VehicleModel getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(VehicleModel vehicle) {
		this.vehicle = vehicle;
	}
	
	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public UserModel getReservedByUserID() {
		return user;
	}

	public void setReservedByUserID(UserModel user) {
		this.user = user;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getReservationStart() {
		return reservationStart;
	}

	public void setReservationStart(String reservationStart) {
		this.reservationStart = reservationStart;
	}

	public String getReservationEnd() {
		return reservationEnd;
	}

	public void setReservationEnd(String reservationEnd) {
		this.reservationEnd = reservationEnd;
	}

}
