package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.workers.IProgressListener;
import it.conteit.scoresmanager.control.workers.SwingWorker;
import it.conteit.scoresmanager.tools.GuiUtils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ProgressDialog extends JDialog implements IProgressListener{
	private static final long serialVersionUID = 5889667515995251164L;
	
	private final JPanel m_contentPanel = new JPanel();
	private JLabel lblWorking;
	private JProgressBar progressBar;
	private JLabel lblCurrentStatus;
	private JButton cancelButton;
	
	private SwingWorker<?> worker;
	
	public ProgressDialog() {
		this(true);
		setModal(true);
	}
	
	public ProgressDialog(boolean canBeCancelled) {
		this(canBeCancelled, null);
	}

	/**
	 * Create the dialog.
	 * @param serializer 
	 */
	public ProgressDialog(boolean canBeCancelled, SwingWorker<?> worker) {
		setTitle("Progress");
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
			label.setIcon(new ImageIcon(ProgressDialog.class.getResource("/it/conteit/scoresmanager/gui/images/progress_icon.png")));
			m_contentPanel.add(label, "2, 2");
		}
		{
			lblWorking = new JLabel("Working..");
			m_contentPanel.add(lblWorking, "4, 2");
		}
		{
			progressBar = new JProgressBar();
			progressBar.setValue(50);
			m_contentPanel.add(progressBar, "2, 6, 3, 1");
		}
		{
			lblCurrentStatus = new JLabel("Current status..");
			lblCurrentStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			m_contentPanel.add(lblCurrentStatus, "2, 4, 3, 1");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelOp();
					}
				});
				cancelButton.setEnabled(canBeCancelled);
				getRootPane().setDefaultButton(cancelButton);
				cancelButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
			}
		}
		
		setResizable(false);
		setLocation(GuiUtils.centerScreenFrame(getSize()));
		
		if(worker != null){
			this.worker = worker;
			worker.addProgressListener(this);
		}
	}

	protected void cancelOp() {
		if(worker != null){
			worker.interrupt();
			cancelButton.setEnabled(false);
		}
	}

	public void progressUpdate(String operation, String currStatus, int progress, boolean done) {
		progressBar.setIndeterminate(progress < 0);
		if(progress <= 100 && progress >= 0){
			progressBar.setValue(progress);
		}
		
		lblWorking.setText(operation);
		lblCurrentStatus.setText(currStatus);

		if(!isVisible() && progress < 100){
			setVisible(true);
		} else if(done){
			setVisible(false);
		}
	}

}
