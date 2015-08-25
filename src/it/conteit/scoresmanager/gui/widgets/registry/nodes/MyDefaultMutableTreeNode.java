package it.conteit.scoresmanager.gui.widgets.registry.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class MyDefaultMutableTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 5198027110578299086L;

	public MyDefaultMutableTreeNode(Object userObject, boolean allowChildren){
		super(userObject, allowChildren);
	}
	
	public MyDefaultMutableTreeNode(Object userObject){
		super(userObject);
	}
	
	public abstract boolean isCellEditable(int column);
}
