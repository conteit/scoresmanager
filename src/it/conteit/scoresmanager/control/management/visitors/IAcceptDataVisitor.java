package it.conteit.scoresmanager.control.management.visitors;

public interface IAcceptDataVisitor {
	public void accept(IDataVisitor v);
	
	public void addDataListener(IDataListener l);
	public void removeDataListener(IDataListener l);
}
