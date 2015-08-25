package it.conteit.scoresmanager.gui.widgets.registry.nodes;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;

public class DayNode extends MyDefaultMutableTreeNode {
	private static final long serialVersionUID = -6327110803518791681L;

	public DayNode(IDay gd){
		super(gd);

		add(new PartialsFolderNode());
		add(new PenalitiesFolderNode());

		//Add scores..
		for(int i = 0; i < gd.scores().length; i++){
			IScore s = gd.scores()[i];
			if(s instanceof Partial){
				getPartialsRoot().add(new ScoreNode(s));
			} else if (s instanceof Penality){
				getPenalitiesRoot().add(new ScoreNode(s));
			}
		}
	}

	public MyDefaultMutableTreeNode getPartialsRoot(){
		return (MyDefaultMutableTreeNode) getChildAt(0);
	}

	public MyDefaultMutableTreeNode getPenalitiesRoot(){
		return (MyDefaultMutableTreeNode) getChildAt(1);
	}

	@Override
	public boolean isCellEditable(int column) {
		return false;
	}
}
