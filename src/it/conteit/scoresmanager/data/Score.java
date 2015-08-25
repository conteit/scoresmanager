package it.conteit.scoresmanager.data;

import java.util.ArrayList;

import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;

/**
 * Basic implementation of IScore
 * 
 * @author conteit
 * @version 5.0
 */
public class Score implements IScore{
	private String desc;
	private Integer[] values;
	
	private IDay parent;
	
	private ArrayList<IDataListener> listeners = new ArrayList<IDataListener>();
	
	public Score(String desc, int teamCount) throws InconsistencyException{
		this(desc, new Integer[teamCount]);
		
		for(int i=0; i<values.length; i++){
			values[i] = new Integer(0);
		}
	}
	
	public Score(String desc, Integer[] values) throws InconsistencyException{
		if(desc == null || desc.equals("")){
			throw new InconsistencyException("Invalid argument (desc): must be not null and not \"\"");
		}
		
		if(values == null || values.length < 2){
			throw new InconsistencyException("Invalid argument (values): must be not null and its length must be > 0");
		}
		
		this.desc = desc;
		this.values = values;
	}
	
	public String getDescription() {
		return desc;
	}

	public Integer getValue(int index) {
		return values[index];
	}

	public void setDescription(String newDesc) throws InconsistencyException{
		if(newDesc != null && !newDesc.equals("")){
			desc = newDesc;
		} else throw new InconsistencyException("Invalid argument (newDesc): must be not null");
		
		notifyListeners();
	}

	public void setValue(int index, Integer newValue) throws InconsistencyException{
		if(newValue != null){
			values[index] = newValue;
		} else throw new InconsistencyException("Invalid argument (newValue): must be not null");
		
		notifyListeners();
	}

	public int teamCount() {
		return values.length;
	}
	
	public void subtract(IScore s) throws InconsistencyException {
		if(s.teamCount() != values.length){
			throw new InconsistencyException("Invalid score: " + s);
		}
		
		for(int i = 0; i < values.length; i++){
			values[i] -= s.getValue(i);
		}
	}

	public void sum(IScore s) throws InconsistencyException {
		if(s.teamCount() != values.length){
			throw new InconsistencyException("Invalid score: " + s);
		}
		
		for(int i = 0; i < values.length; i++){
			values[i] += s.getValue(i);
		}
	}
	
	public String toString(){
		return desc;
	}
	
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		
		if(o instanceof Score){
			return desc.equals(((Score) o).getDescription()) && (teamCount() == ((Score) o).teamCount());
		}
		
		return false;
	}
	
	/**
	 * Factory method
	 * 
	 * @param desc		Description of the new score
	 * @param values	Values related to this score
	 */
	public static IScore create(String desc, Integer[] values) throws InconsistencyException{
		return new Score(desc, values);
	}

	/**
	 * Factory method
	 * 
	 * @param desc		Description of the new score
	 * @param teamCount	Number of teams involved
	 */
	public static IScore create(String desc, int teamCount) throws InconsistencyException{
		return new Score(desc, teamCount);
	}

	public void accept(IDataVisitor v) {
		v.visit(this);
	}
	
	public void addDataListener(IDataListener l) {
		listeners.add(l);
	}

	public void removeDataListener(IDataListener l) {
		listeners.remove(l);
	}
	
	private void notifyListeners(){
		for(IDataListener l : listeners){
			l.dataChanged(new DataEvent(this));
		}
	}

	public IDay getDay() {
		return parent;
	}

	public void setDay(IDay day) {
		parent = day;
	}
}
