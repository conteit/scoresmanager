package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.data.ITeam;

import java.awt.Component;

public class CreateGrestCommand extends Command {
	private static final String GREST_NAME = "GrestName";
	private static final String TEAMS_COUNT = "TeamsCount";
	private static final String TEAM_BASE = "Team";
	
	public CreateGrestCommand() {
		this(null);
	}
	
	public CreateGrestCommand(Component sourceComp){
		super("CreateGrest", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		int teamsCount = new Integer(getParameter(TEAMS_COUNT).toString());
		ITeam[] teams = new ITeam[teamsCount];
		
		for(int i=0; i<teamsCount; i++){
			teams[i] = (ITeam) getParameter(TEAM_BASE + i);
		}
		
		try {
			GrestsManager.getInstance().createGrest(getParameter(GREST_NAME).toString(), teams);
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(){
		return new CreateGrestCommand();
	}

	public static Command createCommand(Component sourceComp, String grestName, ITeam[] teams){
		Command cmd = new CreateGrestCommand(sourceComp);
		cmd.setParameter(GREST_NAME, grestName);
		cmd.setParameter(TEAMS_COUNT, teams.length);
		
		for(int i=0; i<teams.length; i++){
			cmd.setParameter(TEAM_BASE + i, teams[i]);
		}
		
		return cmd;
	}
}
