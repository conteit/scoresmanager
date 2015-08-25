package it.conteit.scoresmanager.gui.valiators;

import javax.swing.JList;

public class TeamsListValidator implements IValidator {
	private String name;
	private JList value;
	
	public TeamsListValidator(String fieldName, JList list){
		name = fieldName;
		value = list;
	}
	
	public String accept() {
		if(value.getModel().getSize() >= 2){
			return null;
		} else {
			return "\"" + name + "\" list must have at least 2 elements";
		}
	}

}
