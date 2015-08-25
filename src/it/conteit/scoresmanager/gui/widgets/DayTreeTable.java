package it.conteit.scoresmanager.gui.widgets;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;

public class DayTreeTable extends RegistryTreeTable {
	private static final long serialVersionUID = -7793773945116851143L;
	private IDay day;

	public DayTreeTable(IDay day, IGrest grest) {
		super(day, grest);
		
		this.day = day;
		setRootVisible(true);
	}
	
	public IDay getSelectedDay(){
		return day;
	}
}
