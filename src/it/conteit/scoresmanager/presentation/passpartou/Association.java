package it.conteit.scoresmanager.presentation.passpartou;

import it.conteit.scoresmanager.data.ITeam;

public class Association {
	public ITeam team;
	public int pteam;
	
	public Association(ITeam t, int pres_team){
		team = t;
		pteam = pres_team;
	}
}
