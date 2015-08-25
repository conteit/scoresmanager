package it.conteit.scoresmanager.control.commands;

import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.presentation.AbstractPresentation;

import java.awt.Component;

public class PresentationCommand extends Command {
	private static final String PRESENTATION_CLASSNAME = "className";
	private static final String GREST = "grest";
	
	public PresentationCommand() {
		this(null);
	}
	
	public PresentationCommand(Component sourceComp){
		super("Presentation", sourceComp);
	}

	@Override
	public void execute() throws CommandExecutionException {
		try {
			/*DrawingSystem.intialize(800, 600);
			Animator.initialize(Timeline.loadFromFile(TEST_FILE), DrawingSystem.getInstance().getDrawingSurface());*/
			String presentationClassName = (String) getParameter(PRESENTATION_CLASSNAME);
			AbstractPresentation pres = AbstractPresentation.getInstance(presentationClassName);
			
			pres.loadConfiguration();
			pres.execute((Grest) getParameter(GREST));
			
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
		
	}
	
	public static Command createCommand(Class<? extends AbstractPresentation> presClass, Grest g){
		return createCommand(presClass, g, null);
	}
	
	public static Command createCommand(Class<? extends AbstractPresentation> presClass, Grest g, Component sourceComp){
		Command cmd = new PresentationCommand(sourceComp);
		cmd.setParameter(PRESENTATION_CLASSNAME, presClass.getCanonicalName());
		cmd.setParameter(GREST, g);
		
		return cmd;
	}
}
