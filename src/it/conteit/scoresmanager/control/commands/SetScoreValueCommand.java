package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class SetScoreValueCommand extends Command {
	private static final String VALUE = "value";
	private static final String INDEX = "teamIndex";
	private static final String INTERESTED_SCORE = "score";
	
	public SetScoreValueCommand() {
		this(null);
	}
	
	public SetScoreValueCommand(Component sourceComp){
		super("SetScoreValue", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			((IScore) getParameter(INTERESTED_SCORE)).setValue(((Integer) getParameter(INDEX)).intValue(), (Integer) getParameter(VALUE));
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IScore s, int teamIndex, Integer value){
		return createCommand(s, teamIndex, value, null);
	}

	public static Command createCommand(IScore s, int teamIndex, Integer value, Component sourceComp){
		Command cmd = new SetScoreValueCommand(sourceComp);
		cmd.setParameter(INTERESTED_SCORE, s);
		cmd.setParameter(INDEX, teamIndex);
		cmd.setParameter(VALUE, value);
		
		return cmd;
	}
}
