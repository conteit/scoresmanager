package it.conteit.scoresmanager.gui.widgets.registry.generators;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;
import it.conteit.scoresmanager.data.Color;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.data.Team;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.DayNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.ScoreNode;

public class TreeGenerator implements IDataVisitor {
	private int type;
	
	private DefaultMutableTreeNode currGrestNode = null;
	private DayNode currDayNode = null;
	private ScoreNode currScoreNode = null;
	
	public TreeGenerator(IAcceptDataVisitor rootElement){
		rootElement.accept(this);
		
		if(rootElement instanceof Grest){
			type = 0;
		} else if(rootElement instanceof Day){
			type = 1;
		} else if(rootElement instanceof Score){
			type = 2;
		}
	}
	
	public MutableTreeNode getResult(){
		switch(type){
		case 0:
			return currGrestNode;
		case 1:
			return currDayNode;
		case 2:
			return currScoreNode;
		default:
			return null;
		}
	}
	
	public void visit(Score s) {
		currScoreNode = new ScoreNode(s);
		
		//Misteriosamente si aggiungono da soli
		/*if(currDayNode != null){
			int index = -1;
			if(s instanceof Partial){
				index = 0;
			} else if (s instanceof Penality){
				index = 1;
			}
			//((DefaultMutableTreeNode) currDayNode.getChildAt(index)).add(currScoreNode);
		}*/
	}

	public void visit(Day d) {
		currDayNode = new DayNode(d);
		
		for(int i=0; i<d.scores().length; i++){
			d.scores()[i].accept(this);
		}
		
		if(currGrestNode != null){
			currGrestNode.add(currDayNode);
		}
	}

	public void visit(Grest g) {
		currGrestNode = new DefaultMutableTreeNode();
		
		for(int i=0; i<g.days().length; i++){
			g.days()[i].accept(this);
		}
		
		try {
			currGrestNode.add(new ScoreNode(g.getTotalScore()));
		} catch (InconsistencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(Color color) {}

	public void visit(Team team) {}
}
