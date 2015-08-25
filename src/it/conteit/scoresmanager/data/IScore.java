package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;

/**
 * Basic interface which describes an IDay's entry
 * 
 * @author conteit
 * @version 5.0
 */
public interface IScore extends IAcceptDataVisitor{
	
	/**
	 * Getter
	 * 
	 * @param index	Index related to the interested player
	 * @return		The requested value
	 */
	public Integer getValue(int index);
	
	/**
	 * Setter
	 * 
	 * @param index		Index related to the interested player
	 * @param newValue	The new value for the score
	 * @throws InconsistencyException	Invalid argument
	 */
	public void setValue(int index, Integer newValue) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return Number of team's involved
	 */
	public int teamCount();
	
	/**
	 * Getter
	 * 
	 * @return Description related to the score
	 */
	public String getDescription();
	
	/**
	 * Setter
	 * 
	 * @param newDesc	The new description for the score
	 * @throws InconsistencyException	Invalid argument
	 */
	public void setDescription(String newDesc) throws InconsistencyException;

	/**
	 * Modifier
	 * 
	 * @param s	Score to sum
	 * @throws InconsistencyException	Invalid score
	 */
	void sum(IScore s) throws InconsistencyException;

	/**
	 * Modifier
	 * 
	 * @param s	Score to subtract
	 * @throws InconsistencyException	Invalid score
	 */
	void subtract(IScore s) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return	The day in which this score is contained
	 */
	IDay getDay();
	
	/**
	 * Setter
	 * 
	 * @param day	
	 */
	void setDay(IDay day);
}
