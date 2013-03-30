package pl.igore;

public class UniqueUserException extends Exception {
	public String getMessage(){
		return "This nickname is taken. Please choose another one.";	}
}
