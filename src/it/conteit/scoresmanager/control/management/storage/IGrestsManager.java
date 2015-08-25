package it.conteit.scoresmanager.control.management.storage;

import java.io.File;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;

/**
 * Interface which represents the list of all grests managed by the application  
 * 
 * @author conteit
 * @version 5.0
 */
public interface IGrestsManager{
	public IGrest createGrest(String name) throws InconsistencyException, StorageException;
	public IGrest createGrest(String name, ITeam[] teams) throws InconsistencyException, StorageException;
	public IGrest importGrest(File file) throws StorageException;
	public void deleteGrest(IGrest g) throws StorageException;
	
	public IGrest[] managedGrests();
	
	public void saveGrest(IGrest g) throws StorageException;
	public void saveAll() throws StorageException;
	
	public void addGrestsManagerListener(IGrestsManagerListener l);
	public void removeGrestsManagerListener(IGrestsManagerListener l);
	
	public boolean nameAlreadyUsed(String name);
}
