package Classes;
public class User {
	private String userName;
	private String password;
	private String dateOfBirth;
	private String firstName;
	private String lastName;
	private boolean isActive = true;
	private boolean isAdmin = false;
	
	public User(String userName, String password,String dateOfBirth,String firstName,String lastName){
		this.userName = userName;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getUserName() {
		return userName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	} 
}
