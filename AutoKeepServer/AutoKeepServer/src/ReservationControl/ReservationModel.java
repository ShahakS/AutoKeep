package ReservationControl;

import UserControl.UserModel;
import VehicleControl.VehicleModel;

public class ReservationModel {
	private int reservationID;
	private UserModel user;
	private VehicleModel vehicle;
	private String reservationDate;
	private String reservationStartDate;
	private String reservationEndDate;
	
	public ReservationModel() {
	}
	
	public ReservationModel(int reservationID, UserModel user, VehicleModel vehicle, String reservationDate,
			String reservationStartDate, String reservationEndDate) {
		super();
		this.reservationID = reservationID;
		this.user = user;
		this.vehicle = vehicle;
		this.reservationDate = reservationDate;
		this.reservationStartDate = reservationStartDate;
		this.reservationEndDate = reservationEndDate;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public VehicleModel getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleModel vehicle) {
		this.vehicle = vehicle;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getReservationStartDate() {
		return reservationStartDate;
	}

	public void setReservationStartDate(String reservationStartDate) {
		this.reservationStartDate = reservationStartDate;
	}

	public String getReservationEndDate() {
		return reservationEndDate;
	}

	public void setReservationEndDate(String reservationEndDate) {
		this.reservationEndDate = reservationEndDate;
	}
}
