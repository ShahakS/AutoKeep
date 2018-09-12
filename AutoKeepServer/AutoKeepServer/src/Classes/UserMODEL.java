package Classes;
public class UserMODEL {
	private String userName;
	private String password;
	private String dateOfBirth;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phoneNumber;
	private boolean isAdmin = false;
	
	public UserMODEL(String userName, String password, String dateOfBirth, String firstName, String lastName,
			String emailAddress, String phoneNumber, boolean isAdmin) {
		super();
		this.userName = userName;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.isAdmin = isAdmin;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
}
