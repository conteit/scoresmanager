package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.gui.dialogs.NewGrestDialog;

import java.awt.Component;

public class NewGrestCommand extends Command {

	public NewGrestCommand() {
		this(null);
	}
	
	public NewGrestCommand(Component sourceComp){
		super("NewGrest", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		NewGrestDialog.showDialog(getSource());
	}
	
	public static Command createCommand(){
		return new NewGrestCommand();
	}

	public static Command createCommand(Component sourceComp){
		return new NewGrestCommand(sourceComp);
	}
}
