package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Grest implements IGrest, Comparable<IGrest>{
	private String name;
	private ArrayList<ITeam> teams;
	private ArrayList<IDay> content;

	private Date creationDate;
	private Date lastmodDate;
	private File logo;

	private ArrayList<IDataListener> listeners = new ArrayList<IDataListener>();

	public Grest(String name, ITeam[] teams, Date creationDate) throws InconsistencyException {
		if(name == null || name.equals("")){
			throw new InconsistencyException("Invalid argument (name): must be not null and not \"\"");
		}

		this.name = name;
		this.logo = null;
		this.content = new ArrayList<IDay>();
		this.teams = new ArrayList<ITeam>();
		for(int i=0; i<teams.length; i++){
			this.teams.add(teams[i]);
		}

		int c = 1;
		for(int i=teams.length; i<2; i++){
			boolean done = false;
			while(!done){
				try{
					addTeam(Team.create("Team" + c));
					done = true;
				} catch(Exception e){
					c++;
				}
			}
			c++;
		}

		this.creationDate = creationDate;
		this.lastmodDate = creationDate;
	}

	public Grest(String name, ITeam[] teams) throws InconsistencyException {
		this(name, teams, new Date());
	}

	public Grest(String name) throws InconsistencyException {
		this(name, new ITeam[0]);
	}

	public Grest(String name, Date creationDate) throws InconsistencyException {
		this(name, new ITeam[0], creationDate);
	}

	public void addDay(IDay day) throws InconsistencyException {
		if(day.teamCount() != teams.size()){
			throw new InconsistencyException("Invalid argument (day): teamCount properties mismatch");
		}

		for(IDay item : content){
			if(item.equals(day)){
				throw new AlreadyAddedException(day);
			}
		}

		content.add(day);
		day.setGrest(this);
		registerListenersToChild(day);
		updateLastModDate();
	}

	public void addTeam(ITeam team) throws InconsistencyException {
		if(content.size() > 0){
			throw new InconsistencyException("If Grest has at least one day no team can be added");
		}

		for(ITeam item : teams){
			if(item.equals(team)){
				throw new AlreadyAddedException(team);
			}
		}

		teams.add(team);
		registerListenersToChild(team);
		updateLastModDate();
	}

	public IDay[] days() {
		IDay[] res = new IDay[content.size()];
		content.toArray(res);

		return res;
	}

	public String getName() {
		return name;
	}

	public void removeDay(IDay day) {
		content.remove(day);
		day.setGrest(null);
		unregisterListenersToChild(day);
		updateLastModDate();
	}

	public void removeTeam(ITeam team) throws InconsistencyException {
		if(content.size() > 0){
			throw new InconsistencyException("If Grest has at least one day no team can be removed");
		}

		if(teams.size() == 2){
			throw new InconsistencyException("Cannot have less than 2 teams");
		}
		
		teams.remove(team);
		unregisterListenersToChild(team);
		updateLastModDate();
	}

	public void setName(String newName) throws InconsistencyException {
		if(newName == null || newName.equals("")){
			throw new InconsistencyException("Invalid argument (name): must be not null and not \"\"");
		}
		
		/*if(GrestsManager.getInstance().nameAlreadyUsed(newName)){
			throw new InconsistencyException("Invalid argument (name): already used");
		}*/

		this.name = newName;
		updateLastModDate();
	}

	public ITeam[] teams() {
		ITeam[] res = new ITeam[teams.size()];
		teams.toArray(res);

		return res;
	}

	public int teamCount(){
		return teams.size();
	}

	public void accept(IDataVisitor v) {
		v.visit(this);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastModDate() {
		return lastmodDate;
	}

	public void setLastModDate(Date newDate) {
		lastmodDate = newDate;
		notifyListeners();
	}

	public String toString(){
		return name;
	}

	public boolean equals(Object o){
		if(o == this){
			return true;
		}

		if(o instanceof Grest){
			return name.equals(((Grest) o).getName()) && teams.size() == ((Grest) o).teamCount() &&
			creationDate.equals(((Grest) o).getCreationDate());
		}

		return false;
	}

	public int compareTo(IGrest o) {
		return creationDate.compareTo(o.getCreationDate());
	}

	public static IGrest create(String name) throws InconsistencyException{
		return new Grest(name);
	}

	public static IGrest create(String name, ITeam[] teams) throws InconsistencyException{
		return new Grest(name, teams);
	}

	public static IGrest create(String name, Date creationDate) throws InconsistencyException{
		return new Grest(name, creationDate);
	}

	public static IGrest create(String name, ITeam[] teams, Date creationDate) throws InconsistencyException{
		return new Grest(name, teams, creationDate);
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

	private void updateLastModDate(){
		setLastModDate(new Date());
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
		for(ITeam t : teams){
			t.addDataListener(l);
		}

		for(IDay d : content){
			d.addDataListener(l);
		}
	}

	private void unregisterListenerToChild(IDataListener l){
		for(ITeam t : teams){
			t.removeDataListener(l);
		}

		for(IDay d : content){
			d.removeDataListener(l);
		}
	}

	public File getLogo() {
		return logo;
	}

	public void setLogo(File f) throws InconsistencyException {
		if(f != null){
			if(!f.exists() || !f.isFile()){
				throw new InconsistencyException("Invalid argument: not a file");
			}
		}
		
		this.logo = f;
		updateLastModDate();
	}

	public IScore getTotalScore() throws InconsistencyException {
		Score res = new Score("Total", teams.size());
		
		for(IDay d : content){
			res.sum(d.totalScore());
		}
		
		return res;
	}
	
	public IScore getSummaryAt(int dayIndex) throws InconsistencyException{
		IScore res = new Score("Total", teams.size());
		
		for(int i = 0; i <= dayIndex && i < content.size(); i++){
			res.sum(content.get(i).totalScore());
		}
		
		return res;
	}
}
