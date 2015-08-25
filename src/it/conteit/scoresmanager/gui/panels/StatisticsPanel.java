package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.stats.StatsDataSet;
import it.conteit.scoresmanager.tools.SortedScore;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class StatisticsPanel extends JPanel implements IDataListener {
	private static final long serialVersionUID = 4354862315756383989L;
	private IGrest grest;
	private ChartPanel panel;
	private JDialog d;
	private JLabel label;

	public StatisticsPanel() {
		this(null);
	}

	/**
	 * Create the panel.
	 */
	public StatisticsPanel(IGrest g) {
		this.grest = g;
		g.addDataListener(this);
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:50dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("5dlu"),}));
		{
			{
				JScrollPane scrollPane = new JScrollPane();
				label = new JLabel("");
				label.setBorder(new EmptyBorder(2, 5, 0, 0));
				label.setVerticalAlignment(SwingConstants.TOP);
				label.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				scrollPane.setViewportView(label);
				add(scrollPane, "2, 4, fill, fill");
			}

			createStats();
		}
	}

	private void zoomGraph() {
		d = new JDialog((Frame) null, "", true);
		JFreeChart chart = ChartFactory.createXYLineChart("", "Time (days)",
				"Scores", new StatsDataSet(grest), PlotOrientation.VERTICAL,
				true, true, false);

		JPanel p = new ChartPanel(chart);
		p.setBorder(new EmptyBorder(10, 10, 10, 10));
		d.add(p);
		d.pack();
		d.setLocationRelativeTo(this);

		d.setVisible(true);
	}

	public void dataChanged(DataEvent ev) {
		refreshStats();
	}

	private void createStats() {
		JFreeChart chart = ChartFactory.createXYLineChart("",
				"Time (days)", "Scores", new StatsDataSet(grest),
				PlotOrientation.VERTICAL, true, true, false);

		panel = new ChartPanel(chart);
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				zoomGraph();
			}
		});
		add(panel, "2, 2, fill, fill");

		label.setText("");

		try {
			SortedScore scores = new SortedScore(grest);

			String text = "<html>\r\t<div style=\"margin-bottom: 2px;\">\r\t\t<b>Winner(s): "
					+ scores.getTeamName(0)
					+ "</b>  ("
					+ scores.getValue(0)
					+ ")"
					+ "\r\t</div>\r\t<div>\r\t\tDistances from the winner:\r\t\t<ol style=\"margin-top: 0px;\">\r\t\t\t";

			for (int i = 0; i < scores.teamCount(); i++) {
				text += "<li><b>" + scores.getTeamName(i) + ": "
						+ (scores.getValue(i) - scores.getValue(0)) + "</b>  (" + scores.getValue(i) + ")"
						+ "</li>\r\t\t\t";
			}

			text += "\r\t\t</ol>\r\t</div>\r</html>";

			label.setText(text);
		} catch (InconsistencyException e) {
			ApplicationSystem.getInstance().logError(
					"Cannot compute Stats (Total scores)");
		}
	}

	private void refreshStats() {
		if(d != null){
			d.setVisible(false);
		}
		if(panel != null){
			remove(panel);
		}

		createStats();
	}
}
