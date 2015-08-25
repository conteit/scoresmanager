package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IScore;

import java.awt.Component;

public class RemoveScoreCommand extends Command {
	public static final String SCORE_TO_BE_REMOVED = "score";
	public static final String INTERESTED_DAY = "day";

	public RemoveScoreCommand() {
		super("RemoveScore");
	}

	public RemoveScoreCommand(Component sourceComp) {
		super("RemoveScore", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		if(ApplicationSystem.getInstance().showQuestion("Do you really want to remove \"" + ((IScore) getParameter(SCORE_TO_BE_REMOVED)).getDescription() + "\" score from \"" + ((IDay) getParameter(INTERESTED_DAY)).getDescription() + "\" day?", getSource()) == DialogResults.YES){
			IDay day = ((IDay) getParameter(INTERESTED_DAY));
			IScore score = ((IScore) getParameter(SCORE_TO_BE_REMOVED));

			day.removeScore(score);
		}
	}

	public static Command createCommand(IScore score, IDay day){
		return createCommand(score, day, null);
	}

	public static Command createCommand(IScore score, IDay day, Component sourceComp){
		Command cmd = new RemoveScoreCommand(sourceComp);
		cmd.setParameter(SCORE_TO_BE_REMOVED, score);
		cmd.setParameter(INTERESTED_DAY, day);

		return cmd;
	}

}
