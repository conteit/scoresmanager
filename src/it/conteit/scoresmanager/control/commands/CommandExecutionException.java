package it.conteit.scoresmanager.control.commands;

public class CommandExecutionException extends Exception {
	private static final long serialVersionUID = 5314380380577950598L;
	private Object[] data;
	private Exception cause;
	
	public CommandExecutionException(Exception cause){
		this("Exception raised: " + cause.getMessage(), cause, null);
	}
	
	public CommandExecutionException(String message, Object[] data){
		this(message, null, data);
	}
	
	public CommandExecutionException(String message, Exception cause, Object[] data){
		super(message);
		this.data = data;
	}
	
	public Object[] getData(){
		return data;
	}
	
	public Exception getCause(){
		return cause;
	}
}
