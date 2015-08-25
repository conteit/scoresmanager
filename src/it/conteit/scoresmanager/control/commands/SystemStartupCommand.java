package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.gui.MainFrame;

import java.awt.Component;

import javax.swing.SwingUtilities;

public class SystemStartupCommand extends Command {

	public SystemStartupCommand() {
		this(null);
	}
	
	public SystemStartupCommand(Component sourceComp){
		super("System Startup", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		GrestsManager.intialize();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {					
					MainFrame.getInstance().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Command createCommand(){
		return new SystemStartupCommand();
	}

	public static Command createCommand(Component sourceComp){
		return new SystemStartupCommand(sourceComp);
	}
}
