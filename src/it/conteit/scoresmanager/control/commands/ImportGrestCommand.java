package it.conteit.scoresmanager.control.commands;

import java.io.File;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.GrestsFileChooser;

public class ImportGrestCommand extends Command {

	public ImportGrestCommand() {
		super("ImportGrest");
	}

	@Override
	public void execute() throws CommandExecutionException {
		File grestFile = GrestsFileChooser.showChooseDialog(getSource());
		
		if(grestFile != null){
			try {
				GrestsManager.getInstance().importGrest(grestFile);
			} catch (StorageException e) {
				throw new CommandExecutionException(e);
			}
		}
	}
	
	public static Command createCommand(){
		return new ImportGrestCommand();
	}

}
