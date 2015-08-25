package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class SetDayDescCommand extends Command {
	private static final String VALUE = "desc";
	private static final String INTERESTED_DAY = "day";
	
	public SetDayDescCommand() {
		this(null);
	}
	
	public SetDayDescCommand(Component sourceComp){
		super("SetDayDesc", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			((IDay) getParameter(INTERESTED_DAY)).setDescription((String) getParameter(VALUE));
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IDay d, String desc){
		return createCommand(d, desc, null);
	}

	public static Command createCommand(IDay d, String desc, Component sourceComp){
		Command cmd = new SetDayDescCommand(sourceComp);
		cmd.setParameter(INTERESTED_DAY, d);
		cmd.setParameter(VALUE, desc);
		
		return cmd;
	}
}
