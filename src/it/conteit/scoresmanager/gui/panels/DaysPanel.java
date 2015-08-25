package it.conteit.scoresmanager.gui.panels;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.EditDayCommand;
import it.conteit.scoresmanager.control.commands.NewDayCommand;
import it.conteit.scoresmanager.control.commands.RefreshDayCommand;
import it.conteit.scoresmanager.control.commands.RefreshRegistryCommand;
import it.conteit.scoresmanager.control.commands.RemoveDayCommand;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.gui.registry.IRegistryListener;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.gui.widgets.RegistryTreeTable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DaysPanel extends AbstractApplicationPanel implements IRegistryListener, MouseListener {
	private static final long serialVersionUID = -7417295505942230044L;
	
	private RegistryTreeTable registry;
	private IGrest grest;
	private JButton btnEditDay;
	private JButton btnRemoveDay;
	private JButton btnAddDay;
	private JPopupMenu popupMenu;
	private JMenuItem mntmAddDay;
	private JMenuItem mntmEditDay;
	private JMenuItem mntmRemoveDay;

	private JButton button_3;

	public DaysPanel() {
		this(null);
	}

	/**
	 * Create the panel.
	 */
	public DaysPanel(IGrest grest) {
		this.grest = grest;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JScrollPane scrollPane = new JScrollPane();
			{
				{
					registry = new RegistryTreeTable(grest);
					registry.addRegistryListener(this);
					registry.addMouseListener(this);
					scrollPane.setViewportView(registry);
				}
				
				popupMenu = new JPopupMenu();
				addPopup(registry, popupMenu);
				{
					mntmEditDay = new JMenuItem("Edit day");
					mntmEditDay.setEnabled(false);
					mntmEditDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_edit.png")));
					mntmEditDay.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {
							editDay_action();
						}
						
					});
					popupMenu.add(mntmEditDay);
				}
				popupMenu.addSeparator();
				{
					mntmAddDay = new JMenuItem("Add new day");
					mntmAddDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_new.png")));
					mntmAddDay.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {
							newDay_action();
						}
						
					});
					popupMenu.add(mntmAddDay);
				}
				{
					mntmRemoveDay = new JMenuItem("Remove day");
					mntmRemoveDay.setEnabled(false);
					mntmRemoveDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_cancel.png")));
					mntmRemoveDay.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {
							removeDay_action();
						}
						
					});
					popupMenu.add(mntmRemoveDay);
				}
			}
			add(scrollPane, "2, 2, fill, fill");
		}
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setOrientation(SwingConstants.VERTICAL);
			add(toolBar, "4, 2, default, top");
			
			{
				btnEditDay = new JButton("");
				btnEditDay.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editDay_action();
					}
				});
				btnEditDay.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				btnEditDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_edit.png")));
				btnEditDay.setToolTipText("Edit selected day");
				btnEditDay.setEnabled(false);
				toolBar.add(btnEditDay);
			}
			toolBar.addSeparator();
			{
				btnAddDay = new JButton("");
				btnAddDay.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						newDay_action();
					}
				});
				btnAddDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_new.png")));
				btnAddDay.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				btnAddDay.setToolTipText("Add a new day to grest");
				toolBar.add(btnAddDay);
			}
			toolBar.addSeparator(new Dimension(2,2));
			{
				btnRemoveDay = new JButton("");
				btnRemoveDay.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeDay_action();
					}
				});
				btnRemoveDay.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/day_cancel.png")));
				btnRemoveDay.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				btnRemoveDay.setToolTipText("Remove selected day from grest");
				btnRemoveDay.setEnabled(false);
				toolBar.add(btnRemoveDay);
			}
			
			toolBar.addSeparator();
			button_3 = new JButton("");
			button_3.setToolTipText("Refresh registry");
			button_3.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					refresh_action();
				}
			});
			button_3.setIcon(new ImageIcon(DaysPanel.class.getResource("/it/conteit/scoresmanager/gui/images/update.png")));
			toolBar.add(button_3);
		}
	}
	
	protected void refresh_action() {
		try {
			ApplicationSystem.getInstance().execute(RefreshRegistryCommand.createCommand(grest, this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showWarning("Some error occurs while refreshing registry.\n" + e.getMessage(), this);
		}
	}

	protected void editDay_action() {
		try {
			ApplicationSystem.getInstance().execute(EditDayCommand.createCommand(grest, registry.getSelectedDay(), this));
			ApplicationSystem.getInstance().execute(RefreshDayCommand.createCommand(registry.getSelectedDay(), this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot edit selected day.\n" + e.getMessage(), this);
		}
	}

	protected void removeDay_action() {
		try {
			ApplicationSystem.getInstance().execute(RemoveDayCommand.createCommand(grest, registry.getSelectedDay(), this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot remove selected day.\n" + e.getMessage(), this);
		}
	}

	protected void newDay_action() {
		try {
			ApplicationSystem.getInstance().execute(NewDayCommand.createCommand(grest, this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot add new day.\n" + e.getMessage(), this);
		}
	}

	@Override
	public void updateGUI(String[] validationResult, boolean isOk) {}

	private void updateButtonEnabled(boolean en) {		
		btnRemoveDay.setEnabled(en);
		btnEditDay.setEnabled(en);
		mntmRemoveDay.setEnabled(en);
		mntmEditDay.setEnabled(en);
	}

	public void daySelected(IDay d) {
		updateButtonEnabled(d != null);
	}

	public void scoreSelected(IScore s, IDay d) {
		updateButtonEnabled(d != null);
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

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			editDay_action();
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}
