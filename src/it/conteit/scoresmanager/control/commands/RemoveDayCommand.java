package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;

import java.awt.Component;

public class RemoveDayCommand extends Command {
	private static final String INTERESTED_GREST = "grest";
	private static final String INTERESTED_DAY = "day";
	
	public RemoveDayCommand() {
		this(null);
	}
	
	public RemoveDayCommand(Component sourceComp){
		super("NewDay", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		if(ApplicationSystem.getInstance().showQuestion("Do you really want to remove \"" + getParameter(INTERESTED_DAY) + 
				"\" day from \"" + getParameter(INTERESTED_GREST) + "\" grest", getSource()) == DialogResults.YES){
			((IGrest) getParameter(INTERESTED_GREST)).removeDay((IDay) getParameter(INTERESTED_DAY));
		}
	}
	
	public static Command createCommand(IGrest g, IDay d){
		return createCommand(g, d, null);
	}

	public static Command createCommand(IGrest g, IDay d, Component sourceComp){
		Command cmd = new RemoveDayCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, g);
		cmd.setParameter(INTERESTED_DAY, d);
		
		return cmd;
	}
}
