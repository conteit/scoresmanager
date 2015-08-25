package it.conteit.scoresmanager.control.management.storage;

import it.conteit.scoresmanager.data.IGrest;

public interface IGrestsManagerListener {
	public void grestAdded(IGrest g);
	public void grestUpdated(IGrest g);
	public void grestRemoved(IGrest g);
}
