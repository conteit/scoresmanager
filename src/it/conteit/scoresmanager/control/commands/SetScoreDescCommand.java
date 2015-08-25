package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class SetScoreDescCommand extends Command {
	private static final String VALUE = "desc";
	private static final String INTERESTED_SCORE = "score";
	
	public SetScoreDescCommand() {
		this(null);
	}
	
	public SetScoreDescCommand(Component sourceComp){
		super("SetScoreDesc", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			((IScore) getParameter(INTERESTED_SCORE)).setDescription((String) getParameter(VALUE));
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IScore s, String desc){
		return createCommand(s, desc, null);
	}

	public static Command createCommand(IScore s, String desc, Component sourceComp){
		Command cmd = new SetScoreDescCommand(sourceComp);
		cmd.setParameter(INTERESTED_SCORE, s);
		cmd.setParameter(VALUE, desc);
		
		return cmd;
	}
}
