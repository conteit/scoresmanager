package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.MainFrame;

public class OpenGrestCommand extends Command {
	public static final String GREST_TO_BE_OPENED = "grest";
	
	public OpenGrestCommand() {
		super("OpenGrest");
	}

	@Override
	public void execute() throws CommandExecutionException {
		MainFrame.getInstance().createDocumentTab((IGrest) getParameter(GREST_TO_BE_OPENED));
	}
	
	public static Command createCommand(IGrest grest){
		Command cmd = new OpenGrestCommand();
		cmd.setParameter(GREST_TO_BE_OPENED, grest);
		
		return cmd;
	}

}
