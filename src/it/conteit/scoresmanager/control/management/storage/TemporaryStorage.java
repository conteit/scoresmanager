package it.conteit.scoresmanager.control.management.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class TemporaryStorage implements ITemporaryStorage{
	private File directory;
	private HashMap<String, File> pool;
	private static TemporaryStorage instance;
	private static final String DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/temp";

	public TemporaryStorage(String location) throws StorageException{		
		if(location == null){
			throw new StorageException("Invalid Temporary Folder location");
		}
		
		directory = new File(location);
		if(!directory.exists()){
			if(!directory.isDirectory()){
				throw new StorageException("Invalid Temporary Folder location: not a directory");
			}
			
			try {			
				directory.mkdir();
			} catch (Exception e) {
				throw new StorageException("Cannot obtain access to temp directory");
			}
		}
		
		pool = new HashMap<String, File>();
	}

	public void createTempFile(String id, byte[] data) throws StorageException{
		try {
			File newFile = File.createTempFile("temp", ".tmp", directory);
			newFile.deleteOnExit();

			writeToFile(newFile, data);
			
			pool.put(id, newFile);
		} catch (Exception e) {
			throw new StorageException("Cannot create temporary file");
		}
	}

	private void writeToFile(File newFile, byte[] data) throws Exception{
		FileOutputStream fout = null;
		
		try {
			fout = new FileOutputStream(newFile);
			fout.write(data);
			fout.flush();
		} finally {
			fout.close();
		}
	}

	public void clearTempDir() throws StorageException{
		ArrayList<File> notDeletedFiles = new ArrayList<File>(pool.size());
		for(File f : pool.values()){
			if(!f.delete()){
				notDeletedFiles.add(f);
			}
		}

		if(notDeletedFiles.size() > 0){
			pool.clear();
			File[] fs = new File[notDeletedFiles.size()];
			notDeletedFiles.toArray(fs);

			throw new StorageException("Listed files must be deleted manually", fs);
		}
	}

	public File retrieveTempFile(String id){
		return pool.get(id);
	}

	public static TemporaryStorage getInstance(){
		return getInstance(DEFAULT_DIRECTORY);
	}

	public static TemporaryStorage getInstance(String tempDir){
		if(instance == null){
			try {
				instance = new TemporaryStorage(tempDir);
			} catch (StorageException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		return instance;
	}
}
