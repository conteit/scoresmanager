package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.ImagesFileChooser;
import it.conteit.scoresmanager.gui.models.TeamsListModel;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.gui.valiators.ChildIsOkValidator;
import it.conteit.scoresmanager.gui.valiators.IApplicationComponent;
import it.conteit.scoresmanager.gui.valiators.TextFieldNotEmptyValidator;
import it.conteit.scoresmanager.gui.valiators.UniqueInTeamsListValidator;
import it.conteit.scoresmanager.gui.widgets.ColorComboBox;
import it.conteit.scoresmanager.gui.widgets.TeamsList;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class EditTeamPanel extends AbstractApplicationPanel implements ListSelectionListener{
	private static final long serialVersionUID = -4655491541033303366L;

	private TeamsList list;
	private JLabel teamImageLabel;
	private JTextField teamNameTextEdit;
	private JTextField avatarTextEdit;
	private JLabel warningsLabel;

	private JButton avatarBrowseButton;
	private boolean validationOK = true;

	private ITeam team = null;

	private ColorComboBox colorComboBox;

	private boolean fixMode;

	/**
	 * Create the panel.
	 */
	public EditTeamPanel(TeamsList list, IApplicationComponent parent) {
		super();
		setBorder(UIManager.getBorder("InsetBorder.aquaVariant"));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("bottom:20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:17dlu"),}));
		setBorder(new CompoundBorder(new EmptyBorder(-7, 0, -10, 0), UIManager.getBorder("InsetBorder.aquaVariant")));

		setParent(parent);
		
		JLabel lblTeamsName = new JLabel("Team name:");
		lblTeamsName.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(lblTeamsName, "4, 4, 3, 1");

		teamImageLabel = new JLabel("");
		teamImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		teamImageLabel.setIcon(new ImageIcon(EditTeamPanel.class.getResource("/it/conteit/scoresmanager/gui/images/team_icon.png")));
		teamImageLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(teamImageLabel, "2, 4, 1, 3, fill, default");

		teamNameTextEdit = new JTextField();
		teamNameTextEdit.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if(validationOK){
					saveData();
				}
			}
		});
		teamNameTextEdit.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				teamNameTextEdit_keyEvent(e);
			}
			public void keyPressed(KeyEvent e) {
				teamNameTextEdit_keyEvent(e);
			}
		});
		teamNameTextEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(teamNameTextEdit, "4, 6, 3, 1, fill, default");
		teamNameTextEdit.setColumns(10);

		JSeparator separator_1 = new JSeparator();
		add(separator_1, "2, 8, 5, 1");

		JLabel lblColor = new JLabel("Color:");
		lblColor.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(lblColor, "2, 10, right, default");

		colorComboBox = new ColorComboBox();
		colorComboBox.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if(validationOK){
					saveData();
				}
			}
		});
		colorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorComboBox_actionPerformed(e);
			}
		});
		colorComboBox.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(colorComboBox, "4, 10, 3, 1, fill, default");

		JLabel lblAvatar = new JLabel("Avatar:");
		lblAvatar.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(lblAvatar, "2, 12, right, default");

		avatarTextEdit = new JTextField();
		avatarTextEdit.setEditable(false);
		avatarTextEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(avatarTextEdit, "4, 12, fill, default");
		avatarTextEdit.setColumns(10);

		avatarBrowseButton = new JButton("Browse..");
		avatarBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				avatarBrowseButton_actionPerformed(e);
			}
		});
		avatarBrowseButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		add(avatarBrowseButton, "6, 12");

		warningsLabel = new JLabel("Invalid Team name: already used");
		warningsLabel.setForeground(Color.RED);
		warningsLabel.setVisible(false);
		warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		warningsLabel.setIcon(new ImageIcon(EditTeamPanel.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
		add(warningsLabel, "2, 16, 5, 1");

		this.list = list;

		addValidator(new TextFieldNotEmptyValidator("Team Name", teamNameTextEdit));
		addValidator(new UniqueInTeamsListValidator(list, teamNameTextEdit));

		parent.addValidator(new ChildIsOkValidator("Team panel", this));
		setComponentsEnabled(false);
	}

	protected void teamNameTextEdit_keyEvent(KeyEvent e) {
		check();
	}

	protected void avatarBrowseButton_actionPerformed(ActionEvent e) {
		if(!avatarTextEdit.getText().equals("")){
			int result = ApplicationSystem.getInstance().showOptionDialog("What do you want to do?", new String[]{"Set NO avatar", "Choose another..", "Cancel"}, this);
			switch(result){
			case 0:
				avatarTextEdit.setText("");
				teamImageLabel.setIcon(new ImageIcon(EditTeamPanel.class.getResource("/it/conteit/scoresmanager/gui/images/team_icon.png")));
				break;
			case 1:
				File avatar = ImagesFileChooser.showDialog(this);
				if(avatar != null){
					avatarTextEdit.setText(avatar.getPath());
					teamImageLabel.setIcon(obtainImage(avatar));
				}
				break;
			default:
				return;
			}
		} else {
			File avatar = ImagesFileChooser.showDialog(this);
			if(avatar != null){
				avatarTextEdit.setText(avatar.getPath());
				teamImageLabel.setIcon(obtainImage(avatar));
			}
		}
		
		if(validationOK){
			saveData();
		}
	}

	protected void colorComboBox_actionPerformed(ActionEvent e) {
		if(colorComboBox.getSelectedItem() instanceof Color){
			Color c = (Color) colorComboBox.getSelectedItem();
			colorComboBox.setToolTipText(String.format("(R=%d, G=%d, B=%d, A=%d)", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
		} else {
			Color c = JColorChooser.showDialog(this, "Choose color for Team", (Color) team.getColor().getValue());
			if(c != null){
				colorComboBox.chooseColor(c);
				colorComboBox.setToolTipText(String.format("(R=%d, G=%d, B=%d, A=%d)", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
			}
		}
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {
		if(!isOk){
			String res = new String(validationResult[0]);
			for(int i=1; i<validationResult.length; i++){
				res += "; ";
				res += validationResult[i];
			}
			warningsLabel.setText(res);
			warningsLabel.setToolTipText(res);
		}
		warningsLabel.setVisible(!isOk);
	}

	public void valueChanged(ListSelectionEvent e) {
		if(fixMode){
			fixMode = false;
			return;
		}
		
		setComponentsEnabled(false);
		if(team != null){
			if(validationOK){
				saveData();
			} else {
				if(ApplicationSystem.getInstance().showConfirmation("Some informations are invalid:\n\n" + warningsLabel.getText().replace("; ", ".\n") + "\n\nPress OK to discard changes and go on.", this) == DialogResults.CANCEL){
					for(int i=0; i<list.getModel().getSize(); i++){
						if(list.getModel().getElementAt(i).equals(team)){
							fixMode = true;
							list.setSelectedIndex(i);
							setComponentsEnabled(true);
							return;
						}
					}
				}
			}
		}
		
		if(list.getSelectedIndex() != -1){
			loadData();
			setComponentsEnabled(true);		
		}
	}
	
	private void loadData() {
		team = ((TeamsListModel) list.getModel()).getTeam(list.getSelectedIndex());
		teamNameTextEdit.setText(team.getName());

		colorComboBox.chooseColor((Color) team.getColor().getValue());

		if(team.getAvatar() != null){
			avatarTextEdit.setText(team.getAvatar().getPath());
			teamImageLabel.setIcon(obtainImage(team.getAvatar()));
		} else {
			avatarTextEdit.setText("");
			teamImageLabel.setIcon(new ImageIcon(EditTeamPanel.class.getResource("/it/conteit/scoresmanager/gui/images/team_icon.png")));
		}
		
		check();
	}

	private void saveData() {		
		try {
			if(!teamNameTextEdit.getText().equals("")){
				team.setName(teamNameTextEdit.getText());
			}
			if(colorComboBox.getSelectedItem() instanceof Color){
				team.setColor(new it.conteit.scoresmanager.data.Color((Color) colorComboBox.getSelectedItem()));
			}
			if(!avatarTextEdit.getText().equals("")){
				team.setAvatar(new File(avatarTextEdit.getText()));
			} else {
				team.setAvatar(null);
			}
			
			((TeamsListModel) list.getModel()).updateTeam(team);
		} catch (InconsistencyException e) {
			ApplicationSystem.getInstance().logError(e.getMessage());
			ApplicationSystem.getInstance().showWarning("Cannot save team informations.\n" + e.getMessage(), this);
		}
	}

	private void setComponentsEnabled(boolean isEnabled){
		teamImageLabel.setEnabled(isEnabled);
		teamNameTextEdit.setEnabled(isEnabled);
		colorComboBox.setEnabled(isEnabled);
		avatarBrowseButton.setEnabled(isEnabled);
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
