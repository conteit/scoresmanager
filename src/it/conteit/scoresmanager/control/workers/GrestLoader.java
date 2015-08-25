package it.conteit.scoresmanager.control.workers;

import java.io.File;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.storage.IStorageListener;
import it.conteit.scoresmanager.control.management.storage.StorageEvent;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.control.management.storage.XMLReader;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.ProgressDialog;

public class GrestLoader extends SwingWorker<IGrest> implements IStorageListener{
	private File source;
	
	public GrestLoader(File source){
		this.source = source;
	}
	
	@Override
	public IGrest construct() {		
		try {
			IGrest grest = XMLReader.loadFromFile(source, this);
			
			super.notifyProgress("Loading grest..", "Completed", 100, true);
			return grest;
		} catch (StorageException e) {
			ApplicationSystem.getInstance().showError("Cannot load grest from " + source.getPath(), null);
		}
		
		return null;
	}
	
	public static IGrest load(File source){
		GrestLoader loader = GrestLoader.startLoading(source);
		
		while(!loader.isCompleted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
		return loader.get();
	}

	public static GrestLoader startLoading(File source){
		GrestLoader loader = new GrestLoader(source);
		new ProgressDialog(false, loader);
		
		loader.start();
		return loader;
	}

	public void storageProgress(StorageEvent ev) {
		super.notifyProgress("Loading grest..", ev.getDescription(), -1, false);
	}
}
