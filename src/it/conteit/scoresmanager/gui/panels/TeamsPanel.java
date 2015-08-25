package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.AddTeamCommand;
import it.conteit.scoresmanager.control.commands.RemoveTeamCommand;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.Team;
import it.conteit.scoresmanager.gui.models.TeamsListModel;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.gui.valiators.IApplicationComponent;
import it.conteit.scoresmanager.gui.widgets.TeamsList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TeamsPanel extends AbstractApplicationPanel implements ListSelectionListener, IDataListener {
	private static final long serialVersionUID = 504206933544861337L;
	
	private TeamsList list;

	private JLabel warningsLabel;

	private JButton button;

	private IGrest grest;

	private AbstractButton btnAddTeam;

	public TeamsPanel(){
		this(null, null);
	}
	
	/**
	 * Create the panel.
	 */
	public TeamsPanel(IGrest grest, IApplicationComponent parent) {
		this.grest = grest;
		grest.addDataListener(this);
		
		setParent(parent);
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("75dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("5dlu"),}));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(UIManager.getColor("Button.disabledText")));
			panel.setLayout(new BorderLayout(0, 0));
			add(panel, "2, 2, fill, fill");
			{
				JToolBar toolBar = new JToolBar();
				toolBar.setFloatable(false);
				
				panel.add(toolBar, BorderLayout.NORTH);
				{
					btnAddTeam = new JButton("Add team");
					btnAddTeam.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addTeam_actionPerf(e);
						}
					});
					btnAddTeam.setIcon(new ImageIcon(TeamsPanel.class.getResource("/it/conteit/scoresmanager/gui/images/player_new.png")));
					btnAddTeam.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					toolBar.add(btnAddTeam);
				}
				toolBar.addSeparator();
				{
					button = new JButton("");
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeTeam_actionPerf(e);
						}
					});
					button.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					button.setIcon(new ImageIcon(TeamsPanel.class.getResource("/it/conteit/scoresmanager/gui/images/player_cancel.png")));
					toolBar.add(button);
				}
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					if(grest != null){
						list = new TeamsList(grest);
					} else {
						list = new TeamsList();
					}
					list.addListSelectionListener(this);
					scrollPane.setViewportView(list);
				}
			}
		}
		{
			JPanel panel = new EditTeamPanel(list, this);
			list.addListSelectionListener((ListSelectionListener) panel);
			add(panel, "4, 2, fill, fill");
		}
		{
			warningsLabel = new JLabel("Invalid");
			warningsLabel.setForeground(Color.RED);
			warningsLabel.setVisible(false);
			warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			warningsLabel.setIcon(new ImageIcon(EditGrestPanel.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
			add(warningsLabel, "2, 4, 3, 1");
		}
		
		setComponentsEnabled(grest.days().length == 0);
		check();

	}

	protected void addTeam_actionPerf(ActionEvent e) {
		String teamName = "New Team";
		int i=2;

		while(((TeamsListModel) list.getModel()).nameAlreadyUsed(teamName)){
			teamName = "New Team " + i;
			i++;
		}

		try {
			//Add team to grest
			ApplicationSystem.getInstance().execute(AddTeamCommand.createCommand(grest, Team.create(teamName), this));
			check();
		} catch (Exception e1) {
			ApplicationSystem.getInstance().logError(e1.getMessage());
			ApplicationSystem.getInstance().showError("Cannot create new team.\n" + e1.getMessage(), this);
		}
	}

	protected void removeTeam_actionPerf(ActionEvent e) {
		//Remove team from grest
		try {
			ApplicationSystem.getInstance().execute(RemoveTeamCommand.createCommand(grest, list.getSelectedTeam(), this));
		} catch (Exception e1) {
			ApplicationSystem.getInstance().showError("Cannot remove team.\n" + e1.getMessage(), this);
		}
		check();
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {
		if(!isOk){
			String res = new String(validationResult[0]);
			for(int i=1; i<validationResult.length; i++){
				res += ";  ";
				res += validationResult[i];
			}
			warningsLabel.setText(res);
			warningsLabel.setToolTipText(res);
		}
		warningsLabel.setVisible(!isOk);
		button.setEnabled(list.getSelectedIndex() != -1 && list.getModel().getSize() > 2);
	}

	public void valueChanged(ListSelectionEvent e) {
		button.setEnabled(list.getSelectedIndex() != -1 && list.getModel().getSize() > 2);
	}

	private void setComponentsEnabled(boolean enabled){
		btnAddTeam.setEnabled(enabled);
		button.setEnabled(enabled);
	}

	public void dataChanged(DataEvent ev) {
		if(ev.getSource() instanceof IGrest){
			IGrest g = (IGrest) ev.getSource();
			setComponentsEnabled(g.days().length == 0);
		}
	}
}
