package it.conteit.scoresmanager.presentation.sottosopra;

import it.conteit.scoresmanager.data.ITeam;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class TeamEntry extends JPanel {
	private static final long serialVersionUID = -1945797901650874547L;
	private JComboBox comboBox;
	private ITeam team;

	public TeamEntry() {
		this(null, 0);
	}
	
	public TeamEntry(ITeam team, int relatedTeam) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		setBorder(new CompoundBorder(UIManager.getBorder("TitledBorder.aquaVariant"), new EmptyBorder(0, 5, 0, 5)));
		{
			JLabel lblTeamname = new JLabel("TeamName");
			if (team != null){
				this.team = team;
				lblTeamname.setText(team.getName());
			}
			
			lblTeamname.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			add(lblTeamname, "2, 2, right, default");
		}
		{
			comboBox = new JComboBox();
			comboBox.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"Red", "Yellow", "Green", "Blue"}));
			if (relatedTeam >= 0 && relatedTeam <4){
				comboBox.setSelectedIndex(relatedTeam);
			}
			add(comboBox, "4, 2, fill, default");
		}
	}
	
	public Association getAssociation(){
		return new Association(team, comboBox.getSelectedIndex());
	}

}
