package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.ImportGrestCommand;
import it.conteit.scoresmanager.control.commands.NewGrestCommand;
import it.conteit.scoresmanager.control.commands.OpenGrestCommand;
import it.conteit.scoresmanager.control.commands.RemoveGrestCommand;
import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.widgets.GrestsList;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GrestsListPanel extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 7874231441588437737L;

	private GrestsList grestsList;

	private JMenuItem removeMenuItem;

	private JMenuItem openMenuItem;

	private JButton openButton;

	private ArrayList<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();

	/**
	 * Create the panel.
	 */
	public GrestsListPanel() {
		setLayout(new BorderLayout(0, 0));
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			add(toolBar, BorderLayout.NORTH);
			{
				JButton newButton = new JButton("New");
				newButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						newButton_actionPerformed(e);
					}
				});
				newButton.setToolTipText("Create a new Grest");
				newButton.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest_new.png")));
				toolBar.add(newButton);
			}
			toolBar.addSeparator(new Dimension(2,2));
			{
				JButton importButton = new JButton(/*"Import.."*/);
				importButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						importButton_actionPerformed(e);
					}
				});
				importButton.setToolTipText("Import an existing Grest from an external file");
				importButton.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/import_grest.png")));
				toolBar.add(importButton);
			}
			toolBar.addSeparator();
			{
				openButton = new JButton("");
				openButton.setEnabled(false);
				openButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openButton_actionPerformed(e);
					}
				});
				openButton.setToolTipText("Show Grest's informations and permit editing");
				openButton.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/open_grest.png")));
				toolBar.add(openButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			add(scrollPane, BorderLayout.CENTER);
			{
				grestsList = new GrestsList();
				grestsList.addListSelectionListener(this);
				
				grestsList.addMouseListener(new MouseAdapter() {
					public void mouseClicked(final MouseEvent e) {
						if(e.getClickCount() == 2){
							if(grestsList.getSelectedGrest() != null){
								openButton_actionPerformed(null);
							}
						}

						openButton.setEnabled(grestsList.getSelectedGrest() != null);
						openMenuItem.setEnabled(grestsList.getSelectedGrest() != null);
						removeMenuItem.setEnabled(grestsList.getSelectedGrest() != null);
					}
				});
				{
					JPopupMenu popupMenu = new JPopupMenu();
					addPopup(grestsList, popupMenu);
					{
						openMenuItem = new JMenuItem("Edit grest");
						openMenuItem.setEnabled(false);
						openMenuItem.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/open_grest.png")));
						openMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openButton_actionPerformed(e);
							}
						});
						popupMenu.add(openMenuItem);
					}
					{
						{
							JSeparator separator = new JSeparator();
							popupMenu.add(separator);
						}
					}
					{
						JMenuItem newMenuItem = new JMenuItem("New grest");
						newMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								newButton_actionPerformed(e);
							}
						});
						newMenuItem.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/grest_new.png")));
						popupMenu.add(newMenuItem);
					}
					{
						JMenuItem importMenuItem = new JMenuItem("Import grest..");
						importMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								newButton_actionPerformed(e);
							}
						});
						importMenuItem.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/import_grest.png")));
						popupMenu.add(importMenuItem);
					}
					removeMenuItem = new JMenuItem("Remove grest");
					removeMenuItem.setEnabled(false);
					removeMenuItem.setIcon(new ImageIcon(GrestsListPanel.class.getResource("/it/conteit/scoresmanager/gui/images/player_cancel.png")));
					removeMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeMenuItem_actionPerformed(e);
						}
					});
					popupMenu.add(removeMenuItem);
				}
				scrollPane.setViewportView(grestsList);
			}
		}

	}
	
	public IGrest getSelectedGrest(){
		return grestsList.getSelectedGrest();
	}

	protected void removeMenuItem_actionPerformed(ActionEvent e) {
		try {
			ApplicationSystem.getInstance().execute(RemoveGrestCommand.createCommand(grestsList.getSelectedGrest(), this));
		} catch (CommandExecutionException e1) {
			ApplicationSystem.getInstance().showWarning("Cannot remove the selected grest.", this);
		}
	}

	protected void importButton_actionPerformed(ActionEvent e) {		
		try {
			ApplicationSystem.getInstance().execute(ImportGrestCommand.createCommand());
		} catch (CommandExecutionException e1) {
			ApplicationSystem.getInstance().showWarning("Cannot import the specified grest.", this);
		} 
	}

	protected void openButton_actionPerformed(ActionEvent e) {
		try {
			ApplicationSystem.getInstance().execute(OpenGrestCommand.createCommand(grestsList.getSelectedGrest()));
		} catch (CommandExecutionException e1) {
			ApplicationSystem.getInstance().showWarning("Cannot open the selected grest.", this);
		}		
	}

	protected void newButton_actionPerformed(ActionEvent e) {
		try {
			ApplicationSystem.getInstance().execute(NewGrestCommand.createCommand());
		} catch (CommandExecutionException e1) {
			ApplicationSystem.getInstance().showWarning("Cannot create grest.", this);
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public static void main(String[] args){
		ApplicationSystem.initialize(new String[0]);
		GrestsManager.getInstance();

		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				JFrame f = new JFrame("Grests List");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.add(new GrestsListPanel());
				f.pack();
				f.setVisible(true);
			}
			
		});
	}

	public void valueChanged(ListSelectionEvent e) {
		for(ListSelectionListener l : listeners){
			l.valueChanged(e);
		}
	}
	
	public void addListSelectionListener(ListSelectionListener l){
		listeners.add(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l){
		listeners.remove(l);
	}
}
