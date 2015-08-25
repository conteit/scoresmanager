package it.conteit.scoresmanager.control.management.storage;

public interface IStorage {
	public void addStorageListener(IStorageListener l);
	public void removeStorageListener(IStorageListener l);
}
