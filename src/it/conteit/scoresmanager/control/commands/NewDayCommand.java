package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.dialogs.DayNameInputDialog;

import java.awt.Component;

public class NewDayCommand extends Command {
	private static final String INTERESTED_GREST = "grest";
	
	public NewDayCommand() {
		this(null);
	}
	
	public NewDayCommand(Component sourceComp){
		super("NewDay", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		IGrest grest = (IGrest) getParameter(INTERESTED_GREST);
		IDay newDay = DayNameInputDialog.showDialog(grest);
		
		if(newDay == null){
			return;
		}
		
		try {
			grest.addDay(newDay);
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IGrest g){
		return createCommand(g, null);
	}

	public static Command createCommand(IGrest g, Component sourceComp){
		Command cmd = new NewDayCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, g);
		
		return cmd;
	}
}
