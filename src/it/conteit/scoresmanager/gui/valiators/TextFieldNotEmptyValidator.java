package it.conteit.scoresmanager.gui.valiators;

import javax.swing.JTextField;

public class TextFieldNotEmptyValidator implements IValidator {
	private String name;
	private JTextField value;
	
	public TextFieldNotEmptyValidator(String fieldName, JTextField field){
		name = fieldName;
		value = field;
	}
	
	public String accept() {
		if(value.getText().length() > 0){
			return null;
		} else {
			return "\"" + name + "\" field cannot be empty";
		}
	}

}
