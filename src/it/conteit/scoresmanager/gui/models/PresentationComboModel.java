package it.conteit.scoresmanager.gui.models;

import it.conteit.scoresmanager.control.IPresentationListListener;
import it.conteit.scoresmanager.presentation.AbstractPresentation;

import javax.swing.DefaultComboBoxModel;

public class PresentationComboModel extends DefaultComboBoxModel implements IPresentationListListener {
	private static final long serialVersionUID = -3437024103906598571L;
	
	private Class<? extends AbstractPresentation> clazz;
	
	public PresentationComboModel(){
		super();
	}
	
	public void defaultPresentationChanged(
			Class<? extends AbstractPresentation> clazz) {
		this.clazz = clazz;
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
