package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.CreateGrestCommand;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.Team;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.ImagesFileChooser;
import it.conteit.scoresmanager.gui.models.TeamsListModel;
import it.conteit.scoresmanager.gui.panels.EditTeamPanel;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationDialog;
import it.conteit.scoresmanager.gui.valiators.TeamsListValidator;
import it.conteit.scoresmanager.gui.valiators.TextFieldNotEmptyValidator;
import it.conteit.scoresmanager.gui.valiators.UniqueInGrestsListValidator;
import it.conteit.scoresmanager.gui.widgets.DialogHeader;
import it.conteit.scoresmanager.gui.widgets.TeamsList;
import it.conteit.scoresmanager.tools.GuiUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class NewGrestDialog extends AbstractApplicationDialog implements ListSelectionListener {
	private static final long serialVersionUID = 2685992999838795120L;

	private final JPanel m_contentPanel = new JPanel();
	private JTextField grestNameTextEdit;
	private JTextField logoTextEdit;
	private JLabel grestImageLabel;
	private TeamsList teamsList;
	private JButton okButton;
	private JLabel warningsLabel;

	private JButton removeTeamButton;

	public NewGrestDialog(){
		super((Frame) null);
		createDialog();
	}
	
	public NewGrestDialog(Dialog owner){
		super(owner);
		createDialog();
	}

	public NewGrestDialog(Frame owner){
		super(owner);
		createDialog();
	}

	/**
	 * Create the dialog.
	 */
	public void createDialog() {
		setTitle("New Grest");
		setBounds(100, 100, 569, 496);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		m_contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("53dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:40dlu"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("bottom:10dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		m_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(m_contentPanel, BorderLayout.CENTER);

		grestImageLabel = new JLabel("");
		grestImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		grestImageLabel.setIcon(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
		m_contentPanel.add(grestImageLabel, "2, 2, 1, 3");

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		m_contentPanel.add(lblName, "4, 2, right, default");

		grestNameTextEdit = new JTextField("New Grest");
		grestNameTextEdit.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				grestNameTextEdit_keyEvent(e);
			}
			public void keyPressed(KeyEvent e) {
				grestNameTextEdit_keyEvent(e);
			}
		});
		grestNameTextEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		m_contentPanel.add(grestNameTextEdit, "6, 2, fill, default");
		grestNameTextEdit.setColumns(10);

		JLabel lblLogo = new JLabel("Logo:");
		lblLogo.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		m_contentPanel.add(lblLogo, "4, 4, right, default");

		logoTextEdit = new JTextField();
		logoTextEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		logoTextEdit.setEditable(false);
		m_contentPanel.add(logoTextEdit, "6, 4, fill, default");
		logoTextEdit.setColumns(10);

		JButton logoBrowseButton = new JButton("Browse..");
		logoBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoBrowseButton_actionPerformed(e);
			}
		});
		logoBrowseButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		m_contentPanel.add(logoBrowseButton, "8, 4");

		JSeparator separator = new JSeparator();
		m_contentPanel.add(separator, "2, 6, 7, 1");

		JPanel panel = new JPanel();
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("75dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		m_contentPanel.add(panel, "2, 8, 7, 1, fill, fill");

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(UIManager.getColor("Button.disabledText")));
		panel_2.setLayout(new BorderLayout(0, 0));
		panel.add(panel_2, "2, 2, fill, fill");

		JToolBar panel_3 = new JToolBar();
		panel_3.setFloatable(false);
		panel_2.add(panel_3, BorderLayout.NORTH);

		JButton addTeamButton = new JButton("Add team");
		addTeamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTeamButton_actionPerformed(e);
			}
		});
		addTeamButton.setIcon(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/player_new.png")));
		addTeamButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_3.add(addTeamButton);

		panel_3.addSeparator();
		
		removeTeamButton = new JButton("");
		removeTeamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTeamButton_actionPerformed(e);
			}
		});
		removeTeamButton.setEnabled(false);
		removeTeamButton.setIcon(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/player_cancel.png")));
		removeTeamButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_3.add(removeTeamButton);

		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);

		teamsList = new TeamsList();
		teamsList.addListSelectionListener(this);
		scrollPane.setViewportView(teamsList);

		JPanel teamEditPane = new EditTeamPanel(teamsList, this);
		panel.add(teamEditPane, "4, 2, fill, fill");
		teamEditPane.setVisible(true);
		teamsList.addListSelectionListener((ListSelectionListener) teamEditPane);
		{
			JPanel headerPane = new JPanel(new BorderLayout());
			getContentPane().add(headerPane, BorderLayout.NORTH);

			JPanel footerPane = new JPanel(new BorderLayout());
			getContentPane().add(footerPane, BorderLayout.SOUTH);

			JPanel warningsPane = new JPanel(new BorderLayout());
			warningsPane.setBorder(new EmptyBorder(0,12,0,0));
			footerPane.add(warningsPane, BorderLayout.CENTER);

			warningsLabel = new JLabel("Invalid Grest name: already used");
			warningsLabel.setForeground(Color.RED);
			warningsLabel.setVisible(false);
			warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			warningsLabel.setIcon(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
			warningsPane.add(warningsLabel, BorderLayout.CENTER);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			footerPane.add(buttonPane, BorderLayout.EAST);
			{
				okButton = new JButton("Create");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButton_actionPerformed(e);
					}
				});
				okButton.setEnabled(false);
				getRootPane().setDefaultButton(okButton);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButton_actionPerformed(e);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}


			DialogHeader header = new DialogHeader();
			header.setTitle("Create new Grest");
			header.setDescription("Specify a name, a logo and the involved teams, then press Create.");
			header.setImage(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/grest_create.png")));
			headerPane.add(header, BorderLayout.CENTER);
		}
		
		setLocation(GuiUtils.centerScreenFrame(getSize()));

		addValidator(new TextFieldNotEmptyValidator("Grest Name", grestNameTextEdit));
		addValidator(new TeamsListValidator("Teams", teamsList));
		
		addValidator(new UniqueInGrestsListValidator(grestNameTextEdit));
		check();
	}

	protected void grestNameTextEdit_keyEvent(KeyEvent e) {
		check();
	}

	protected void cancelButton_actionPerformed(ActionEvent e) {
		setVisible(false);
	}

	protected void okButton_actionPerformed(ActionEvent e) {
		setVisible(false);
		try {
			ApplicationSystem.getInstance().execute(CreateGrestCommand.createCommand(this, getGrestName(), getTeams()));
		} catch (CommandExecutionException e1) {
			ApplicationSystem.getInstance().showWarning("Some problems occured during grest creation.", this);
		}
	}

	protected void removeTeamButton_actionPerformed(ActionEvent e) {
		if(ApplicationSystem.getInstance().showQuestion("Do you really want to remove \"" + teamsList.getSelectedTeam() + "\"", this) == DialogResults.YES){
			((TeamsListModel) teamsList.getModel()).removeTeam(teamsList.getSelectedTeam());
		}
		check();
	}

	protected void addTeamButton_actionPerformed(ActionEvent e) {
		String teamName = "New Team";
		int i=2;

		while(((TeamsListModel) teamsList.getModel()).nameAlreadyUsed(teamName)){
			teamName = "New Team " + i;
			i++;
		}

		try {
			((TeamsListModel) teamsList.getModel()).addTeam(Team.create(teamName));
			check();
		} catch (InconsistencyException e1) {
			ApplicationSystem.getInstance().logError(e1.getMessage());
			ApplicationSystem.getInstance().showError("Cannot create new team.\n" + e1.getMessage(), this);
		}
	}

	protected void logoBrowseButton_actionPerformed(ActionEvent e) {
		if(!logoTextEdit.getText().equals("")){
			int result = ApplicationSystem.getInstance().showOptionDialog("What do you want to do?", new String[]{"Set NO logo", "Choose another..", "Cancel"}, this);
			switch(result){
			case 0:
				logoTextEdit.setText("");
				grestImageLabel.setIcon(new ImageIcon(EditTeamPanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
				break;
			case 1:
				File logo = ImagesFileChooser.showDialog(this);
				if(logo != null){
					logoTextEdit.setText(logo.getPath());
					grestImageLabel.setIcon(obtainImage(logo));
				}
				break;
			default:
				return;
			}
		} else {
			File logo = ImagesFileChooser.showDialog(this);
			if(logo != null){
				logoTextEdit.setText(logo.getPath());
				grestImageLabel.setIcon(obtainImage(logo));
			}
		}
	}

	protected void grestNameText(ActionEvent e) {
		check();
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {
		okButton.setEnabled(isOk);

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
		removeTeamButton.setEnabled(teamsList.getModel().getSize() > 2 && teamsList.getSelectedIndex() != -1);
	}

	private ImageIcon obtainImage(File source){
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

	public String getGrestName(){
		return grestNameTextEdit.getText();
	}

	public ITeam[] getTeams(){
		ITeam[] teams = new ITeam[teamsList.getModel().getSize()];

		for(int i=0; i<teams.length; i++){
			teams[i] = ((TeamsListModel) teamsList.getModel()).getTeam(i);
		}

		return teams;
	}

	public static void showDialog(final Component owner){
		NewGrestDialog dialog = null;
		if(owner instanceof Dialog){
			dialog = new NewGrestDialog((Dialog) owner);
		} else if(owner instanceof Frame){
			dialog = new NewGrestDialog((Frame) owner);
		} else {
			dialog = new NewGrestDialog((Frame) null);			
		}

		dialog.setVisible(true);
	}

	public void valueChanged(ListSelectionEvent e) {
		removeTeamButton.setEnabled(teamsList.getSelectedIndex() != -1 && teamsList.getModel().getSize() > 2);
	}
}
