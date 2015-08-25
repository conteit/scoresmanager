package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.RenameImportGrestInputDialog;

import java.awt.Component;

import javax.swing.SwingUtilities;

public class RenameGrestCommand extends Command {
	public static final String GREST_TO_BE_RENAMED = "grest";

	public RenameGrestCommand() {
		super("RenameGrest");
	}

	public RenameGrestCommand(Component sourceComp) {
		super("RenameGrest", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			if(SwingUtilities.isEventDispatchThread()){
				op();
			} else {
				SwingUtilities.invokeAndWait(new Runnable(){

					public void run() {
						op();
					}

				});
			}
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}
	
	private void op(){
		IGrest res = RenameImportGrestInputDialog.showDialog((IGrest) getParameter(GREST_TO_BE_RENAMED));
		setReturnValue(res != null);
	}

	public static Command createCommand(IGrest grest){
		return createCommand(grest, null);
	}

	public static Command createCommand(IGrest grest, Component sourceComp){
		Command cmd = new RenameGrestCommand(sourceComp);
		cmd.setParameter(GREST_TO_BE_RENAMED, grest);

		return cmd;
	}

}
