package customExceptions;

import java.text.ParseException;

public class TimeDifferenceParsingException extends ParseException{

	public TimeDifferenceParsingException(String message, int errorLocation) {
		super(message, errorLocation);
		
		// TODO Auto-generated constructor stub
	}
	
}
