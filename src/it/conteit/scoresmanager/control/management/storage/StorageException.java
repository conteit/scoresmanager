package it.conteit.scoresmanager.control.management.storage;

public class StorageException extends Exception {
	private Object[] data;
	private static final long serialVersionUID = 3289431345592191617L;

	public StorageException(String desc){
		this(desc, null);
	}
	
	public StorageException(String desc, Object[] data){
		super(desc);
		this.data = data;
	}
	
	public Object[] getData(){
		return data;
	}
}
