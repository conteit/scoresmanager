package it.conteit.scoresmanager.control.workers;

import java.io.File;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.storage.IStorageListener;
import it.conteit.scoresmanager.control.management.storage.StorageEvent;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.control.management.storage.XMLWriter;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.ProgressDialog;

public class GrestSerializer extends SwingWorker<Void> implements IStorageListener{
	private IGrest source;
	private File destination;
	
	public GrestSerializer(IGrest source, File destination){
		this.source = source;
		this.destination = destination;
	}
	
	@Override
	public Void construct() {		
		try {
			XMLWriter.serializeToFile(source, destination.getPath(), this);
			
			super.notifyProgress("Saving grest..", "Completed", 100, true);
		} catch (StorageException e) {
			ApplicationSystem.getInstance().showError("Cannot save grest to " + destination.getPath(), null);
		}
		
		return null;
	}

	public static void save(IGrest source, File destination){
		GrestSerializer serializer = GrestSerializer.startSaving(source, destination);
		
		while(!serializer.isCompleted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {break;}
		}
	}
	
	public static GrestSerializer startSaving(IGrest source, File destination){
		GrestSerializer serializer = new GrestSerializer(source, destination);
		new ProgressDialog(false, serializer);
		
		serializer.start();
		return serializer;
	}

	public void storageProgress(StorageEvent ev) {
		super.notifyProgress("Saving grest..", ev.getDescription(), -1, false);
	}
}
