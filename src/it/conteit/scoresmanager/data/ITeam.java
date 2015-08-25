package it.conteit.scoresmanager.data;

import java.io.File;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;

/**
 * Interface which represents a team
 * 
 * @author conteit
 * @version 5.0
 */
public interface ITeam extends IAcceptDataVisitor{
	
	/**
	 * Getter
	 * 
	 * @return	The name of the team
	 */
	public String getName();
	
	/**
	 * Setter
	 * 
	 * @param newName	The new value for the team's name
	 * @throws InconsistencyException 
	 */
	public void setName(String newName) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return	The implemented color object
	 */
	public IColor<?> getColor();
	
	/**
	 * Setter
	 * 
	 * @param newColor	The new value for the team's color
	 * @throws InconsistencyException 
	 */
	public void setColor(IColor<?> newColor) throws InconsistencyException;
	
	/**
	 * Getter
	 * 
	 * @return The team's avatar file
	 */
	public File getAvatar();
	
	/**
	 * Setter
	 * 
	 * @param newAvatar The new avatar for the team
	 * @throws InconsistencyException 
	 */
	public void setAvatar(File newAvatar) throws InconsistencyException;
}
