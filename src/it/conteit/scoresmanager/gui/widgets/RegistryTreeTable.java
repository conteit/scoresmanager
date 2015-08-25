package it.conteit.scoresmanager.gui.widgets;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.gui.models.DayTreeTableModel;
import it.conteit.scoresmanager.gui.models.RegistryTreeTableModel;
import it.conteit.scoresmanager.gui.registry.IRegistryListener;
import it.conteit.scoresmanager.gui.registry.SpinnerTableCellEditor;
import it.conteit.scoresmanager.gui.renderers.RegistryTreeCellRenderer;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.DayNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.FolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.ScoreNode;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTreeTable;

public class RegistryTreeTable extends JXTreeTable implements TreeSelectionListener{
	private static final long serialVersionUID = 4266741887672033639L;
	
	private DefaultMutableTreeNode selectedNode;
	private ArrayList<IRegistryListener> listeners = new ArrayList<IRegistryListener>();
	
	public RegistryTreeTable(IDay day, IGrest grest){
		super(new DayTreeTableModel(day, grest));
		
		addTreeSelectionListener(this);
		
		setColumnControlVisible(true);
		setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		setTreeCellRenderer(new RegistryTreeCellRenderer());
		
		setDefaultEditor(Integer.class, new SpinnerTableCellEditor());
		
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	public RegistryTreeTable(IGrest grest){
		super(new RegistryTreeTableModel(grest));
		
		addTreeSelectionListener(this);
		
		setColumnControlVisible(true);
		setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		setTreeCellRenderer(new RegistryTreeCellRenderer());
		
		setDefaultEditor(Integer.class, new SpinnerTableCellEditor());
		
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (getTreeSelectionModel().getSelectionCount() != 0) {
			selectedNode = (DefaultMutableTreeNode) getTreeSelectionModel()
			.getSelectionPath().getLastPathComponent();
		}
		
		if(selectedNode instanceof ScoreNode){
			notifyScoreSelection();
		} else {
			notifyDaySelection();
		}
	}

	private void notifyScoreSelection() {
		for(IRegistryListener l : listeners){
			l.scoreSelected(getSelectedScore(), getSelectedDay());
		}
	}

	private void notifyDaySelection() {
		for(IRegistryListener l : listeners){
			l.daySelected(getSelectedDay());
		}
	}

	public IDay getSelectedDay(){
		if(selectedNode != null){
			if(selectedNode instanceof DayNode){
				return (IDay) selectedNode.getUserObject();
			} else if(selectedNode instanceof FolderNode){
				return (IDay) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject();
			} else if(selectedNode instanceof ScoreNode){
				if(selectedNode.getParent() != null && selectedNode.getParent().getParent() != null){
					return (IDay) ((DefaultMutableTreeNode) selectedNode.getParent().getParent()).getUserObject();
				}
				
				return null;
			}
		}

		return null;
	}

	public IScore getSelectedScore(){
		if(selectedNode != null && selectedNode instanceof ScoreNode){
			return (IScore) selectedNode.getUserObject();
		}

		return null;
	}

	public void addRegistryListener(IRegistryListener l) {
		listeners.add(l);
	}
	
	public void removeRegistryListener(IRegistryListener l) {
		listeners.remove(l);
	}
}
