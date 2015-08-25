package it.conteit.scoresmanager.presentation.passpartou;

import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.ITeam;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;

public class SSTeamsPane extends JPanel {
	private static final long serialVersionUID = 5065766410518583498L;
	
	private JPanel contentPane;

	private TeamEntry[] tpanes;

	public SSTeamsPane(){
		this(null);
	}
	
	public SSTeamsPane(IGrest g){
		setLayout(new BorderLayout(0, 0));
		{
			JLabel lblAssociateGrestsTeams = new JLabel("Associate grest's teams, on the left, to presentation's teams, on the right:");
			lblAssociateGrestsTeams.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			lblAssociateGrestsTeams.setBorder(new EmptyBorder(5,5,5,5));
			add(lblAssociateGrestsTeams, BorderLayout.NORTH);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBorder(new EmptyBorder(0,0,0,0));
			add(scrollPane, BorderLayout.CENTER);
			{
				contentPane = new JPanel();
				contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
				scrollPane.setViewportView(contentPane);
				
				ITeam[] ts = g.teams();
				tpanes = new TeamEntry[ts.length]; 
				for(int i = 0; i < ts.length; i++){
					tpanes[i] = new TeamEntry(ts[i], (i % 4));
					contentPane.add(tpanes[i]);
				}
			}
		}
	}
	
	public Association[] getAssociations(){
		Association[] res = new Association[tpanes.length];
		
		for (int i = 0; i < res.length; i++){
			res[i] = tpanes[i].getAssociation();
		}
		
		return res;
	}
}
