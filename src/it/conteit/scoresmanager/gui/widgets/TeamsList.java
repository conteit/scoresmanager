package it.conteit.scoresmanager.gui.widgets;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.gui.models.TeamsListModel;
import it.conteit.scoresmanager.gui.renderers.TeamsListCellRenderer;

import javax.swing.JList;

public class TeamsList extends JList {
	private static final long serialVersionUID = 7278349401944339161L;

	public TeamsList(IGrest grest){
		super(new TeamsListModel(grest));
		setCellRenderer(new TeamsListCellRenderer());
	}
	
	public TeamsList(){
		super(new TeamsListModel());
		setCellRenderer(new TeamsListCellRenderer());
	}
	
	public ITeam getSelectedTeam(){
		return (ITeam) getModel().getElementAt(getSelectedIndex());
	}
}
