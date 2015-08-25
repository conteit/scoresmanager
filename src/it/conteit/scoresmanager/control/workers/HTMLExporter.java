package it.conteit.scoresmanager.control.workers;

import java.io.File;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.storage.ExportToDoc;
import it.conteit.scoresmanager.control.management.storage.IStorageListener;
import it.conteit.scoresmanager.control.management.storage.PageOrientation;
import it.conteit.scoresmanager.control.management.storage.StorageEvent;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.ProgressDialog;

public class HTMLExporter extends SwingWorker<Void> implements IStorageListener{
	private IGrest source;
	private File destination;
	
	public HTMLExporter(IGrest source, File destination){
		this.source = source;
		this.destination = destination;
	}
	
	@Override
	public Void construct() {		
		try {
			ExportToDoc.exportToFile(source, destination.getPath(), this, PageOrientation.VERTICAL, ExportToDoc.HTML);
			
			super.notifyProgress("Exporting grest..", "Completed", 100, true);
		} catch (StorageException e) {
			ApplicationSystem.getInstance().showError("Cannot export grest to " + destination.getPath(), null);
		}
		
		return null;
	}

	public static void export(IGrest source, File destination){
		HTMLExporter serializer = HTMLExporter.startExporting(source, destination);
		
		while(!serializer.isCompleted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {break;}
		}
	}
	
	public static HTMLExporter startExporting(IGrest source, File destination){
		HTMLExporter serializer = new HTMLExporter(source, destination);
		new ProgressDialog(false, serializer);
		
		serializer.start();
		return serializer;
	}

	public void storageProgress(StorageEvent ev) {
		super.notifyProgress("Exporting grest..", ev.getDescription(), -1, false);
	}
}
