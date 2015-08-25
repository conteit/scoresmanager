package it.conteit.scoresmanager.data;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;

/**
 * Interface which describes a color
 * @author conteit
 *
 */
public interface IColor<T> extends IAcceptDataVisitor{
	
	/**
	 * Getter
	 * 
	 * @return	The color as implemented
	 */
	public T getValue();
	
	/**
	 * Factory method
	 * 
	 * @param color	The color implementation
	 * @return		The IColor instance
	 */
	//public IColor<T> create(T color);
}
