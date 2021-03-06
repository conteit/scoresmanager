package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.dialogs.ScoreNameInputDialog;

import java.awt.Component;

public class NewPartialCommand extends Command {
	private static final String INTERESTED_DAY = "day";
	
	public NewPartialCommand() {
		this(null);
	}
	
	public NewPartialCommand(Component sourceComp){
		super("NewPartial", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		IDay day = (IDay) getParameter(INTERESTED_DAY);
		IScore newScore = ScoreNameInputDialog.showDialog(day, false);
		
		if(newScore == null){
			return;
		}
		
		try {
			day.addScore(newScore);
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IDay d){
		return createCommand(d, null);
	}

	public static Command createCommand(IDay d, Component sourceComp){
		Command cmd = new NewPartialCommand(sourceComp);
		cmd.setParameter(INTERESTED_DAY, d);
		
		return cmd;
	}
}
