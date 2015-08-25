package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.visitors.DataEvent;
import it.conteit.scoresmanager.control.management.visitors.IDataListener;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.ImagesFileChooser;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.gui.valiators.ChildIsOkValidator;
import it.conteit.scoresmanager.gui.valiators.IApplicationComponent;
import it.conteit.scoresmanager.gui.valiators.NotUsedNewGrestNameValidator;
import it.conteit.scoresmanager.gui.valiators.TextFieldNotEmptyValidator;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EditGrestPanel extends AbstractApplicationPanel implements IDataListener {
	private static final long serialVersionUID = 937167328826709373L;

	private IGrest grest = null;

	private JTextField txtGrestname;
	private JPanel statsPane;
	private JPanel teamsPane;
	private JPanel daysPane;
	private JPanel southPanel;
	private JToggleButton editGrestNameButton;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private static final String TEAMS_PANE = "Teams options";
	private static final String DAYS_PANE = "Grest's Registry";
	private static final String STATS_PANE = "Statistics";

	private JLabel warningsLabel;
	private JLabel logoLabel;
	private JLabel datesLabel;
	
	public EditGrestPanel(){
		createEditGrestPanel();
	}

	public EditGrestPanel(IGrest grest) {
		this.grest = grest;
		createEditGrestPanel();
	}

	/**
	 * Create the panel.
	 */
	public void createEditGrestPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:15dlu"),}));
		{
			JPanel panel = new JPanel();
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("default:grow"),}));
			add(panel, "2, 2, fill, fill");
			{
				logoLabel = new JLabel("");
				logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				logoLabel.setToolTipText("Double-click to change logo");
				logoLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2 && !editGrestNameButton.isSelected()){
							changeLogo();
						}
					}
				});
				logoLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
				logoLabel.setIcon(new ImageIcon(EditGrestPanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
				panel.add(logoLabel, "2, 2");
			}
			{
				warningsLabel = new JLabel("Invalid");
				warningsLabel.setForeground(Color.RED);
				warningsLabel.setVisible(false);
				warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				warningsLabel.setIcon(new ImageIcon(EditGrestPanel.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
				add(warningsLabel, "2, 10");
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,},
					new RowSpec[] {
						FormFactory.NARROW_LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,}));
				panel.add(panel_1, "4, 2, fill, fill");
				{
					txtGrestname = new JTextField();
					txtGrestname.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							check();
						}
						
						public void keyReleased(KeyEvent e) {
							check();
						}
					});
					txtGrestname.setEditable(false);
					txtGrestname.setFont(new Font("Lucida Grande", Font.BOLD, 11));
					panel_1.add(txtGrestname, "2, 2, fill, default");
					txtGrestname.setColumns(10);
				}
				{
					JToolBar tb = new JToolBar();
					tb.setFloatable(false);
					editGrestNameButton = new JToggleButton("");
					editGrestNameButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							editGrestName();
						}
					});
					editGrestNameButton.setIcon(new ImageIcon(EditGrestPanel.class.getResource("/it/conteit/scoresmanager/gui/images/edit.png")));
					editGrestNameButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					editGrestNameButton.setToolTipText("Edit grest name");
					tb.add(editGrestNameButton);
					panel_1.add(tb, "4, 2");
				}
				{
					datesLabel = new JLabel("<html>\r\t<div style=\"margin-left: 4px;\">\r\t\t<div>\r\t\t\tCreated on: <b>" + (grest != null ? dateFormat.format(grest.getCreationDate()) : "") + "</b>\r\t\t</div>\r\t\t<div>\r\t\t\tModified on: <b>" + (grest != null ? dateFormat.format(grest.getLastModDate()) : "") + "</b>\r\t\t</div>\r\t</div>\r</html>");
					datesLabel.setForeground(UIManager.getColor("Button.disabledText"));
					datesLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					panel_1.add(datesLabel, "2, 4");
				}
			}
		}
		{
			JSeparator separator = new JSeparator();
			add(separator, "2, 4");
		}
		{
			createSouthPanel();
		}
		{
			JComboBox comboBox = new JComboBox(new String[] {TEAMS_PANE, DAYS_PANE, STATS_PANE});
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					CardLayout cl = (CardLayout)(getSouthPanel().getLayout());
					if(((String)e.getItem()).equals(STATS_PANE)){
						
					}
					cl.show(getSouthPanel(), (String)e.getItem());
				}
			});

			// --------------------------------------------------------------------------------
			// Must be changed according to grest status: Empty Grest = 0; Not Empty Grest = 1;
			// Check if valid solution or a method which changes panel is needed
			// --------------------------------------------------------------------------------

			if(grest != null){
				loadData();
				if(grest.days().length > 0){
					showPanel(comboBox, 1);
				} else {
					showPanel(comboBox, 0);
				}
			}

			// --------------------------------------------------------------------------------

			comboBox.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			add(comboBox, "2, 6, fill, default");
		}

		addValidator(new TextFieldNotEmptyValidator("Grest Name", txtGrestname));	
		addValidator(new ChildIsOkValidator(TEAMS_PANE, (IApplicationComponent) getTeamsPanel()));
		addValidator(new ChildIsOkValidator(DAYS_PANE, (IApplicationComponent) getDaysPanel()));
		
		if(grest != null){
			grest.addDataListener(this);
			addValidator(new NotUsedNewGrestNameValidator(grest, txtGrestname));
		}
		
		check();
	}

	private void loadData() {
		txtGrestname.setText(grest.getName());
		if(grest.getLogo() != null){
			logoLabel.setIcon(obtainImage(grest.getLogo()));
		} else{
			logoLabel.setIcon(new ImageIcon(EditGrestPanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest.png")));
		}
		updateGrestDatesLabel();
	}

	protected void editGrestName() {
		txtGrestname.setEditable(editGrestNameButton.isSelected());
		if(!editGrestNameButton.isSelected()){
			changeGrestName();
		}
	}

	private void changeGrestName() {
		if(grest != null){
			try {
				if(!txtGrestname.getText().equals("") && !txtGrestname.getText().equals(grest.getName())){
					grest.setName(txtGrestname.getText());
				}
			} catch (InconsistencyException e) {
				ApplicationSystem.getInstance().showWarning("Cannot change grest name: " + e.getMessage(), this);
			}
		}
	}

	private void showPanel(JComboBox comboBox, int index){
		comboBox.setSelectedIndex(index);
	}

	private JPanel getDaysPanel(){
		if(daysPane == null){
			daysPane = new DaysPanel(grest);
			daysPane.setBorder(UIManager.getBorder("InsetBorder.aquaVariant"));
		}

		return daysPane;
	}

	private JPanel getTeamsPanel(){
		if(teamsPane == null){
			teamsPane = new TeamsPanel(grest, this);
			teamsPane.setBorder(UIManager.getBorder("InsetBorder.aquaVariant"));
		}

		return teamsPane;
	}

	private JPanel getStatsPanel(){
		if(statsPane == null){
			statsPane = new StatisticsPanel(grest);
			statsPane.setBorder(UIManager.getBorder("InsetBorder.aquaVariant"));
		}

		return statsPane;
	}

	private void createSouthPanel(){
		southPanel = new JPanel();
		southPanel.setBorder(new EmptyBorder(-7, 0, -9, 0));
		southPanel.setLayout(new CardLayout(0, 0));

		southPanel.add(TEAMS_PANE, getTeamsPanel());
		southPanel.add(DAYS_PANE, getDaysPanel());
		southPanel.add(STATS_PANE, getStatsPanel());

		add(southPanel, "2, 8, fill, fill");
	}

	private JPanel getSouthPanel(){
		if(southPanel == null){
			createSouthPanel();
		}

		return southPanel;
	}
	
	private void updateGrestDatesLabel(){
		datesLabel.setText("<html>\r\t<div style=\"margin-left: 4px;\">\r\t\t<div>\r\t\t\tCreated on: <b>" + (grest != null ? dateFormat.format(grest.getCreationDate()) : "") + "</b>\r\t\t</div>\r\t\t<div>\r\t\t\tModified on: <b>" + (grest != null ? dateFormat.format(grest.getLastModDate()) : "") + "</b>\r\t\t</div>\r\t</div>\r</html>");
	}

	protected void changeLogo() {
		if(grest == null){
			ApplicationSystem.getInstance().showInformation("Demo mode: no grest specified", this);
			return;
		}
		
		if(grest.getLogo() != null){
			int result = ApplicationSystem.getInstance().showOptionDialog("What do you want to do?", new String[]{"Set NO logo", "Choose another..", "Cancel"}, this);
			switch(result){
			case 0:
				try {
						grest.setLogo(null);
					} catch (InconsistencyException e1) {
						ApplicationSystem.getInstance().logError("Cannot change grest's logo: " + e1.getMessage());
						ApplicationSystem.getInstance().showError("Cannot change grest's logo", this);
					}
				break;
			case 1:
				File logo = ImagesFileChooser.showDialog(this);
				if(logo != null){
					try {
						grest.setLogo(logo);
					} catch (InconsistencyException e) {
						ApplicationSystem.getInstance().logError("Cannot change grest's logo: " + e.getMessage());
						ApplicationSystem.getInstance().showError("Cannot change grest's logo", this);
					}
				}
				break;
			default:
				return;
			}
		} else {
			File logo = ImagesFileChooser.showDialog(this);
			if(logo != null){
				try {
					grest.setLogo(logo);
				} catch (InconsistencyException e) {
					ApplicationSystem.getInstance().logError("Cannot change grest's logo: " + e.getMessage());
					ApplicationSystem.getInstance().showError("Cannot change grest's logo", this);
				}
			}
		}
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {
		editGrestNameButton.setEnabled(isOk);

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
	}

	public void dataChanged(DataEvent ev) {
		if(ev.getSource() instanceof IGrest){
			loadData();
		}
	}
	
	public IGrest getRelatedGrest(){
		return grest;
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
