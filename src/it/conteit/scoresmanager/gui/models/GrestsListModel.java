package it.conteit.scoresmanager.gui.models;

import java.util.Arrays;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.data.IGrest;

import javax.swing.DefaultListModel;

public class GrestsListModel extends DefaultListModel {
	private static final long serialVersionUID = -5398938206791197527L;
	
	public GrestsListModel(){
		super();
		IGrest[] grestsList = GrestsManager.getInstance().managedGrests();
		
		Arrays.sort(grestsList);
		
		for(int i=0; i<grestsList.length; i++){
			addGrest(grestsList[i]);
		}
	}
	
	// Possibile fonte di errore
	public void addGrest(IGrest grest){
		int i;
		boolean done = false;
		for(i=0; i<super.size(); i++){
			if(((IGrest) super.get(i)).compareTo(grest) >= 0){
				super.insertElementAt(grest, i);
				done = true;
				return;
			}
		}
		
		if(!done){
			super.addElement(grest);
		}
	}
	
	public void removeGrest(IGrest grest){
		super.removeElement(grest);
	}
	
	public IGrest getGrest(int index){
		return (IGrest) super.get(index);
	}
	
	public void updateGrest(IGrest grest){
		for(int i=0; i<super.size(); i++){
			if(((IGrest) super.get(i)).getName().equals(grest.getName())){
				super.set(i, grest);
			}
		}
	}
}
