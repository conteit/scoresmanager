package it.conteit.scoresmanager.gui.registry;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IScore;

public interface IRegistryListener {
	public void daySelected(IDay d);
	public void scoreSelected(IScore s, IDay d);
}
