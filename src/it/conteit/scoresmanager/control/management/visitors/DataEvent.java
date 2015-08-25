package it.conteit.scoresmanager.control.management.visitors;


public class DataEvent {
	private String desc;
	private IAcceptDataVisitor source;
	
	public DataEvent(IAcceptDataVisitor source){
		this("", source);
	}
	
	public DataEvent(String desc, IAcceptDataVisitor source){
		this.source = source;
		this.desc = desc;
	}
	
	public String getDescription(){
		return desc;
	}
	
	public IAcceptDataVisitor getSource(){
		return source;
	}
	
	public String toString(){
		return source.toString();
	}
}
