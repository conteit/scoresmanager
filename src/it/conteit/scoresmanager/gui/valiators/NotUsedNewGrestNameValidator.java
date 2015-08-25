package it.conteit.scoresmanager.gui.valiators;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.data.IGrest;

import javax.swing.JTextField;

public class NotUsedNewGrestNameValidator implements IValidator {
	private JTextField value;
	private IGrest grest;
	
	public NotUsedNewGrestNameValidator(IGrest g, JTextField value){
		this.value = value;
		this.grest = g;
	}
	
	public String accept() {
		if(grest.getName().equals(value.getText())){
			return null;
		}
		
		if(!GrestsManager.getInstance().nameAlreadyUsed(value.getText())){
			return null;
		} else {
			return "Grest \"" + value.getText() + "\" already exists";
		}
	}

}
