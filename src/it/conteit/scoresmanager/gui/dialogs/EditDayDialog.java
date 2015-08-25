package it.conteit.scoresmanager.gui.dialogs;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.NewPartialCommand;
import it.conteit.scoresmanager.control.commands.NewPenalityCommand;
import it.conteit.scoresmanager.control.commands.RemoveScoreCommand;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.gui.registry.IRegistryListener;
import it.conteit.scoresmanager.gui.widgets.DayTreeTable;
import it.conteit.scoresmanager.tools.GuiUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EditDayDialog extends JDialog implements IRegistryListener {
	private static final long serialVersionUID = -3908697484579683658L;
	
	private final JPanel m_contentPanel = new JPanel();
	private DayTreeTable registry;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JButton button0;
	private JPopupMenu popupMenu;
	private JMenuItem menuItem;
	private JMenuItem menuItem_1;
	private JMenuItem menuItem_2;
	private JMenuItem menuItem0;

	private IGrest grest;

	public EditDayDialog() {
		this(null, null);
	}
	
	/**
	 * Create the dialog.
	 */
	public EditDayDialog(IDay day, IGrest grest) {
		super((Frame) null);
		setModal(true);
		this.grest = grest;
		setTitle("Edit day");
		setBounds(100, 100, 550, 400);
		getContentPane().setLayout(new BorderLayout());
		m_contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		m_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(m_contentPanel, BorderLayout.CENTER);
		{
			JScrollPane scrollPane = new JScrollPane();
			
			registry = new DayTreeTable(day, grest);
			
			popupMenu = new JPopupMenu();
			addPopup(registry, popupMenu);
			
			menuItem0 = new JMenuItem("Rename");
			menuItem0.setEnabled(false);
			menuItem0.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/rename.png")));
			menuItem0.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					edit_action();
				}
			});
			popupMenu.add(menuItem0);
			
			popupMenu.addSeparator();
			
			menuItem = new JMenuItem("Add partial");
			menuItem.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/partial_new.png")));
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addPartial_action();
				}
			});
			popupMenu.add(menuItem);
			
			menuItem_1 = new JMenuItem("Add penality");
			menuItem_1.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/penality_new.png")));
			menuItem_1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					remove_action();
				}
			});
			popupMenu.add(menuItem_1);
			
			popupMenu.addSeparator();
			
			menuItem_2 = new JMenuItem("Remove score");
			menuItem_2.setEnabled(false);
			menuItem_2.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/player_cancel.png")));
			menuItem_2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addPartial_action();
				}
			});
			popupMenu.add(menuItem_2);
			m_contentPanel.add(scrollPane, "2, 2, fill, fill");
			
			scrollPane.setViewportView(registry);
		}
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.setFloatable(false);
			
			button0 = new JButton("");
			button0.setEnabled(false);
			button0.setToolTipText("Rename selected item (day or score)");
			button0.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					edit_action();
				}
			});
			button0.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/rename.png")));
			toolBar.add(button0);
			
			toolBar.addSeparator();
			
			button = new JButton("");
			button.setToolTipText("Add new partial");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addPartial_action();
				}
			});
			button.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/partial_new.png")));
			toolBar.add(button);
			
			toolBar.addSeparator(new Dimension(2,2));
			
			button_1 = new JButton("");
			button_1.setToolTipText("Add new penality");
			button_1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addPenality_action();
				}
			});
			button_1.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/penality_new.png")));
			toolBar.add(button_1);
			
			toolBar.addSeparator();
			
			button_2 = new JButton("");
			button_2.setToolTipText("Remove selected score");
			button_2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					remove_action();
				}
			});
			button_2.setEnabled(false);
			button_2.setIcon(new ImageIcon(EditDayDialog.class.getResource("/it/conteit/scoresmanager/gui/images/player_cancel.png")));
			toolBar.add(button_2);
			
			m_contentPanel.add(toolBar, "4, 2, default, top");
		}
		
		registry.addRegistryListener(this);
		
		setLocation(GuiUtils.centerScreenFrame(getSize()));
	}

	protected void edit_action() {
		if(registry.getSelectedScore() != null){
			ScoreNameInputDialog.showDialog(registry.getSelectedDay(), registry.getSelectedScore());
		} else if(registry.getSelectedDay() != null){
			DayNameInputDialog.showDialog(grest, registry.getSelectedDay());
		}
		
	}

	protected void remove_action() {
		try {
			ApplicationSystem.getInstance().execute(RemoveScoreCommand.createCommand(registry.getSelectedScore(), registry.getSelectedDay(), this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot add remove selected score.\n" + e.getMessage(), this);
		}
	}

	protected void addPenality_action() {
		try {
			ApplicationSystem.getInstance().execute(NewPenalityCommand.createCommand(registry.getSelectedDay(), this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot add new penality.\n" + e.getMessage(), this);
		}
	}

	protected void addPartial_action() {
		try {
			ApplicationSystem.getInstance().execute(NewPartialCommand.createCommand(registry.getSelectedDay(), this));
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showError("Cannot add new partial.\n" + e.getMessage(), this);
		}
	}

	public static void showDialog(IDay day, IGrest grest) {
		EditDayDialog dialog = new EditDayDialog(day, grest);
		
		dialog.setVisible(true);
	}

	private void updateButtonEnabled(boolean en, boolean en2) {		
		button0.setEnabled(en || en2);
		button.setEnabled(en);
		button_1.setEnabled(en);
		button_2.setEnabled(en2);
		
		menuItem0.setEnabled(en || en2);
		menuItem.setEnabled(en);
		menuItem_1.setEnabled(en);
		menuItem_2.setEnabled(en2);
	}
	
	public void daySelected(IDay d) {
		updateButtonEnabled(d != null, false);
	}

	public void scoreSelected(IScore s, IDay d) {
		updateButtonEnabled(d != null, s != null);
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
}
