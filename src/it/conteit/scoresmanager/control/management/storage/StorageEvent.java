package it.conteit.scoresmanager.control.management.storage;

public class StorageEvent {
	private String desc;
	private Object[] data;
	
	public StorageEvent(String description){
		this(description, new Object[0]);
	}
	
	public StorageEvent(String description, Object[] data){
		desc = description;
		this.data = data;
	}
	
	public String getDescription(){
		return desc;
	}
	
	public Object[] getData(){
		return data;
	}
	
	public String toString(){
		String dataS = desc;
		
		for(int i=0; i<data.length; i++){
			dataS += " | ";
			dataS += data[i];
		}
		
		return dataS; 
	}
}
