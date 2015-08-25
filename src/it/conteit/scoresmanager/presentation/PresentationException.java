package it.conteit.scoresmanager.presentation;

public class PresentationException extends Exception {
	private static final long serialVersionUID = 8880510513007284459L;

	public PresentationException(Exception e){
		super("Error during presentation: " + e.getMessage(), e);
	}
}
