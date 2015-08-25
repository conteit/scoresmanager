package it.conteit.scoresmanager.gui.renderers;

import it.conteit.scoresmanager.data.ITeam;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import java.awt.Image;

public class TeamsListCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 3368031727253581531L;
	
	private static final String TEXT1 = "<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px;\">\r\t\t\t<b>";
	private static final String TEXT2 = "</b>\r\t\t</div>\r\t</div>\r</html>";
	
	/**
	 * Create the panel.
	 */
	public TeamsListCellRenderer() {
		super("<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px;\">\r\t\t\t<b>Team Name</b>\r\t\t</div>\r\t</div>\r</html>");
		setIcon(new ImageIcon(TeamsListCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/team_icon.png")));
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		ITeam team = (ITeam) value;
		
		if(isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground((Color) team.getColor().getValue());
		}
		
		if(team.getAvatar() == null){
			setIcon(new ImageIcon(TeamsListCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/team_icon.png")));
		} else {
			setIcon(obtainImage(team.getAvatar()));
		}
		
		setText(TEXT1 + team.getName() + TEXT2);
		
		return this;
	}

	public ImageIcon obtainImage(File source){
		ImageIcon img = new ImageIcon(source.getPath());
		Image image = img.getImage();
		double width, height;
		
		if (img.getIconHeight() > img.getIconWidth()){
			height = 50;
			width = (height / img.getIconHeight()) * img.getIconWidth();
		} else {
			width = 50;
			height = (width / img.getIconWidth()) * img.getIconHeight();
		}
		image = image.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
		
		return new ImageIcon(image);
	}
}
