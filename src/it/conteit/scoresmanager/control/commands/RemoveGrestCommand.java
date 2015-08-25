package it.conteit.scoresmanager.control.commands;

import java.awt.Component;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.data.IGrest;

public class RemoveGrestCommand extends Command {
	public static final String GREST_TO_BE_REMOVED = "grest";
	
	public RemoveGrestCommand() {
		super("RemoveGrest");
	}
	
	public RemoveGrestCommand(Component sourceComp) {
		super("RemoveGrest", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			if(ApplicationSystem.getInstance().showQuestion("Do you really want to remove \"" + ((IGrest) getParameter(GREST_TO_BE_REMOVED)).getName() + "\" grest?", getSource()) == DialogResults.YES){
				GrestsManager.getInstance().deleteGrest((IGrest) getParameter(GREST_TO_BE_REMOVED));
			}
		} catch (StorageException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IGrest grest){
		return createCommand(grest, null);
	}
	
	public static Command createCommand(IGrest grest, Component sourceComp){
		Command cmd = new RemoveGrestCommand(sourceComp);
		cmd.setParameter(GREST_TO_BE_REMOVED, grest);
		
		return cmd;
	}

}
