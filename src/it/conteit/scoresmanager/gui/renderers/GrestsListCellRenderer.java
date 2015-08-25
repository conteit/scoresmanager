package it.conteit.scoresmanager.gui.renderers;

import it.conteit.scoresmanager.data.IGrest;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class GrestsListCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 3368031727253581531L;
	
	private static final String TEXT1 = "<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px;\">\r\t\t\t<b>";
	private static final String TEXT2 = "</b>\r\t\t</div>\r\t\t<div style=\"font-size:8px; color: #666666;\">\r\t\t\t<em>Created on: ";
	private static final String TEXT2_SEL = "</b>\r\t\t</div>\r\t\t<div style=\"font-size:8px;\">\r\t\t\t<em>Created on: ";
	private static final String TEXT3 = "</em>\r\t\t</div>\r\t\t<div style=\"margin-top: 1px;font-size:8px; color: #666666;\">\r\t\t\t<em>Modified on: ";
	private static final String TEXT3_SEL = "</em>\r\t\t</div>\r\t\t<div style=\"margin-top: 1px;font-size:8px;\">\r\t\t\t<em>Modified on: ";
	private static final String TEXT4 = "</em>\r\t\t</div>\r\t</div>\r</html>";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Create the panel.
	 */
	public GrestsListCellRenderer() {
		super("<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px;\">\r\t\t\t<b>Grest Name</b>\r\t\t</div>\r\t\t<div style=\"font-size:8px; color: #666666;\">\r\t\t\t<em>Created on: DD/MM/YYYY HH:MM</em>\r\t\t</div>\r\t\t<div style=\"margin-top: 1px;font-size:8px; color: #666666;\">\r\t\t\t<em>Modified on: DD/MM/YYYY HH:MM</em>\r\t\t</div>\r\t</div>\r</html>");
		setIcon(new ImageIcon(GrestsListCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		IGrest grest = (IGrest) value;
		
		if(isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			setText(TEXT1 + grest.getName() + TEXT2_SEL + dateFormat.format(grest.getCreationDate()) + TEXT3_SEL + dateFormat.format(grest.getLastModDate()) + TEXT4);
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setText(TEXT1 + grest.getName() + TEXT2 + dateFormat.format(grest.getCreationDate()) + TEXT3 + dateFormat.format(grest.getLastModDate()) + TEXT4);
		}
		
		if(grest.getLogo() == null){
			setIcon(new ImageIcon(TeamsListCellRenderer.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
		} else {
			setIcon(obtainImage(grest.getLogo()));
		}
		
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
