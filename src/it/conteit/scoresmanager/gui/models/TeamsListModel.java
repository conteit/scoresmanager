package it.conteit.scoresmanager.gui.models;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.Team;

import javax.swing.DefaultListModel;

public class TeamsListModel extends DefaultListModel implements IDataListener {
	private static final long serialVersionUID = 4802971824332542552L;

	private IGrest grest;

	public TeamsListModel(IGrest grest){
		super();

		this.grest = grest;
		grest.addDataListener(this);

		ITeam[] teams = grest.teams();
		for(int i=0; i< teams.length; i++){
			addTeam(teams[i]);
		}
	}

	public TeamsListModel(){
		super();

		try {
			addTeam(Team.create("Team1"));
			addTeam(Team.create("Team2"));
		} catch (InconsistencyException e) {
			ApplicationSystem.getInstance().logError("Cannot create default teams");
		}
	}
	
	public void addTeam(ITeam team){
/*		int i;
		boolean done = false;
		for(i=0; i<super.size(); i++){
			if(((ITeam) super.get(i)).getName().compareTo(team.getName()) >= 0){
				super.insertElementAt(team, i);
				done = true;
				return;
			}
		}

		if(!done){*/
			super.addElement(team);
		/*}*/
	}

	public void removeTeam(ITeam team){
		super.removeElement(team);
	}

	public ITeam getTeam(int index){
		return (ITeam) super.get(index);
	}

	public void updateTeam(ITeam team){
		for(int i=0; i<super.size(); i++){
			if(((ITeam) super.get(i)).getName().equals(team.getName())){
				super.set(i, team);
				
				if(grest != null){
					for(int j=0; j<grest.teamCount(); j++){
						ITeam t = grest.teams()[j];
						if(team.equals(t)){
							try {
								t.setName(team.getName());
								t.setColor(team.getColor());
								t.setAvatar(team.getAvatar());
							} catch (InconsistencyException e) {
								ApplicationSystem.getInstance().showWarning("Error while updating team. Some data will be lost.", null);
							}
						}
					}
				}
				
			}
		}
	}

	public boolean nameAlreadyUsed(String name){
		for(int i=0; i<super.getSize(); i++){
			if(((ITeam) super.get(i)).getName().equals(name)){
				return true;
			}
		}

		return false;
	}

	public void dataChanged(DataEvent ev) {
		if(ev.getSource() instanceof IGrest){
			IGrest g = ((IGrest) ev.getSource());

			super.clear();

			for(int k=0;k<g.teamCount(); k++){
				ITeam t = g.teams()[k];
				if(nameAlreadyUsed(t.getName())){
					updateTeam(t);
				} else {
					addTeam(t);
				}
			}
		}
	}
}
