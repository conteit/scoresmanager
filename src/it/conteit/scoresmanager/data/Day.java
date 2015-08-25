package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;

import java.util.ArrayList;

public class Day implements IDay{
	private String desc;
	private ArrayList<IScore> content;
	private int teamCount;
	
	private IGrest grest;
	
	private ArrayList<IDataListener> listeners = new ArrayList<IDataListener>();
	
	public Day(String desc, int teamCount) throws InconsistencyException{
		if(desc == null || desc.equals("")){
			throw new InconsistencyException("Invalid argument (desc): must be not null and not \"\"");
		}
		
		if(teamCount < 2){
			throw new InconsistencyException("Invalid argument (teamCount): at least 2 teams");
		}
		
		this.desc = desc;
		this.teamCount = teamCount;
		this.content = new ArrayList<IScore>();
	}
	
	public int teamCount(){
		return teamCount;
	}
	
	public void addScore(IScore s) throws InconsistencyException{
		if(s.teamCount() != teamCount){
			throw new InconsistencyException("Invalid argument: teamCount properties mismatch");
		}
		
		for(IScore item : content){
			if(item.equals(s)){
				throw new AlreadyAddedException(s);
			}
		}
		
		content.add(s);
		s.setDay(this);
		registerListenersToChild(s);
		notifyListeners();
	}

	public String getDescription() {
		return desc;
	}

	public IScore partialsSum() throws InconsistencyException {
		Score res = new Score("Partials", teamCount);
		
		for(IScore s : content){
			if(s instanceof Partial){
				res.sum(s);
			}
		}
		
		return res;
	}

	public IScore penalitiesSum() throws InconsistencyException {
		Score res = new Score("Penalities", teamCount);
		
		for(IScore s : content){
			if(s instanceof Penality){
				res.sum(s);
			}
		}
		
		return res;
	}

	public void removeScore(IScore s) {
		content.remove(s);
		s.setDay(null);
		unregisterListenersToChild(s);
		notifyListeners();
	}

	public IScore[] scores() {
		IScore[] res = new IScore[content.size()];
		content.toArray(res);
		
		return res;
	}

	public void setDescription(String newDesc) throws InconsistencyException {
		if(newDesc == null || newDesc.equals("")){
			throw new InconsistencyException("Invalid argument: must be not null and not \"\"");
		}
		
		this.desc = newDesc;
		notifyListeners();
	}

	public IScore totalScore() throws InconsistencyException {
		Score res = new Score("Partials", teamCount);
		
		for(IScore s : content){
			res.sum(s);
		}
		
		return res;
	}

	public void accept(IDataVisitor v) {
		v.visit(this);
	}
	
	public String toString(){
		return desc;
	}
	
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		
		if(o instanceof Day){
			return desc.equals(((Day) o).getDescription()) && teamCount() == ((Day) o).teamCount();
		}
		
		return false;
	}
	
	/**
	 * Factory method
	 * 
	 * @param desc		Day's description
	 * @param teamCount	Number of teams involved
	 * @return
	 * @throws InconsistencyException 
	 */
	public static IDay create(String desc, int teamCount) throws InconsistencyException{
		return new Day(desc, teamCount);
	}
	
	public void addDataListener(IDataListener l) {
		listeners.add(l);
		registerListenerToChild(l);
	}

	public void removeDataListener(IDataListener l) {
		listeners.remove(l);
		unregisterListenerToChild(l);
	}
	
	private void notifyListeners(){
		for(IDataListener l : listeners){
			l.dataChanged(new DataEvent(this));
		}
	}
	
	private void registerListenersToChild(IAcceptDataVisitor o){
		for(IDataListener l : listeners){
			o.addDataListener(l);
		}
	}
	
	private void unregisterListenersToChild(IAcceptDataVisitor o){
		for(IDataListener l : listeners){
			o.removeDataListener(l);
		}
	}
	
	private void registerListenerToChild(IDataListener l){		
		for(IScore s : content){
			s.addDataListener(l);
		}
	}
	
	private void unregisterListenerToChild(IDataListener l){
		for(IScore s : content){
			s.removeDataListener(l);
		}
	}

	public IGrest getGrest() {
		return grest;
	}

	public void setGrest(IGrest g) {
		grest = g;
	}
}
