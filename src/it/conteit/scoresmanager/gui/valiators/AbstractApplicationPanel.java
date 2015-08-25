package it.conteit.scoresmanager.gui.valiators;


import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

public abstract class AbstractApplicationPanel extends JPanel implements IApplicationComponent{
	private static final long serialVersionUID = 8449772909008904539L;
	private ArrayList<IValidator> validators = new ArrayList<IValidator>();
	
	private boolean validationOK = true;
	
	private IApplicationComponent parent;
	
	public void addValidator(IValidator v) {
		validators.add(v);
	}

	public void check() {
		LinkedList<String> res = new LinkedList<String>();
		
		for(IValidator v : validators){
			String vRes = v.accept();
			
			if(vRes != null){
				res.offer(vRes);
			}
		}
		
		String[] validationResult = new String[res.size()];
		res.toArray(validationResult);
		
		validationOK = (validationResult.length == 0);
		updateGUI(validationResult, validationOK);
		
		if(parent != null){
			parent.check();
		}
	}

	public void removeValidator(IValidator v) {
		validators.remove(v);
	}

	public void setParent(IApplicationComponent parent){
		this.parent = parent;
	}
	
	public abstract void updateGUI(String[] validationResult, boolean isOk);
	
	public boolean isContentValid(){
		return validationOK;
	}
}
