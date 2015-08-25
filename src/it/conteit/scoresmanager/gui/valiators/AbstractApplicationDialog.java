package it.conteit.scoresmanager.gui.valiators;


import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JDialog;

public abstract class AbstractApplicationDialog extends JDialog implements IApplicationComponent{
	private static final long serialVersionUID = -6649481877565245715L;
	private ArrayList<IValidator> validators = new ArrayList<IValidator>();
	
	protected boolean validationOK = true;
	
	private IApplicationComponent parent;
	
	public AbstractApplicationDialog(){
		this((Frame) null);
	}
	
	public AbstractApplicationDialog(Dialog owner){
		super(owner, true);
		setModal(true);
	}
	
	public AbstractApplicationDialog(Frame owner){
		super(owner, true);
		setModal(true);
	}
	
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

	public abstract void updateGUI(String[] validationResult, boolean isOk);
	
	public boolean isContentValid(){
		return validationOK;
	}
	
	public void setParent(IApplicationComponent parent){
		this.parent = parent;
	}
}
