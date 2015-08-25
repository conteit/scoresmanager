package it.conteit.scoresmanager.gui.models;

import it.conteit.scoresmanager.control.IPresentationListListener;
import it.conteit.scoresmanager.presentation.AbstractPresentation;

import javax.swing.DefaultListModel;

public class PresentationModel extends DefaultListModel implements IPresentationListListener {
	private static final long serialVersionUID = -1531502436191535838L;
	
	private Class<? extends AbstractPresentation> clazz;
	
	public PresentationModel(){
		super();
	}
	
	public void defaultPresentationChanged(
			Class<? extends AbstractPresentation> clazz) {
		this.clazz = clazz;
		super.fireContentsChanged(this, 0, super.size());
	}

	public void presentationAdded(Class<? extends AbstractPresentation> clazz, boolean isDefault) {
		super.addElement(clazz);
		if(isDefault){
			isDefaultPresentation(clazz);
		}
	}

	public void presentationRemoved(Class<? extends AbstractPresentation> clazz) {
		super.removeElement(clazz);
	}

	public boolean isDefaultPresentation(
			Class<? extends AbstractPresentation> clazz) {
		return clazz.equals(this.clazz);
	}
	
}
