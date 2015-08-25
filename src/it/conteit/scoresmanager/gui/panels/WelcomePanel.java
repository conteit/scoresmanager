package it.conteit.scoresmanager.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.SwingConstants;

public class WelcomePanel extends JPanel {
	private static final long serialVersionUID = -246185704500483876L;

	/**
	 * Create the panel.
	 */
	public WelcomePanel() {
		setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			add(scrollPane, BorderLayout.CENTER);
			{
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout(0, 0));
				panel.setBackground(Color.WHITE);
				scrollPane.setViewportView(panel);
				{
					JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT)){
						private static final long serialVersionUID = 2710535488070831563L;

						public void paint(Graphics g){
							super.paint(g);

							Graphics2D g2d = (Graphics2D) g;
							Color currColor = g2d.getColor();

							g2d.setColor(UIManager.getColor("ComboBox.disabledForeground"));
							g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
							g2d.setColor(currColor);
						}
					};
					panel_1.setBackground(new Color(153, 204, 255));
					panel.add(panel_1, BorderLayout.NORTH);
					{
						JLabel lblscoresManagerVersion = new JLabel("<html>\r\t<div style=\"margin: 5px\">\r\t\t<div style=\"margin-bottom\t: 3px; font-size:14px\">\r\t\t\t<b>Scores Manager</b>\r\t\t</div>\r\t\t<div style=\"font-size:9px;\">\r\t\t\tversion 5.0\r\t\t</div>\r\t\t<div style=\"font-size:8px;\">\r\t\t\t&nbsp;\r\t\t</div>\r\t\t<div style=\"font-size:9px;\">\r\t\t\tGrest management and team's scores presentation\r\t\t</div>\r\t</div>\r</html>");
						lblscoresManagerVersion.setBorder(new EmptyBorder(0, 10, 10, 0));
						lblscoresManagerVersion.setIcon(new ImageIcon(WelcomePanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
						panel_1.add(lblscoresManagerVersion);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_1.setLayout(new FormLayout(new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.RELATED_GAP_COLSPEC,
							ColumnSpec.decode("default:grow"),
							FormFactory.RELATED_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							RowSpec.decode("7dlu"),
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,}));
					panel_1.setBackground(Color.WHITE);
					panel.add(panel_1, BorderLayout.CENTER);
					{
						JLabel lblgettingStarted = new JLabel("<html>\r\t<div style=\"font-size: 110%; margin-bottom: 5px;\">\r\t\t<b>Getting started</b>\r\t</div>\r\t<div style=\"margin-left:5px;\">\r\t\tOnce upon a time..\r\t</div>\r</html>");
						lblgettingStarted.setVerticalAlignment(SwingConstants.TOP);
						lblgettingStarted.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
						panel_1.add(lblgettingStarted, "4, 4");
					}
				}
			}
		}

	}

}
