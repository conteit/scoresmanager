package it.conteit.scoresmanager.gui.valiators;

public class ChildIsOkValidator implements IValidator {
	private IApplicationComponent child;
	private String childName;
	
	public ChildIsOkValidator(String childName, IApplicationComponent child){
		this.child = child;
		this.childName = childName;
	}
	
	public String accept() {
		if(child.isContentValid()){
			return null;
		}
		
		return "Something's wrong in \"" + childName + "\"";
	}

}
