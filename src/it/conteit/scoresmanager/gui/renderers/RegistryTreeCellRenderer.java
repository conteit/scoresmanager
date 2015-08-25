package it.conteit.scoresmanager.gui.renderers;

import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.DayNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.FolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.PartialsFolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.PenalitiesFolderNode;
import it.conteit.scoresmanager.gui.widgets.registry.nodes.ScoreNode;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class RegistryTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -4936674876279546194L;

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus){
		Component c=super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	
		((JLabel) c).setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		if(c instanceof JLabel){			
			if(value instanceof ScoreNode){				
				if(((ScoreNode)value).getUserObject() instanceof Penality){
					((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/penality.png")));
				} else if(((ScoreNode)value).getUserObject() instanceof Partial){
					((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/partial.png")));
				} else {
					((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/stats.png")));
				}
				
				((JLabel) c).setText(((Score) ((ScoreNode)value).getUserObject()).getDescription());				
			} else if(value instanceof PartialsFolderNode){
				((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/partials_folder.png")));
				((JLabel) c).setText("<html><i>" + ((String) ((FolderNode) value).getUserObject()) + "</i></html>");
			} else if(value instanceof PenalitiesFolderNode){
				((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/penalities_folder.png")));
				((JLabel) c).setText("<html><i>" + ((String) ((FolderNode) value).getUserObject()) + "</i></html>");	
			} else if(value instanceof DayNode){
				((JLabel) c).setIcon(new ImageIcon(RegistryTreeCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/date.png")));
				((JLabel) c).setText(((Day) ((DayNode) value).getUserObject()).toString());
			}
		}
		
		return c;
	}
}
