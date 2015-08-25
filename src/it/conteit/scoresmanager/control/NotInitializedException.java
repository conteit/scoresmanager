package it.conteit.scoresmanager.control;

public class NotInitializedException extends RuntimeException {
	private static final long serialVersionUID = -2004936359765172295L;

	public NotInitializedException(String message){
		super(message);
	}
}
