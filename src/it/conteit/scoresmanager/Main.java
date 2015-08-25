package it.conteit.scoresmanager;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.SystemStartupCommand;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationSystem.initialize(args);
		try {
			ApplicationSystem.getInstance().execute(SystemStartupCommand.createCommand());
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot startup Scores Manager application.\nIf problem persist try to reinstall.", null);
		}
	}

}
