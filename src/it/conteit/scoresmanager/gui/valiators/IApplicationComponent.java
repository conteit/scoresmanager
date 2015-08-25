package it.conteit.scoresmanager.gui.valiators;


public interface IApplicationComponent {
	
	/**
	 * Adds a validator for the dialog
	 * 
	 * @param v The new validator
	 */
	public void addValidator(IValidator v);
	
	/**
	 * Removes the specified validator from the dialog's list
	 * 
	 * @param v The validator to be removed
	 */
	public void removeValidator(IValidator v);
	
	/**
	 * Performs a validation of dialog's data using all validators
	 */
	public void check();
	
	/**
	 * Updates the dialog's GUI, after check computation
	 *  
	 * @param validationResult 	Result of check computation
	 * @param isOK				True if OK, false otherwise (validationResult.lentgth == 0)
	 */
	public void updateGUI(String[] validationResult, boolean isOk);
	
	/**
	 * 
	 * @return True if last validation has been succesfull, false otherwise
	 */
	public boolean isContentValid();
	
	public void setParent(IApplicationComponent parent);
}
