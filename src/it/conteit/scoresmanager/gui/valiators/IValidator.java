package it.conteit.scoresmanager.gui.valiators;

public interface IValidator {
	
	/**
	 * Checks specified constraints and return the result of validation
	 * 
	 * @return A description of the problem, or null if OK
	 */
	public String accept();
}
