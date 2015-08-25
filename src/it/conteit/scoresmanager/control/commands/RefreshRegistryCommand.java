package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;

import java.awt.Component;

public class RefreshRegistryCommand extends Command {
	private static final String INTERESTED_GREST = "grest";
	
	public RefreshRegistryCommand() {
		this(null);
	}
	
	public RefreshRegistryCommand(Component sourceComp){
		super("RefreshDay", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			String name = ((IGrest) getParameter(INTERESTED_GREST)).getName();
			((IGrest) getParameter(INTERESTED_GREST)).setName(name);
		} catch (InconsistencyException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	public static Command createCommand(IGrest d){
		return createCommand(d, null);
	}

	public static Command createCommand(IGrest d, Component sourceComp){
		Command cmd = new RefreshRegistryCommand(sourceComp);
		cmd.setParameter(INTERESTED_GREST, d);
		
		return cmd;
	}
}
