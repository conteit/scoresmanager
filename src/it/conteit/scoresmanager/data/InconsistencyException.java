package it.conteit.scoresmanager.data;

import java.lang.Exception;

public class InconsistencyException extends Exception {
	
	private static final long serialVersionUID = 9076256656588662864L;

	public InconsistencyException(String explanation){
		super(explanation);
	}
}
