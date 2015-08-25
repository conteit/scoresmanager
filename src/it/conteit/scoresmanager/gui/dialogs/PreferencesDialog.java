package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.gui.MainFrame;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.DirectoryChooser;
import it.conteit.scoresmanager.gui.models.PresentationModel;
import it.conteit.scoresmanager.gui.renderers.PresentationListCellRenderer;
import it.conteit.scoresmanager.gui.widgets.DialogHeader;
import it.conteit.scoresmanager.gui.widgets.TitledSeparator;
import it.conteit.scoresmanager.presentation.AbstractPresentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class PreferencesDialog extends JDialog {
	private static final long serialVersionUID = 6709111916331665094L;
	
	private final JPanel m_contentPanel = new JPanel();
	private JTextField textField;
	private JButton btnBrowse;
	private JCheckBox chckbxSaveInformationsWhen;
	private JCheckBox chckbxAutoExport;
	private JList list;
	private JButton btnSetAsDefault;
	private PresentationModel model;

	private Class<? extends AbstractPresentation> defPres;

	public PreferencesDialog() {
		super((Frame) null, true);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				loadData();
			}
		});
		setTitle("Preferences");
		setBounds(100, 100, 538, 445);
		getContentPane().setLayout(new BorderLayout());
		m_contentPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(m_contentPanel, BorderLayout.CENTER);
		{
			DialogHeader header= new DialogHeader();
			header.setTitle("Application's Preferences");
			header.setDescription("Set your preferences, and press OK to save them");
			header.setImage(new ImageIcon(PreferencesDialog.class.getResource("/it/conteit/scoresmanager/gui/images/preferences_icon.png")));
			m_contentPanel.add(header, BorderLayout.NORTH);
		}
		{
			JPanel panel = new JPanel();
			panel.setLayout(new FormLayout(new ColumnSpec[] {
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
					FormFactory.DEFAULT_ROWSPEC,}));
			m_contentPanel.add(panel, BorderLayout.CENTER);
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,}));
				panel.add(panel_1, "2, 2, fill, fill");
				{
					chckbxAutoExport = new JCheckBox("Automatically export grest to PDF while saving");
					chckbxAutoExport.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeDir(!chckbxAutoExport.isSelected());
						}
					});
					chckbxAutoExport.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					panel_1.add(chckbxAutoExport, "2, 2, 3, 1");
				}
				{
					textField = new JTextField();
					textField.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					textField.setEditable(false);
					panel_1.add(textField, "4, 4, fill, default");
					textField.setColumns(10);
				}
				{
					btnBrowse = new JButton("Browse..");
					btnBrowse.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeDir(false);
						}
					});
					btnBrowse.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					btnBrowse.setEnabled(false);
					panel_1.add(btnBrowse, "6, 4");
				}
				{
					chckbxSaveInformationsWhen = new JCheckBox("Save grest informations when application is closing");
					chckbxSaveInformationsWhen.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					panel_1.add(chckbxSaveInformationsWhen, "2, 6, 3, 1");
				}
			}
			{
				JPanel title2 = new TitledSeparator("Presentation options");
				panel.add(title2, "2, 4, fill, fill");
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
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,}));
				panel.add(panel_1, "2, 6, fill, fill");
				{
					JScrollPane scrollPane = new JScrollPane();
					panel_1.add(scrollPane, "2, 2, fill, fill");
					{
						model = new PresentationModel();
						list = new JList(model);
						ApplicationSystem.getInstance().addListener(model);
						model.defaultPresentationChanged(ApplicationSystem.getInstance().getDefaultPresentationClass());
						list.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								btnSetAsDefault.setEnabled(list.getSelectedIndex() >= 0);
							}
						});
						list.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
						list.setCellRenderer(new PresentationListCellRenderer());
						scrollPane.setViewportView(list);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_2.setLayout(new FormLayout(new ColumnSpec[] {
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
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,}));
					panel_1.add(panel_2, "4, 2, fill, fill");
					{
						btnSetAsDefault = new JButton("Set as default");
						btnSetAsDefault.setEnabled(false);
						btnSetAsDefault.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								ApplicationSystem.getInstance().setDefaultPresIndex(list.getSelectedIndex());
							}
						});
						btnSetAsDefault.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
						panel_2.add(btnSetAsDefault, "2, 2");
						{
							JButton btnEdit = new JButton("Configure");
							btnEdit.setVisible(false);
							btnEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
							panel_2.add(btnEdit, "2, 4");
						}
					}
					
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						savePreferences();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!defPres.equals(ApplicationSystem.getInstance().getDefaultPresentationClass())){
							ApplicationSystem.getInstance().setDefaultPresentation(defPres);
						}
						
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit()
		.getScreenSize();
		int xPos= new Double((screensize.getWidth() - getSize().width) / 2).intValue();
		int yPos = new Double((screensize.getHeight() - getSize().height) / 2).intValue();
		setLocation(xPos, yPos);
		setResizable(false);
	}

	protected void savePreferences() {
		ApplicationSystem.getInstance().setAutoExportDir(textField.getText());
		ApplicationSystem.getInstance().setSaveOnExit(chckbxSaveInformationsWhen.isSelected());
		MainFrame.getInstance().setSaveOnExitCheckbox(chckbxSaveInformationsWhen.isSelected());
	}
	
	protected void loadData(){
		chckbxAutoExport.setSelected(ApplicationSystem.getInstance().autoExportEnabled());
		btnBrowse.setEnabled(ApplicationSystem.getInstance().autoExportEnabled());
		textField.setText(ApplicationSystem.getInstance().getAutoExportDir());
		chckbxSaveInformationsWhen.setSelected(ApplicationSystem.getInstance().saveOnExit());
		
		model.clear();
		for(int i=0; i<ApplicationSystem.getInstance().getPresentationClassesCount(); i++){
			model.addElement(ApplicationSystem.getInstance().getPresentationClass(i));
		}
		list.setSelectedIndex(ApplicationSystem.getInstance().getDefaultPresIndex());
		defPres = ApplicationSystem.getInstance().getDefaultPresentationClass();
	}

	protected void changeDir(boolean disable) {
		if(!disable){
			File dir = DirectoryChooser.showDialog(this);
			
			if(dir != null){
				textField.setText(dir.getPath());
			} else {
				textField.setText("");
				disable = true;
			}
		} else {			
			textField.setText("");
		}
		
		btnBrowse.setEnabled(!disable);
	}

	public static void showDialog(){
		PreferencesDialog dialog = new PreferencesDialog();
		dialog.loadData();
		
		dialog.setVisible(true);
	}
}
