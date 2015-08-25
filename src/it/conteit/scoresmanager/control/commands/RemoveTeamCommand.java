package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class RemoveTeamCommand extends Command {
	public static final String INTERESTED_GREST = "grest";
	public static final String TEAM_TO_BE_REMOVED = "team";
	
	public RemoveTeamCommand() {
		super("RemoveTeam");
	}
	
	public RemoveTeamCommand(Component sourceComp) {
		super("RemoveTeam", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			if(ApplicationSystem.getInstance().showQuestion("Do you really want to remove \"" + ((ITeam) getParameter(TEAM_TO_BE_REMOVED)).getName() + "\" team?", getSource()) == DialogResults.YES){
				((IGrest) getParameter(INTERESTED_GREST)).removeTeam((ITeam) getParameter(TEAM_TO_BE_REMOVED));
			}
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IGrest grest, ITeam team){
		return createCommand(grest, team, null);
	}
	
	public static Command createCommand(IGrest grest, ITeam team, Component sourceComp){
		Command cmd = new RemoveTeamCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, grest);
		cmd.setParameter(TEAM_TO_BE_REMOVED, team);
		
		return cmd;
	}

}
