package it.conteit.scoresmanager.control.management.storage;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.Command;
import it.conteit.scoresmanager.control.commands.RenameGrestCommand;
import it.conteit.scoresmanager.control.workers.GrestLoader;
import it.conteit.scoresmanager.control.workers.GrestSerializer;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.GrestsFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GrestsManager implements IGrestsManager {
	private final static String DEFAULT_LOCATION = System.getProperty("user.dir") + "/data";
	private String location;

	private ArrayList<IGrest> managedGrests;
	private HashMap<IGrest, File> grestsFiles;
	private ArrayList<IGrestsManagerListener> listeners = new ArrayList<IGrestsManagerListener>();

	private static GrestsManager instance = null;
	
	public GrestsManager() throws StorageException{
		this(DEFAULT_LOCATION);
	}
	
	public GrestsManager(String location) throws StorageException{
		if(location == null){
			throw new StorageException("Invalid Temporary Folder location");
		}
		
		File repository = new File(location);
		if(!repository.exists()){
			if(!repository.isDirectory()){
				throw new StorageException("Invalid Temporary Folder location: not a directory");
			}
			
			try {			
				repository.mkdir();
			} catch (Exception e) {
				throw new StorageException("Cannot obtain access to temp directory");
			}
		}
		
		File[] files = repository.listFiles();
		GrestsFileFilter gff = new GrestsFileFilter();
		managedGrests = new ArrayList<IGrest>();
		grestsFiles = new HashMap<IGrest, File>();
		
		for(int i=0; i<files.length; i++){
			if(gff.accept(files[i]) && files[i].isFile()){
				IGrest g =XMLReader.loadFromFile(files[i]);
				managedGrests.add(g);
				grestsFiles.put(g, files[i]);
			}
		}
		
		this.location = repository.getAbsolutePath();
	}

	public void addGrestsManagerListener(IGrestsManagerListener l) {
		listeners.add(l);
	}

	public IGrest createGrest(String name) throws InconsistencyException, StorageException {
		if(nameAlreadyUsed(name)){
			throw new InconsistencyException("Name \"" + name + "\" already used");
		}
		
		IGrest grest = Grest.create(name);
		managedGrests.add(grest);
		grestsFiles.put(grest, new File(location + generateFileName(grest.getName())));
		try {
			grestsFiles.get(grest).createNewFile();
		} catch (IOException e) {
			throw new InconsistencyException("Cannot create grest's file");
		}
		saveGrest(grest);
		notifyGrestAdded(grest);
		
		return grest;
	}

	public IGrest createGrest(String name, ITeam[] teams) throws InconsistencyException, StorageException {
		if(nameAlreadyUsed(name)){
			throw new InconsistencyException("Name \"" + name + "\" already used");
		}
		
		IGrest grest = Grest.create(name, teams);
		managedGrests.add(grest);
		grestsFiles.put(grest, new File(location + generateFileName(grest.getName())));
		try {
			grestsFiles.get(grest).createNewFile();
		} catch (IOException e) {
			throw new InconsistencyException("Cannot create grest's file");
		}
		saveGrest(grest);
		notifyGrestAdded(grest);
		
		return grest;
	}

	public IGrest[] managedGrests() {
		IGrest[] res = new IGrest[managedGrests.size()];
		managedGrests.toArray(res);
		
		return res;
	}

	public void deleteGrest(IGrest g) throws StorageException {
		if(managedGrests.remove(g)){
			notifyGrestRemoved(g);
			
			File f = grestsFiles.get(g);
			
			if(!f.delete()){
				throw new StorageException("Cannot delete \"" + f + "\" file. Delete it manually.");
			}
		}
	}

	public void removeGrestsManagerListener(IGrestsManagerListener l) {
		listeners.remove(l);
	}
	
	public void saveAll() throws StorageException {
		for(IGrest g : managedGrests){
			saveGrest(g);
		}
	}

	public void saveGrest(IGrest g) throws StorageException {
		GrestSerializer.save(g, grestsFiles.get(g));
		//XMLWriter.serializeToFile(g, grestsFiles.get(g).getPath());
		notifyGrestUpdated(g);
	}
	
	public static GrestsManager getInstance(){
		return getInstance(DEFAULT_LOCATION);
	}
	
	public static GrestsManager getInstance(String location){
		if(instance == null){
			try {
				instance = new GrestsManager(location);
			} catch (StorageException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return instance;
	}
	
	public static void intialize(){
		getInstance();
	}

	private void notifyGrestAdded(IGrest g){
		for(IGrestsManagerListener l : listeners){
			l.grestAdded(g);
		}
	}
	
	private void notifyGrestRemoved(IGrest g){
		for(IGrestsManagerListener l : listeners){
			l.grestRemoved(g);
		}
	}
	
	private void notifyGrestUpdated(IGrest g){
		for(IGrestsManagerListener l : listeners){
			l.grestUpdated(g);
		}
	}
	
	private String generateFileName(String s){
		return "/" + s.replace(' ', '_') + ".grest";
	}

	public IGrest importGrest(File file) throws StorageException {
		IGrest grest = GrestLoader.load(file);
		
		if(nameAlreadyUsed(grest.getName())){
			try {
				Command cmd = RenameGrestCommand.createCommand(grest);
				ApplicationSystem.getInstance().execute(cmd);
				if(!((Boolean) cmd.getReturnValue()).booleanValue()){
					throw new Exception("Name already used");
				}
			} catch (Exception e) {
				throw new StorageException("Cannot import grest (" + e.getMessage() + ")");
			}
		}
		
		managedGrests.add(grest);
		grestsFiles.put(grest, new File(location + generateFileName(grest.getName())));
		saveGrest(grest);
		
		notifyGrestAdded(grest);
		
		return grest;
	}

	public boolean nameAlreadyUsed(String name) {
		for(IGrest g : managedGrests){
			if(g.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
}
