package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationDialog;
import it.conteit.scoresmanager.gui.valiators.TextFieldNotEmptyValidator;
import it.conteit.scoresmanager.gui.valiators.UniqueInGrestsListValidator;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RenameImportGrestInputDialog extends AbstractApplicationDialog {
	private static final long serialVersionUID = 5889667515995251164L;
	
	private final JPanel m_contentPanel = new JPanel();
	private JTextField inputField;
	private JLabel warningsLabel;
	private JButton okButton;
	
	private IGrest result = null;
	
	public RenameImportGrestInputDialog() {
		this((Frame) null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				result = null;
			}
		});
	}
	
	public RenameImportGrestInputDialog(Dialog owner) {
		super(owner);
		createImportGrestInputDialog();
	}
	
	public RenameImportGrestInputDialog(Frame owner) {
		super(owner);
		createImportGrestInputDialog();
	}

	/**
	 * Create the dialog.
	 * @param serializer 
	 */
	public void createImportGrestInputDialog() {
		setTitle("Rename imported grest");
		setBounds(100, 100, 499, 193);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		m_contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6dlu"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("8dlu"),
				RowSpec.decode("bottom:12dlu"),
				RowSpec.decode("5dlu"),
				RowSpec.decode("12dlu"),}));
		m_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(m_contentPanel, BorderLayout.CENTER);
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(RenameImportGrestInputDialog.class.getResource("/it/conteit/scoresmanager/gui/images/progress_icon.png")));
			m_contentPanel.add(label, "2, 2");
		}
		{
			JLabel lblTheImportedGrest = new JLabel("<html>\r\t<div>The imported Grest has an already used name.</div>\n\t<div>Resolve the conflict specifing another one:</div>\r</html>");
			lblTheImportedGrest.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			m_contentPanel.add(lblTheImportedGrest, "4, 2");
		}
		{
			inputField = new JTextField();
			inputField.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					key_ev(e);
				}
				
				public void keyReleased(KeyEvent e) {
					key_ev(e);
				}
			});
			inputField.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			m_contentPanel.add(inputField, "2, 4, 3, 1");
		}
		{
			warningsLabel = new JLabel("Invalid name");
			warningsLabel.setIcon(new ImageIcon(RenameImportGrestInputDialog.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
			warningsLabel.setForeground(Color.RED);
			warningsLabel.setVisible(false);
			warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			m_contentPanel.add(warningsLabel, "2, 6, 3, 1");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ok_action(e);
					}
				});
				//okButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
				buttonPane.add(okButton);
				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel_action(e);
					}
				});
				//cancelButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
			}
		}
		
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit()
		.getScreenSize();
		int xPos= new Double((screensize.getWidth() - 286) / 2).intValue();
		int yPos = new Double((screensize.getHeight() / 2) - 200).intValue();
		setLocation(xPos, yPos);
		setResizable(false);
		
		addValidator(new TextFieldNotEmptyValidator("Grest Name", inputField));
		addValidator(new UniqueInGrestsListValidator(inputField));
		check();
	}

	protected void key_ev(KeyEvent e) {
		check();
	}

	protected void cancel_action(ActionEvent e) {
		result = null;
		setVisible(false);
	}

	protected void ok_action(ActionEvent e) {
		try {
			result.setName(inputField.getText());
		} catch (InconsistencyException e1) {
			ApplicationSystem.getInstance().logError("Cannot rename grest");
		}
		setVisible(false);
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
	}
	
	public IGrest getResult(){
		return result;
	}
	
	public void setGrest(IGrest grest){
		result = grest;
		inputField.setText(grest.getName());
		check();
	}
	
	public static IGrest showDialog(IGrest grest){
		RenameImportGrestInputDialog dialog = new RenameImportGrestInputDialog();
		dialog.setGrest(grest);
		dialog.setVisible(true);
		
		return dialog.getResult();
	}
}
