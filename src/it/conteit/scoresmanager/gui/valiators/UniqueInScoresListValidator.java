package it.conteit.scoresmanager.gui.valiators;

import it.conteit.scoresmanager.data.IDay;

import javax.swing.JTextField;

public class UniqueInScoresListValidator implements IValidator {
	private JTextField value;
	private IDay d;
	private String oldValue;

	public UniqueInScoresListValidator(IDay d, JTextField value){
		this(d, value, null);
	}
	
	public UniqueInScoresListValidator(IDay d, JTextField value, String oldValue){
		this.d = d;
		this.value = value;
		this.oldValue = oldValue;
	}

	public String accept() {
		if(oldValue != null && oldValue.equals(value.getText())){
			return null;
		}
		
		for(int i=0; i<d.scores().length; i++){
			if(d.scores()[i].getDescription().equals(value.getText())){
				return "Score name \"" + value.getText() + "\": already used";
			}
		}
		
		return null;
	}
	
	public void setOldValue(String oldValue){
		this.oldValue = oldValue;
	}

}
