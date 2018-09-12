package Classes;

public class Reservation {
	private int reservationID;
	private int reservedByUserID;
	private String reservationDate;
	private String reservationStart;
	private String reservationEnd;
	private boolean isDelayed;

	public Reservation(int reservationID, int reservedByUserID, String reservationDate, String reservationStart,
			String reservationEnd) {
		super();
		this.reservationID = reservationID;
		this.reservedByUserID = reservedByUserID;
		this.reservationDate = reservationDate;
		this.reservationStart = reservationStart;
		this.reservationEnd = reservationEnd;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public int getReservedByUserID() {
		return reservedByUserID;
	}

	public void setReservedByUserID(int reservedByUserID) {
		this.reservedByUserID = reservedByUserID;
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

	public boolean isDelayed() {
		return isDelayed;
	}

	public void setDelayed(boolean isDelayed) {
		this.isDelayed = isDelayed;
	}
}
