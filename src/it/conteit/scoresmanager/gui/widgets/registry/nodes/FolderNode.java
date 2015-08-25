package it.conteit.scoresmanager.gui.widgets.registry.nodes;


public class FolderNode extends MyDefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6327110803518791681L;

	public FolderNode(String name){
		super(name);
	}

	@Override
	public boolean isCellEditable(int column) {
		return false;
	}
}
