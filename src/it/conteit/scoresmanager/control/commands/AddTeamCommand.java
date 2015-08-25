package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class AddTeamCommand extends Command {
	public static final String INTERESTED_GREST = "grest";
	public static final String NEW_TEAM = "team";
	
	public AddTeamCommand() {
		super("AddTeam");
	}
	
	public AddTeamCommand(Component sourceComp) {
		super("AddTeam", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			IGrest grest = (IGrest) getParameter(INTERESTED_GREST);
			ITeam team = (ITeam) getParameter(NEW_TEAM);
			
			grest.addTeam(team);
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IGrest grest, ITeam team){
		return createCommand(grest, team, null);
	}
	
	public static Command createCommand(IGrest grest, ITeam team, Component sourceComp){
		Command cmd = new AddTeamCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, grest);
		cmd.setParameter(NEW_TEAM, team);
		
		return cmd;
	}

}
