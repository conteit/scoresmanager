package it.conteit.scoresmanager.data;

import java.io.File;
import java.util.ArrayList;

import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;

public class Team implements ITeam {
	private String name;
	private IColor<?> color;
	private File avatar;

	private ArrayList<IDataListener> listeners = new ArrayList<IDataListener>();

	public Team(String name) throws InconsistencyException{
		this(name, new Color(new java.awt.Color(0,0,0)));
	}
	public Team(String name, Color color) throws InconsistencyException{
		if(name == null || name.equals("")){
			throw new InconsistencyException("Invalid argument (name): must be not null and not \"\"");
		}

		if(color == null){
			throw new InconsistencyException("Invalid argument (color): must be not null");
		}

		this.name = name;
		this.color = color;
	}

	public IColor<?> getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object o){
		if(o == this){
			return true;
		}

		if(o instanceof Team){
			return name.equals(((Team) o).getName());
		}

		return false;
	}

	public void setColor(IColor<?> newColor) throws InconsistencyException {
		if(newColor == null){
			throw new InconsistencyException("Invalid argument (color): must be not null");
		}

		this.color = newColor;
		notifyListeners();
	}

	public void setName(String newName) throws InconsistencyException {
		if(newName == null || newName.equals("")){
			throw new InconsistencyException("Invalid argument (name): must be not null and not \"\"");
		}

		this.name = newName;
		notifyListeners();
	}

	public void accept(IDataVisitor v) {
		v.visit(this);
	}

	public String toString(){
		return name;
	}

	public static ITeam create(String name, Color color) throws InconsistencyException{
		return new Team(name, color);
	}

	public static ITeam create(String name) throws InconsistencyException{
		return new Team(name);
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

	public File getAvatar() {
		return avatar;
	}

	public void setAvatar(File newAvatar) throws InconsistencyException {
		if(newAvatar != null){
			if(!newAvatar.exists() || !newAvatar.isFile()){
				throw new InconsistencyException("Invalid argument: not a file");
			}
		}
		avatar = newAvatar;
	}
}
