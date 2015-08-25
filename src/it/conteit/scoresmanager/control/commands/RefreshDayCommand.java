package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class RefreshDayCommand extends Command {
	private static final String INTERESTED_DAY = "day";
	
	public RefreshDayCommand() {
		this(null);
	}
	
	public RefreshDayCommand(Component sourceComp){
		super("RefreshDay", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			String desc = ((IDay) getParameter(INTERESTED_DAY)).getDescription();
			((IDay) getParameter(INTERESTED_DAY)).setDescription(desc);
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IDay d){
		return createCommand(d, null);
	}

	public static Command createCommand(IDay d, Component sourceComp){
		Command cmd = new RefreshDayCommand(sourceComp);
		cmd.setParameter(INTERESTED_DAY, d);
		
		return cmd;
	}
}
