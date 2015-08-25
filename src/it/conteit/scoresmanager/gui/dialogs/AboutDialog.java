package it.conteit.scoresmanager.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog {
	private static final long serialVersionUID = -7968765388962059947L;

	public AboutDialog() {
		super((Frame) null, true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel(){
				private static final long serialVersionUID = -724439529477784353L;

				public void paint(Graphics g){
					super.paint(g);

					Graphics2D g2d = (Graphics2D) g;
					Color currColor = g2d.getColor();

					g2d.setColor(UIManager.getColor("ComboBox.disabledForeground"));
					g2d.drawLine(0, getHeight()-2, getWidth(), getHeight()-2);
					g2d.setColor(currColor);

				}
			};
			
			panel.setBackground(Color.WHITE);
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JLabel lblscoresManagerVersion = new JLabel("<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px; font-size:14px\">\r\t\t\t<b>Scores Manager</b>\r\t\t</div>\r\t\t<div style=\"font-size:9px;\">\r\t\t\tversion 5.0\r\t\t</div>\r\t\t<div style=\"font-size:8px;\">\r\t\t\t&nbsp;\r\t\t</div>\r\t\t<div style=\"font-size:9px;\">\r\t\t\tGrest management and team's scores presentation\r\t\t</div>\r\t</div>\r</html>");
				lblscoresManagerVersion.setBorder(new EmptyBorder(10, 0, 10, 0));
				lblscoresManagerVersion.setIcon(new ImageIcon(AboutDialog.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
				panel.add(lblscoresManagerVersion);
			}
		}
		{
			//JLabel lblThisSoftwareWas = new JLabel("<html>\r\t<div>\r\t\tThis software was developed by Paolo Contessi. \r\t</div>\r\t<div>\r\t\tFor trubleshooting send and e-mail to <span style=\"color:#0000FF\">conteit86@hotmail.com</span>\r\t</div>\r</html>");
			JLabel lblThisSoftwareWas = new JLabel("<html>\r\t<div>\r\t\tThis software was developed by Paolo Contessi. \r\t</div>\r\t<div>\r\t\tFor trubleshooting send an e-mail to <a href=\"mailto:conteit86@hotmail.com\">conteit86@hotmail.com</a>\r\t</div>\r</html>");
			lblThisSoftwareWas.setBorder(new EmptyBorder(5, 10, 5, 0));
			lblThisSoftwareWas.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			getContentPane().add(lblThisSoftwareWas, BorderLayout.CENTER);
		}
		setTitle("About Scores Manager");
		setBounds(100, 100, 386, 177);

		Dimension screensize = java.awt.Toolkit.getDefaultToolkit()
		.getScreenSize();
		int xPos= new Double((screensize.getWidth() - 286) / 2).intValue();
		int yPos = new Double((screensize.getHeight() / 2) - 200).intValue();
		setLocation(xPos, yPos);
		setResizable(false);
	}

}
