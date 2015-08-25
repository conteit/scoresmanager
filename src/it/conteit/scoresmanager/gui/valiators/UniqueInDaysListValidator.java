package it.conteit.scoresmanager.gui.valiators;

import it.conteit.scoresmanager.data.IGrest;

import javax.swing.JTextField;

public class UniqueInDaysListValidator implements IValidator {
	private IGrest grest;
	private JTextField value;
	private String oldValue;
	
	public UniqueInDaysListValidator(IGrest grest, JTextField value){
		this(grest, value, null);
	}
	
	public UniqueInDaysListValidator(IGrest grest, JTextField value, String oldValue){
		this.grest = grest;
		this.value = value;
		this.oldValue = oldValue;
	}

	public String accept() {
		if(oldValue != null && value.getText().equals(oldValue)){
			return null;
		}
		
		for(int i=0; i<grest.days().length; i++){
			if(grest.days()[i].getDescription().equals(value.getText())){
				return "Day name \"" + value.getText() + "\" already used";
			}
		}
		
		return null;
	}
	
	public void setOldValue(String oldValue){
		this.oldValue = oldValue;
	}

}
