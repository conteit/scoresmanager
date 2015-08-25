package it.conteit.scoresmanager.gui.models;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.SetDayDescCommand;
import it.conteit.scoresmanager.control.commands.SetScoreDescCommand;
import it.conteit.scoresmanager.control.commands.SetScoreValueCommand;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.gui.widgets.registry.generators.TreeGenerator;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.MyDefaultMutableTreeNode;

public class DayTreeTableModel extends RegistryTreeTableModel {
	private IDay day;

	public DayTreeTableModel(IDay day, IGrest grest) {
		super(new TreeGenerator(day).getResult(), grest);

		day.addDataListener(this);
		this.day = day;
	}

	public IDay getDay(){
		return day;
	}

	public boolean isCellEditable(Object node, int column) {
		return ((MyDefaultMutableTreeNode) node).isCellEditable(column);
	}

	public void setValueAt(Object value, Object node, int column){
		Object obj = ((MyDefaultMutableTreeNode) node).getUserObject();

		if(obj instanceof IScore){
			IScore s = (IScore) obj;
			
			if(value instanceof Integer){
				try {
					ApplicationSystem.getInstance().execute(SetScoreValueCommand.createCommand(s, column - 1, (Integer) value));
				} catch (Exception e) {
					ApplicationSystem.getInstance().showError("Error while updating score: " + e.getMessage(), null);
				}
			} else {
				try {
					if(!((String) value).equals("")){
						ApplicationSystem.getInstance().execute(SetScoreDescCommand.createCommand(s, (String) value));
					} else {
						ApplicationSystem.getInstance().showWarning("Invalid score name: probably already used", null);
					}
				} catch (Exception e) {
					ApplicationSystem.getInstance().showError("Error while updating score's name: " + e.getMessage(), null);
				}
			}
		} else if(obj instanceof IDay){
			IDay d = (IDay) obj;
			try {
				if(!((String) value).equals("")){
					ApplicationSystem.getInstance().execute(SetDayDescCommand.createCommand(d, (String) value));
				} else {
					ApplicationSystem.getInstance().showWarning("Invalid day name: probably already used", null);
				}
			} catch (Exception e) {
				ApplicationSystem.getInstance().showError("Error while updating day: " + e.getMessage(), null);
			}
		}
	}

	public void dataChanged(DataEvent ev) {
		TreeGenerator tg = new TreeGenerator(ev.getSource());
		DefaultMutableTreeNode dayNode = (DefaultMutableTreeNode) getRoot();

		if(ev.getSource() instanceof Day){
			setRoot(tg.getResult());			
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
