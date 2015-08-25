package it.conteit.scoresmanager.data.stats;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

public class StatsDataSet extends AbstractXYDataset implements XYDataset {
	private static final long serialVersionUID = 8834345646424440021L;
	
	private IGrest grest;
	
	public StatsDataSet(IGrest g){
		super();
		
		grest = g;
	}
	
	@Override
	public int getSeriesCount() {
		return grest.teamCount();
	}

	@Override
	public Comparable<?> getSeriesKey(int arg0) {
		return grest.teams()[arg0].getName();
	}

	public int getItemCount(int arg0) {
		return grest.days().length + 1;
	}

	public Number getX(int arg0, int arg1) {
		return arg1;
	}

	public Number getY(int arg0, int arg1) {
		if(arg1 == 0){
			return 0;
		} 
		
		try {
			return grest.getSummaryAt(arg1 - 1).getValue(arg0);
		} catch (InconsistencyException e) {
			ApplicationSystem.getInstance().logWarning(e.getMessage());
			return 0;
		}
	}

}
