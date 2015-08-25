package it.conteit.scoresmanager.control;

import it.conteit.scoresmanager.presentation.AbstractPresentation;

public interface IPresentationListListener {
	public void presentationAdded(Class<? extends AbstractPresentation> clazz, boolean isDefault);
	public void presentationRemoved(Class<? extends AbstractPresentation> clazz);
	public void defaultPresentationChanged(Class<? extends AbstractPresentation> clazz);
	public boolean isDefaultPresentation(Class<? extends AbstractPresentation> clazz);
}
