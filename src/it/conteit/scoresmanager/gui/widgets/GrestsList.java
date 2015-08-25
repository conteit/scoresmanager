package it.conteit.scoresmanager.gui.widgets;

import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.control.management.storage.IGrestsManagerListener;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.models.GrestsListModel;
import it.conteit.scoresmanager.gui.renderers.GrestsListCellRenderer;

import javax.swing.JList;

public class GrestsList extends JList implements IGrestsManagerListener {
	private static final long serialVersionUID = 7278349401944339161L;

	public GrestsList(){
		super(new GrestsListModel());
		setCellRenderer(new GrestsListCellRenderer());
		
		GrestsManager.getInstance().addGrestsManagerListener(this);
	}
	
	public IGrest getSelectedGrest(){
		return ((GrestsListModel) getModel()).getGrest(getSelectedIndex());
	}

	public void grestAdded(IGrest g) {
		((GrestsListModel) getModel()).addGrest(g);
	}

	public void grestRemoved(IGrest g) {
		((GrestsListModel) getModel()).removeGrest(g);
	}

	public void grestUpdated(IGrest g) {
		((GrestsListModel) getModel()).updateGrest(g);
	}
}
