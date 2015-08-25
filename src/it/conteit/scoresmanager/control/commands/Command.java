package it.conteit.scoresmanager.control.commands;

import java.awt.Component;
import java.util.HashMap;

public abstract class Command {
	private String name;
	private HashMap<String, Object> attributes;
	private Component source;
	
	public Command(String name){
		this(name, null);
	}
	
	public Command(String name, Component sourceComp){
		this.name = name;
		this.source = sourceComp;
		attributes = new HashMap<String, Object>();
	}
	
	public String getName(){
		return name;
	}
	
	public Object getReturnValue(){
		return getParameter("RETURN_VALUE");
	}
	
	protected void setReturnValue(Object value){
		setParameter("RETURN_VALUE", value);
	}
	
	public void setParameter(String key, Object value){
		attributes.put(key, value);
	}
	
	public Object getParameter(String key){
		return attributes.get(key);
	}
	
	public Component getSource(){
		return source;
	}
	
	public abstract void execute() throws CommandExecutionException;
}
