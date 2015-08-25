package it.conteit.scoresmanager.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import it.conteit.scoresmanager.gui.IDialogHeader;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

public class DialogHeader extends JPanel implements IDialogHeader{
	private static final long serialVersionUID = -4386497745718595402L;

	private String TITLE = "Title";
	private String DESC = "Description";
	private boolean SHOW_SEPARATOR = true;

	private JLabel imageLabel;
	private JLabel textLabel;

	/**
	 * Create the panel.
	 */
	public DialogHeader() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		{
			imageLabel = new JLabel("");
			imageLabel.setIcon(new ImageIcon(DialogHeader.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
			imageLabel.setBorder(new EmptyBorder(7, 0, 10, 10));
			add(imageLabel, BorderLayout.EAST);
		}
		{
			textLabel = new JLabel("<html>\r\t<div style=\"font-size: 105%; margin-bottom: 3px;\">\r\t\t<b>Title</b>\r\t</div>\r\t<div style=\"margin:left: 10px; font-size: 95%; color: #666666\">\r\t\tDescription\r\t</div>\r</html>");
			textLabel.setBorder(new EmptyBorder(5, 10, 15, 0));
			add(textLabel, BorderLayout.CENTER);
		}

	}

	public void setBackgroundColor(Color bkg) {
		setBackground(Color.WHITE);		
	}

	public void setDescription(String desc) {
		if(desc != null){
			DESC = desc;
		}

		updateText();
	}

	public void setHeight(int h) {
		Dimension d = getPreferredSize();
		d.height = h;
		setPreferredSize(d);
	}

	public void setImage(ImageIcon img) {
		if(img == null){
			imageLabel.setIcon(null);
		} else {
			imageLabel.setIcon(img);
		}
	}

	public void setSeparatorVisibility(boolean isVisible) {
		SHOW_SEPARATOR = isVisible;
	}

	public void setTitle(String title) {
		if(title != null){
			TITLE = title;
		}

		updateText(); 
	}

	public void paint(Graphics g){
		super.paint(g);

		if(SHOW_SEPARATOR){
			Graphics2D g2d = (Graphics2D) g;
			Color currColor = g2d.getColor();
			
			g2d.setColor(UIManager.getColor("ComboBox.disabledForeground"));
			g2d.drawLine(0, getHeight()-2, getWidth(), getHeight()-2);
			g2d.setColor(currColor);
		}
	}

	private void updateText(){
		textLabel.setText("<html>\r\t<div style=\"font-size: 105%; margin-bottom: 3px;\">\r\t\t<b>" + TITLE + "</b>\r\t</div>\r\t<div style=\"margin:left: 10px; font-size: 95%; color: #666666\">\r\t\t" + DESC + "\r\t</div>\r</html>");
	}
}
