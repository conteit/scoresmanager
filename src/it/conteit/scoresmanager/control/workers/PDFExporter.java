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

public class PDFExporter extends SwingWorker<Void> implements IStorageListener{
	private IGrest source;
	private File destination;
	private PageOrientation orient;
	
	public PDFExporter(IGrest source, File destination, PageOrientation orient){
		this.source = source;
		this.destination = destination;
		this.orient = orient;
	}
	
	@Override
	public Void construct() {		
		try {
			ExportToDoc.exportToFile(source, destination.getPath(), this, orient, ExportToDoc.PDF);
			
			super.notifyProgress("Exporting grest..", "Completed", 100, true);
		} catch (StorageException e) {
			ApplicationSystem.getInstance().showError("Cannot export grest to " + destination.getPath(), null);
		}
		
		return null;
	}

	public static void export(IGrest source, File destination, PageOrientation orient){
		PDFExporter serializer = PDFExporter.startExporting(source, destination, orient);
		
		while(!serializer.isCompleted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {break;}
		}
	}
	
	public static PDFExporter startExporting(IGrest source, File destination, PageOrientation orient){
		PDFExporter serializer = new PDFExporter(source, destination, orient);
		new ProgressDialog(false, serializer);
		
		serializer.start();
		return serializer;
	}

	public void storageProgress(StorageEvent ev) {
		super.notifyProgress("Exporting grest..", ev.getDescription(), -1, false);
	}
}
