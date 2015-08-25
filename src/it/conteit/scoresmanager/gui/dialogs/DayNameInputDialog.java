package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationDialog;
import it.conteit.scoresmanager.gui.valiators.TextFieldNotEmptyValidator;
import it.conteit.scoresmanager.gui.valiators.UniqueInDaysListValidator;

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

public class DayNameInputDialog extends AbstractApplicationDialog {
	private static final long serialVersionUID = 5889667515995251164L;

	private final JPanel m_contentPanel = new JPanel();
	private JTextField inputField;
	private JLabel warningsLabel;
	private JButton okButton;

	private IGrest grest;
	private IDay result = null;

	private UniqueInDaysListValidator val;

	public DayNameInputDialog(){
		this((Frame) null, null);
	}
	
	public DayNameInputDialog(Dialog owner, IGrest grest) {
		super(owner);
		createImportGrestInputDialog(grest);
	}

	public DayNameInputDialog(Frame owner, IGrest grest) {
		super(owner);
		createImportGrestInputDialog(grest);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param serializer
	 */
	public void createImportGrestInputDialog(IGrest grest) {
		setTitle("Add new Day");
		this.grest = grest;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				result = null;
			}
		});
		setBounds(100, 100, 401, 160);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		m_contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("6dlu"),
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("8dlu"),
						RowSpec.decode("bottom:12dlu"), RowSpec.decode("5dlu"),
						RowSpec.decode("12dlu"), }));
		m_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(m_contentPanel, BorderLayout.CENTER);
		{
			JLabel lblTheImportedGrest = new JLabel("Insert new day name:");
			lblTheImportedGrest.setFont(new Font("Lucida Grande", Font.PLAIN,
					11));
			m_contentPanel.add(lblTheImportedGrest, "2, 2, 3, 1");
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
			warningsLabel
					.setIcon(new ImageIcon(
							DayNameInputDialog.class
									.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
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
				// okButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
				buttonPane.add(okButton);

				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel_action(e);
					}
				});
				// cancelButton.setFont(new Font("Lucida Grande", Font.PLAIN,
				// 11));
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
		int xPos = new Double((screensize.getWidth() - 286) / 2).intValue();
		int yPos = new Double((screensize.getHeight() / 2) - 200).intValue();
		setLocation(xPos, yPos);
		setResizable(false);

		addValidator(new TextFieldNotEmptyValidator("Day Name", inputField));
		val = new UniqueInDaysListValidator(grest, inputField);
		addValidator(val);
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
			if (result == null) {
				result = Day.create(inputField.getText(), grest.teamCount());
			} else {
				result.setDescription(inputField.getText());
			}
		} catch (InconsistencyException e1) {
			ApplicationSystem.getInstance().logError("Cannot rename grest");
		}
		setVisible(false);
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {
		okButton.setEnabled(isOk);
		if (!isOk) {
			String res = new String(validationResult[0]);
			for (int i = 1; i < validationResult.length; i++) {
				res += ";  ";
				res += validationResult[i];
			}
			warningsLabel.setText(res);
			warningsLabel.setToolTipText(res);
		}
		warningsLabel.setVisible(!isOk);
	}

	public IDay getResult() {
		return result;
	}

	public void setDay(IDay day) {
		result = day;
		inputField.setText(day.getDescription());
		val.setOldValue(day.getDescription());

		if (day != null) {
			setTitle("Rename day");
		} else {
			setTitle("Add new day");
		}
		check();
	}

	public static IDay showDialog(IGrest grest) {
		DayNameInputDialog dialog = new DayNameInputDialog((Frame) null, grest);
		dialog.setVisible(true);

		return dialog.getResult();
	}

	public static IDay showDialog(IGrest grest, IDay day) {
		DayNameInputDialog dialog = new DayNameInputDialog((Frame) null, grest);
		dialog.setDay(day);
		dialog.setVisible(true);

		return dialog.getResult();
	}
}
