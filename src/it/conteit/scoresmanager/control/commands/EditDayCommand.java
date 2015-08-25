package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.EditDayDialog;

import java.awt.Component;

public class EditDayCommand extends Command {
	private static final String INTERESTED_GREST = "grest";
	private static final String INTERESTED_DAY = "day";
	
	public EditDayCommand() {
		this(null);
	}
	
	public EditDayCommand(Component sourceComp){
		super("NewDay", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		EditDayDialog.showDialog((IDay) getParameter(INTERESTED_DAY), (IGrest) getParameter(INTERESTED_GREST));
	}
	
	public static Command createCommand(IGrest g, IDay d){
		return createCommand(g, d, null);
	}

	public static Command createCommand(IGrest g, IDay d, Component sourceComp){
		Command cmd = new EditDayCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, g);
		cmd.setParameter(INTERESTED_DAY, d);
		
		return cmd;
	}
}
