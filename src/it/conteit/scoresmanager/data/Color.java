package it.conteit.scoresmanager.data;

import java.util.ArrayList;

import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;

public class Color implements IColor<java.awt.Color>{
	private java.awt.Color color;
	
	private ArrayList<IDataListener> listeners = new ArrayList<IDataListener>();
	
	public Color(java.awt.Color c) throws InconsistencyException{
		if(c == null){
			throw new InconsistencyException("Invalid argument: must be not null");
		}
		
		color = c;
	}
	
	public java.awt.Color getValue() {
		return color;
	}

	public void accept(IDataVisitor v) {
		v.visit(this);
	}
	
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		
		if(o instanceof Color){
			return color.equals(((Color) o).getValue());
		}
		
		return false;
	}
	
	public String toString(){
		return String.format("RGB(A) = (%d, %d, %d, %d)", 
				color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public void addDataListener(IDataListener l) {
		listeners.add(l);
	}

	public void removeDataListener(IDataListener l) {
		listeners.remove(l);
	}
}
