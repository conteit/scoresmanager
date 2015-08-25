package it.conteit.scoresmanager.gui.widgets;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TitledSeparator extends JPanel {
	private static final long serialVersionUID = 5601942872551290112L;
	
	private JLabel label;

	public TitledSeparator(){
		this("Title");
	}
	
	/**
	 * Create the panel.
	 */
	public TitledSeparator(String title) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			label = new JLabel(title);
			label.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			add(label, "2, 2");
		}
		{
			JSeparator separator = new JSeparator();
			separator.setBorder(new EmptyBorder(0, 0, 0, 0));
			add(separator, "4, 2");
		}

	}
	
	public void setTitle(String title){
		label.setText(title);
	}

}
