package it.conteit.scoresmanager.gui.models;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.gui.widgets.registry.generators.TreeGenerator;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.DayNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.FolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.PartialsFolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.PenalitiesFolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.ScoreNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class RegistryTreeTableModel extends MutableTreeTableModel implements IDataListener {
	private IGrest grest;
	
	public RegistryTreeTableModel(MutableTreeNode root, IGrest grest){
		super(root);
		
		this.grest = grest;
	}
	
	public RegistryTreeTableModel(IGrest grest){
		super(new TreeGenerator(grest).getResult());
		
		this.grest = grest;
		grest.addDataListener(this);
	}

	public IGrest getGrest(){
		return grest;
	}
	
	public int getColumnCount() {
		return grest.teamCount() + 1;
	}
	
	public Class<?> getColumnClass(int column) {
		if(column == 0) {
			return super.getColumnClass(column);
		}
		else {
			return Integer.class;
		}
	}
	
	public String getColumnName(int column) {
		if(column == 0){
			return "Description";
		} else {
			return grest.teams()[column - 1].getName();
		}
	}
	
	public Object getValueAt(Object node, int column) {
		try{
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node;

			if(dmtn instanceof DayNode){
				IDay gd = ((IDay) dmtn.getUserObject());
				switch(column){
				case 0:
					return gd.getDescription();
				default:
					return gd.totalScore().getValue(column - 1);
				}
			} else if(dmtn instanceof FolderNode){
				IDay gd = ((IDay) ((DefaultMutableTreeNode) dmtn.getParent()).getUserObject());
				switch(column){
				case 0:
					return dmtn.getUserObject();
				default:
					if(dmtn instanceof PartialsFolderNode){
						return gd.partialsSum().getValue(column - 1);
					} else if(dmtn instanceof PenalitiesFolderNode){
						return gd.penalitiesSum().getValue(column - 1);
					}
				}
			} else if(dmtn instanceof ScoreNode){
				IScore s = ((IScore) dmtn.getUserObject());
				switch(column){
				case 0:
					return s.getDescription();
				default:
					return s.getValue(column - 1);
				}
			}
		} catch(Exception e){
			ApplicationSystem.getInstance().logError(e.getMessage());
		}
		return null;
	}
	
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	public void dataChanged(DataEvent ev) {
		TreeGenerator tg = new TreeGenerator(ev.getSource());
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getRoot();
		
		if(ev.getSource() instanceof Grest){
			setRoot(tg.getResult());			
		} else {
			for(int i=0; i<node.getChildCount(); i++){
				DefaultMutableTreeNode dayNode = (DefaultMutableTreeNode) node.getChildAt(i); 
				if(ev.getSource() instanceof Day){
					if(dayNode.getUserObject().equals(ev.getSource())){
						removeNodeFromParent((MutableTreeNode) node.getChildAt(i));
						insertNodeInto(tg.getResult(), node, i);
						return;
					}
				} else if(ev.getSource() instanceof Score){
					if(dayNode.getUserObject().equals(((IScore) ev.getSource()).getDay())){
						for(int j=0; j<dayNode.getChildCount(); j++){
							DefaultMutableTreeNode scoreNode = (DefaultMutableTreeNode) dayNode.getChildAt(j);
							if(scoreNode.getUserObject().equals(ev.getSource())){
								removeNodeFromParent((MutableTreeNode) dayNode.getChildAt(j));
								insertNodeInto(tg.getResult(), dayNode, j);
								return;
							}
						}
					}
				}
			}
		}
	}
}
