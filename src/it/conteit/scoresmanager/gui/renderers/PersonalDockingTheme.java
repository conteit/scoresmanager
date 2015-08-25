package it.conteit.scoresmanager.gui.renderers;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.*;

public class PersonalDockingTheme extends DockingWindowsTheme {
	private DockingWindowsTheme defTheme;
	public PersonalDockingTheme(){
		super();
		defTheme = new ShapedGradientDockingTheme();
	}
	
	@Override
	public String getName() {
		return "Personal Docking Theme";
	}

	@Override
	public RootWindowProperties getRootWindowProperties() {
		RootWindowProperties props = defTheme.getRootWindowProperties();
		
		return props;
	}

}
