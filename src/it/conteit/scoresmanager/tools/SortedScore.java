package it.conteit.scoresmanager.tools;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.InconsistencyException;

public class SortedScore {
	private IGrest grest;
	private IScore s;
	private int[] a;
	private int nElems;

	public SortedScore(IGrest grest) throws InconsistencyException{
		this.grest = grest;
		s = grest.getTotalScore();
		a = new int[s.teamCount()];
		nElems = s.teamCount();
		
		for(int i=0; i<nElems; i++){
			a[i] = s.getValue(i);
		}
	}

	public int getValue(int placement){
		return a[nElems - placement - 1];
	}
	
	public String getTeamName(int placement) {
		for (int i = 0; i < s.teamCount(); i++) {
			if (s.getValue(i).intValue() == getValue(placement)) {
				return grest.teams()[i].getName();
			}
		}

		return "";
	}

	public int teamCount(){
		return s.teamCount();
	}
	
	public void bubbleSort(){
		int out, in;
		for(out=nElems-1; out>1; out--) // outer loop (backward)
			for(in=0; in<out; in++) // inner loop (forward)
				if( a[in] < a[in+1] ) // out of order?
					swap(in, in+1); // swap them
	} // end bubbleSort()

	private void swap(int one, int two){
		int temp = a[one];
		a[one] = a[two];
		a[two] = temp;
	}
}
