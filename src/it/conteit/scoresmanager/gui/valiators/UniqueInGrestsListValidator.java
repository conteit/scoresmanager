package it.conteit.scoresmanager.gui.valiators;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;

import javax.swing.JTextField;

public class UniqueInGrestsListValidator implements IValidator {
	private JTextField value;
	
	public UniqueInGrestsListValidator(JTextField value){
		this.value = value;
	}
	
	public String accept() {
		if(!GrestsManager.getInstance().nameAlreadyUsed(value.getText())){
			return null;
		} else {
			return "Grest \"" + value.getText() + "\" already exists";
		}
	}

}
