package it.conteit.scoresmanager.control.workers;

public interface IProgressListener {
	public void progressUpdate(String operation, String currStatus, int progress, boolean done);
}
