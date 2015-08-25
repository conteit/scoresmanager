package it.conteit.scoresmanager.presentation;

import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;

public abstract class AbstractPresentation {
	public AbstractPresentation() {
	}

	public abstract AbstractApplicationPanel configurationPanel();

	public abstract boolean isConfigurable();
	
	public abstract void loadConfiguration() throws PresentationException;

	public abstract void execute(Grest g) throws PresentationException;

	public static AbstractPresentation getInstance(String presentationClassName)
			throws PresentationException {
		try {
			Class<? extends AbstractPresentation> presClass = Class.forName(
					presentationClassName).asSubclass(
					AbstractPresentation.class);
			AbstractPresentation instance = (AbstractPresentation) presClass
					.newInstance();

			return instance;
		} catch (Exception e) {
			throw new PresentationException(e);
		}
	}
}
