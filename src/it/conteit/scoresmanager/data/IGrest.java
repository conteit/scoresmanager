package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;

import java.io.File;
import java.util.Date;

/**
 * Interface which represents a Grest
 * 
 * @author conteit
 * @version 5.0
 */
public interface IGrest extends IAcceptDataVisitor{
	
	/**
	 * Getter
	 * 
	 * @return	The name of the grest
	 */
	public String getName();
	
	/**
	 * Setter
	 * 
	 * @param newName	The new name for the grest
	 * @throws InconsistencyException 
	 */
	public void setName(String newName) throws InconsistencyException;
	
	/**
	 * Modifier
	 * 
	 * @param team	Team to be added
	 * @throws InconsistencyException 
	 */
	public void addTeam(ITeam team) throws InconsistencyException;
	
	/**
	 * Modifier
	 * 
	 * @param team	Team to be removed 
	 * @throws InconsistencyException 
	 */
	public void removeTeam(ITeam team) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return All the teams associated to the grest
	 */
	public ITeam[] teams();
	
	/**
	 * Modifier
	 * 
	 * @param day	The day to be added
	 * @throws InconsistencyException 
	 */
	public void addDay(IDay day) throws InconsistencyException;
	
	/**
	 * Modifier
	 * 
	 * @param day The day to be removed
	 */
	public void removeDay(IDay day);
	
	/**
	 * Getter
	 * 
	 * @return	The days associated to the grest
	 */
	public IDay[] days();
	
	/**
	 * Getter
	 * 
	 * @return The logo of the Grest
	 */
	public File getLogo();
	
	/**
	 * Setter
	 * 
	 * @param f	The new logo of the Grest (null for no logo)
	 * @throws InconsistencyException 
	 */
	public void setLogo(File f) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return Number of teams involved
	 */
	public int teamCount();
	
	/**
	 * Getter
	 * 
	 * @return The day in which Grest was created
	 */
	public Date getCreationDate();
	
	/**
	 * Getter
	 * 
	 * @return The day in which Grest was modified last time
	 */
	public Date getLastModDate();
	
	/**
	 * Setter
	 * 
	 * @param newDate The new date in which Grest was modified
	 */
	public void setLastModDate(Date newDate);
	
	/**
	 * Getter
	 * 
	 * @return The sum of all grest's score
	 * @throws InconsistencyException 
	 */
	public IScore getTotalScore() throws InconsistencyException;
	
	public IScore getSummaryAt(int dayIndex) throws InconsistencyException;
	
	public int compareTo(IGrest o);
}
