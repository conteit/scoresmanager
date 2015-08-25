package it.conteit.scoresmanager.control.management.storage;

import java.io.File;

/**
 * Interface for Temporary files management
 * 
 * @author conteit
 * @version 5.0
 */
public interface ITemporaryStorage {
	
	/**
	 * Modifier
	 * 
	 * @param id				An identifier usefull to recall the temporary file
	 * @param data				Initial data to write on the temporary file
	 * @throws StorageException	Creation not successfull
	 */
	public void createTempFile(String id, byte[] data) throws StorageException;
	
	/**
	 * Modifier
	 * Deletes all temporary files referenced.
	 * 
	 * @throws StorageException	Some files must be deleted manually
	 */
	public void clearTempDir() throws StorageException;
	
	/**
	 * Getter
	 * 
	 * @param id	Identifier for the wanted temporary file
	 * @return		The requested files
	 */
	public File retrieveTempFile(String id);
}
