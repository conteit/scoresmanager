package it.conteit.scoresmanager.gui.valiators;

import it.conteit.scoresmanager.gui.models.TeamsListModel;

import javax.swing.JList;
import javax.swing.JTextField;

public class UniqueInTeamsListValidator implements IValidator {
	private JList list;
	private JTextField value;

	public UniqueInTeamsListValidator(JList list, JTextField value){
		this.list = list;
		this.value = value;
	}

	public String accept() {
		for(int i=0; i<list.getModel().getSize(); i++){
			if(i != list.getSelectedIndex()){
				if(((TeamsListModel) list.getModel()).getTeam(i).getName().equals(value.getText())){
					return "Team name \"" + value.getText() + "\" already used";
				}
			}
		}

		return null;
	}

}
