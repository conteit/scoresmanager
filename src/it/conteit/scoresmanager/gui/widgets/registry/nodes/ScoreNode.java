package it.conteit.scoresmanager.gui.widgets.registry.nodes;

import it.conteit.scoresmanager.data.IScore;

public class ScoreNode extends MyDefaultMutableTreeNode {
	private static final long serialVersionUID = -7663650819636522395L;

	public ScoreNode(IScore s){
		super(s, false);
	}

	@Override
	public boolean isCellEditable(int column) {
		if(column == 0){
			return false;
		} else {
			return true;
		}
	}
}
