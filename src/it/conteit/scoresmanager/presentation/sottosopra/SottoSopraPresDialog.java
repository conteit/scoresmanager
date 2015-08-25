package it.conteit.scoresmanager.presentation.sottosopra;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.gui.dialogs.NewGrestDialog;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationDialog;
import it.conteit.scoresmanager.gui.widgets.DialogHeader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SottoSopraPresDialog extends AbstractApplicationDialog {
	private static final long serialVersionUID = 8225052826387300038L;
	
	private static final String FIRST = "First";
	private static final String SECOND = "Second";
	
	private Entry[] result = null;
	
	private JLabel warningsLabel;

	private JButton btnBack;

	private JButton btnNext;

	private JPanel contentPane;

	private SSScoresPane spane;

	private SSTeamsPane tpane;

	private IGrest grest;

	public SottoSopraPresDialog(){
		this(null, null);
	}
	
	public SottoSopraPresDialog(Frame owner, IGrest g){
		super(owner);
		grest = g;
		setSize(575, 450);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			DialogHeader headerPane = new DialogHeader();
			headerPane.setTitle("SottoSopra Presentation Wizard");
			headerPane.setDescription("Configure settings to customize scores presentation");
			headerPane.setImage(new ImageIcon(SottoSopraPresDialog.class.getResource("/it/conteit/scoresmanager/presentation/sottosopra/Logo_Sottosopra_cregrest2010_2x2.jpg")));
			getContentPane().add(headerPane, BorderLayout.NORTH);
		}
		{
			JPanel footerPane = new JPanel(new BorderLayout());
			
			JPanel warningsPane = new JPanel(new BorderLayout());
			warningsPane.setBorder(new EmptyBorder(0,12,0,0));
			footerPane.add(warningsPane, BorderLayout.CENTER);

			warningsLabel = new JLabel("Invalid presentation settings");
			warningsLabel.setForeground(Color.RED);
			warningsLabel.setVisible(false);
			warningsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			warningsLabel.setIcon(new ImageIcon(NewGrestDialog.class.getResource("/it/conteit/scoresmanager/gui/images/warn_ic.png")));
			warningsPane.add(warningsLabel, BorderLayout.CENTER);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			footerPane.add(buttonPane, BorderLayout.EAST);
			{
				btnBack = new JButton("< Back");
				btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				btnBack.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						((CardLayout) contentPane.getLayout()).show(contentPane, FIRST);
						btnBack.setEnabled(false);
						btnNext.setText("Next >");
					}
				});
				btnBack.setEnabled(false);
				buttonPane.add(btnBack);
			}
			{
				btnNext = new JButton("Next >");
				btnNext.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (btnBack.isEnabled()){
							try {
								result = computeEntries();
							} catch (InconsistencyException e1) {
								ApplicationSystem.getInstance().showError("Error during presentation values computation", null);
							}
							setVisible(false);
						} else {
							// Go to second
							((CardLayout) contentPane.getLayout()).show(contentPane, SECOND);
							btnBack.setEnabled(true);
							btnNext.setText("Finish");
						}
					}
				});
				getRootPane().setDefaultButton(btnNext);
				btnNext.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				buttonPane.add(btnNext);
			}
			{
				JPanel sep = new JPanel();
				buttonPane.add(sep);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				buttonPane.add(btnCancel);
			}
			getContentPane().add(footerPane, BorderLayout.SOUTH);
		}
		
		contentPane = new JPanel();
		contentPane.setLayout(new CardLayout(0, 0));
		spane = new SSScoresPane(g);
		contentPane.add(spane, FIRST);
		tpane = new SSTeamsPane(g);
		contentPane.add(tpane, SECOND);
		getContentPane().add(contentPane, BorderLayout.CENTER);
	}
	
	private Entry[] computeEntries() throws InconsistencyException {
		Association[] assoc = tpane.getAssociations();
		Score[] parts = spane.getPartToBeShown();
		Entry[] res = new Entry[parts.length + 1]; 
			
		for (int i = 0; i < parts.length; i++){
			int[] ss = aggregate(assoc, parts[i]);
			res[i] = new Entry(parts[i].getDescription(), ss[0], ss[1], ss[2], ss[3]);
		}
		
		int[] ss = aggregate(assoc, (Score) grest.getTotalScore());
		res[parts.length] = new Entry("Total", ss[0], ss[1], ss[2], ss[3]);
		
		return res;
	}
	
	
	private int[] aggregate(Association[] assoc, Score s){
		int[] res = new int[]{0,0,0,0};
		
		for(int i = 0; i < res.length; i++){
			for (int j = 0; j < assoc.length; j++){
				if(assoc[j].pteam == i){
					res[i] += getScoreValue(assoc[j].team, s);
				}
			}
		}
		
		return res;
	}
	
	private Integer getScoreValue(ITeam team, Score s) {
		ITeam[] t = grest.teams();
		for (int i = 0; i < t.length; i++){
			if(t[i].equals(team)){
				return s.getValue(i);
			}
		}
		
		return 0;
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {}
	
	public Entry[] getResult(){
		return result;
	}

	public IDay getChoosedDay() {
		return spane.getChoosedDay();
	}

}
