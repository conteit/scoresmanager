package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;

/**
 * Interface which represents a day composing a IGrest, in a day there can be some IScores
 * 
 * @author conteit
 * @version 5.0
 */
public interface IDay extends IAcceptDataVisitor{
	
	/**
	 * Getter
	 * 
	 * @return Number of teams involved
	 */
	public int teamCount();
	
	/**
	 * Getter
	 * 
	 * @return Description related to the current day
	 */
	public String getDescription();
	
	/**
	 * Setter
	 * 
	 * @param newDesc	New description for the current day
	 * @throws InconsistencyException 
	 */
	public void setDescription(String newDesc) throws InconsistencyException;
	
	/**
	 * Modifier
	 * 
	 * @param s	The score to be added
	 * @throws InconsistencyException teamCount properties mismatch
	 */
	public void addScore(IScore s) throws InconsistencyException;
	
	/**
	 * Modifier
	 * 
	 * @param s The score to be removed
	 */
	public void removeScore(IScore s);
	
	/**
	 * Getter
	 * 
	 * @return The list of scores associated to this day
	 */
	public IScore[] scores();
	
	/**
	 * Converter
	 * 
	 * @return The sum of all the partials
	 * @throws InconsistencyException 
	 */
	public IScore partialsSum() throws InconsistencyException;
	
	/**
	 * Converter
	 * 
	 * @return The sum of all the penality
	 * @throws InconsistencyException 
	 */
	public IScore penalitiesSum() throws InconsistencyException;
	
	/**
	 * Converter
	 * 
	 * @return The sum of all the scores
	 * @throws InconsistencyException 
	 */
	public IScore totalScore() throws InconsistencyException;
	
	IGrest getGrest();
	
	void setGrest(IGrest g);
}
