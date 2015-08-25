package it.conteit.scoresmanager.gui;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.DialogResults;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.control.commands.ExportGrestCommand;
import it.conteit.scoresmanager.control.commands.OpenGrestCommand;
import it.conteit.scoresmanager.control.commands.PresentationCommand;
import it.conteit.scoresmanager.control.management.storage.GrestsManager;
import it.conteit.scoresmanager.control.management.storage.StorageException;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.PreferencesDialog;
import it.conteit.scoresmanager.gui.models.PresentationComboModel;
import it.conteit.scoresmanager.gui.panels.EditGrestPanel;
import it.conteit.scoresmanager.gui.panels.GrestsListPanel;
import it.conteit.scoresmanager.gui.panels.WelcomePanel;
import it.conteit.scoresmanager.gui.renderers.PersonalDockingTheme;
import it.conteit.scoresmanager.gui.renderers.PresentationListCellRenderer;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.presentation.AbstractPresentation;
import it.conteit.scoresmanager.tools.GuiUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

public class MainFrame extends JFrame implements DockingWindowListener,
ListSelectionListener {
	private static final long serialVersionUID = -303474969976796432L;

	private JPanel m_contentPane;
	private RootWindow rootWindow;
	private ViewMap viewMap;
	private View grestsListView;
	private TabWindow tabWindow;
	private SplitWindow splitWindow;
	private DockingWindow currentView;

	private HashMap<String, Integer> ids = new HashMap<String, Integer>();
	private HashMap<View, EditGrestPanel> docs = new HashMap<View, EditGrestPanel>();

	private GrestsListPanel grestsListPanel;

	private IGrest selectedGrest;

	private JMenuItem menuItem4;

	private JMenuItem menuItem5;

	private JButton pdfButton;

	private JButton expButton;

	private JButton btnPresentation;

	private JComboBox presentationCombo;

	private JMenuItem menuItem6;

	private JMenuItem menuItem7;

	private JCheckBox saveOnExit;

	private static MainFrame instance = null;

	public MainFrame() {
		createMenu();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (ApplicationSystem.getInstance().saveOnExit()) {
					saveAll_actionPerformed();
				}
			}

			/*
			 * public void windowActivated(WindowEvent e){ createMenu(); }
			 */
		});

		addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				createMenu();
			}

			public void focusLost(FocusEvent e) {
			}

		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("ConteIt Scores Manager");
		setBounds(100, 100, 850, 600);
		m_contentPane = new JPanel();
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		m_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(m_contentPane);
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			m_contentPane.add(toolBar, BorderLayout.NORTH);
			{
				JButton button = new JButton("Preferences");
				button
				.setIcon(new ImageIcon(
						MainFrame.class
						.getResource("/it/conteit/scoresmanager/gui/images/preferences_small.png")));
				button.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PreferencesDialog.showDialog();
					}
				});
				toolBar.add(button);
			}
			toolBar.addSeparator(new Dimension(2, 2));
			{
				JButton button = new JButton("Save all");
				button.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				button
				.setIcon(new ImageIcon(
						MainFrame.class
						.getResource("/it/conteit/scoresmanager/gui/images/save_all.png")));
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveAll_actionPerformed();
					}
				});
				toolBar.add(button);
			}
			{
				saveOnExit = new JCheckBox("Save on exit", ApplicationSystem
						.getInstance().saveOnExit());
				saveOnExit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				saveOnExit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ApplicationSystem.getInstance().setSaveOnExit(
								saveOnExit.isSelected());
					}
				});
				toolBar.add(saveOnExit);
			}
			toolBar.addSeparator();
			{
				expButton = new JButton("Export");
				expButton.setEnabled(false);
				expButton
				.setIcon(new ImageIcon(
						MainFrame.class
						.getResource("/it/conteit/scoresmanager/gui/images/export.png")));
				expButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				expButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						export_action();
					}
				});
				toolBar.add(expButton);

				toolBar.addSeparator(new Dimension(2, 2));

				pdfButton = new JButton("Export To PDF");
				pdfButton.setEnabled(false);
				pdfButton
				.setIcon(new ImageIcon(
						MainFrame.class
						.getResource("/it/conteit/scoresmanager/gui/images/print.png")));
				pdfButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				pdfButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						print_action();
					}
				});
				toolBar.add(pdfButton);
			}
			toolBar.addSeparator();

			PresentationComboModel presMod = new PresentationComboModel();
			ApplicationSystem.getInstance().addListener(presMod);
			presMod.defaultPresentationChanged(ApplicationSystem.getInstance()
					.getDefaultPresentationClass());
			presentationCombo = new JComboBox(presMod);
			presentationCombo.setRenderer(new PresentationListCellRenderer());
			presentationCombo
			.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			toolBar.add(presentationCombo);

			for (int i = 0; i < ApplicationSystem.getInstance()
			.getPresentationClassesCount(); i++) {
				presMod.addElement(ApplicationSystem.getInstance()
						.getPresentationClass(i));
			}
			presMod.setSelectedItem(ApplicationSystem.getInstance()
					.getDefaultPresentationClass());

			btnPresentation = new JButton("Presentation");
			btnPresentation.setEnabled(false);
			btnPresentation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					presentation_action();
				}
			});
			btnPresentation
			.setIcon(new ImageIcon(
					MainFrame.class
					.getResource("/it/conteit/scoresmanager/gui/images/presentation.png")));
			btnPresentation.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			toolBar.add(btnPresentation);
			{

			}

			toolBar.add(new JPanel());
		}

		JPanel statusBar = new JPanel();
		add(statusBar, BorderLayout.SOUTH);

		initDockingFramework();
		createWelcomeTab();

		setLocation(GuiUtils.centerScreenFrame(getSize()));
	}

	private void createMenu() {
		{
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				if (!ApplicationSystem.getInstance().isMac()) {
					JMenu menu = new JMenu("Application");
					menuBar.add(menu);
					{
						JMenuItem menuItem = new JMenuItem("Preferences");
						menuItem
						.setIcon(new ImageIcon(
								MainFrame.class
								.getResource("/it/conteit/scoresmanager/gui/images/preferences_small.png")));
						menu.add(menuItem);

						menu.add(new JSeparator());

						JMenuItem menuItem2 = new JMenuItem("Exit");
						menu.add(menuItem2);
					}
				}
				JMenu menu2 = new JMenu("Grest");
				menuBar.add(menu2);
				{
					JMenuItem menuItem = new JMenuItem("New");
					menuItem
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/grest_new.png")));
					menu2.add(menuItem);

					JMenuItem menuItem2 = new JMenuItem("Import..");
					menuItem2
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/import_grest.png")));
					menu2.add(menuItem2);

					JMenuItem menuItem3 = new JMenuItem("Save all");
					menuItem3
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/save_all.png")));
					menu2.add(menuItem3);

					menu2.addSeparator();

					menuItem4 = new JMenuItem("Edit");
					menuItem4
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/open_grest.png")));
					menuItem4.setEnabled(false);
					menuItem4.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							open_action();
						}
					});

					menuItem5 = new JMenuItem("Export To PDF");
					menuItem5
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/print.png")));
					menuItem5.setEnabled(false);
					menuItem5.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							print_action();
						}
					});

					menuItem7 = new JMenuItem("Export To HTML");
					menuItem7
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/html.png")));
					menuItem7.setEnabled(false);
					menuItem7.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							printHTML_action();
						}
					});

					menuItem6 = new JMenuItem("Export");
					menuItem6
					.setIcon(new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/export.png")));
					menuItem6.setEnabled(false);
					menuItem6.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							export_action();
						}
					});

					menu2.add(menuItem6);
					menu2.add(menuItem5);
					menu2.add(menuItem7);

					menu2.addSeparator();

					menu2.add(menuItem4);
				}
			}
		}
	}

	protected void printHTML_action() {
		try {
			EditGrestPanel doc = docs.get(currentView);
			if (doc != null) {
				ApplicationSystem.getInstance().execute(
						ExportGrestCommand.createCommand(doc.getRelatedGrest(),
								ExportGrestCommand.EXPORT_TO_HTML));
			} else {
				ApplicationSystem.getInstance().showWarning(
						"No grest selected", this);
			}
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showWarning(
					"Error during exporting process", this);
		}
	}

	protected void export_action() {
		try {
			EditGrestPanel doc = docs.get(currentView);
			if (doc != null) {
				ApplicationSystem.getInstance().execute(
						ExportGrestCommand.createCommand(doc.getRelatedGrest(),
								ExportGrestCommand.EXPORT_TO_XML));
			} else {
				ApplicationSystem.getInstance().showWarning(
						"No grest selected", this);
			}
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showWarning(
					"Error during exporting process", this);
		}
	}

	@SuppressWarnings("unchecked")
	protected void presentation_action() {
		try {
			ApplicationSystem
			.getInstance()
			.execute(
					PresentationCommand
					.createCommand((Class<? extends AbstractPresentation>) (presentationCombo
							.getModel().getSelectedItem()), (Grest) docs.get(currentView).getRelatedGrest()));
		} catch (Exception e) {
			//TODO In caso di errore scoprire quali sono i grest aperti, se zero currentView = null e setComponentEnabled, altrimenti elenco e scelta, poi si riprova altrimenti errore
			ApplicationSystem.getInstance().showError(
					"Error while loading presentation system", this);
		}
	}

	protected void open_action() {
		if (selectedGrest != null) {
			try {
				ApplicationSystem.getInstance().execute(
						OpenGrestCommand.createCommand(selectedGrest));
			} catch (CommandExecutionException e) {
				ApplicationSystem.getInstance().showError(
						"Cannot open and edit grest: " + e.getMessage(), this);
			}
		}
	}

	protected void print_action() {
		try {
			EditGrestPanel doc = docs.get(currentView);
			if (doc != null) {
				ApplicationSystem.getInstance().execute(
						ExportGrestCommand.createCommand(doc.getRelatedGrest(),
								ExportGrestCommand.EXPORT_TO_PDF));
			} else {
				ApplicationSystem.getInstance().showWarning(
						"No grest selected", this);
			}
		} catch (CommandExecutionException e) {
			ApplicationSystem.getInstance().showWarning("Error while printing",
					this);
		}
	}

	protected void saveAll_actionPerformed() {
		try {
			GrestsManager.getInstance().saveAll();

			if (ApplicationSystem.getInstance().autoExportEnabled()) {
				IGrest[] grests = GrestsManager.getInstance().managedGrests();
				Queue<String> errors = new LinkedList<String>();

				for (int i = 0; i < grests.length; i++) {
					try {
						ApplicationSystem.getInstance().execute(
								ExportGrestCommand.createCommand(grests[i],
										ExportGrestCommand.EXPORT_TO_PDF,
										ApplicationSystem.getInstance()
										.getAutoExportDir(), true));
					} catch (CommandExecutionException e) {
						errors.add(e.getMessage());
					}
				}

				if (errors.size() > 0) {
					String message = "Error while exporting:\n";
					for (String s : errors) {
						message += (s + ";\n");
					}

					message += "\b\b";

					ApplicationSystem.getInstance().showWarning(message, this);
				}
			}
		} catch (StorageException e1) {
			ApplicationSystem.getInstance().showError(
					"Error while saving data.", this);
		}
	}

	private void createWelcomeTab() {
		View v = createTab(
				"Welcome page",
				new ImageIcon(
						MainFrame.class
						.getResource("/it/conteit/scoresmanager/gui/images/new_grest.png")),
						new WelcomePanel());
		v.getWindowProperties().setCloseEnabled(false);
	}

	public void createDocumentTab(IGrest grest) {
		Integer id = ids.get(grest.getName());

		if (id == null) {
			EditGrestPanel pane = new EditGrestPanel(grest);
			View tab = createTab(
					grest.getName(),
					new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/grest_small.png")),
							pane);
			docs.put(tab, pane);
		} else {
			viewMap.getView(id).restoreFocus();
		}
	}

	private View createTab(String name, Icon icon, Component content) {
		View v = new View(name, icon, content);
		int id = (int) Math.round(1000 * Math.random()) + 1;
		viewMap.addView(id, v);

		v.getWindowProperties().setUndockEnabled(false);
		v.getWindowProperties().setUndockOnDropEnabled(false);
		v.addListener(this);

		ids.put(name, id);
		getTabWindow().addTab(v);

		return v;
	}

	private void initDockingFramework() {
		if (rootWindow != null || viewMap != null) {
			return;
		}

		viewMap = new ViewMap();
		rootWindow = DockingUtil.createRootWindow(viewMap, true);
		rootWindow.setWindow(getSplitWindow());
		DockingWindowsTheme theme = new PersonalDockingTheme();
		rootWindow.getRootWindowProperties().addSuperObject(
				theme.getRootWindowProperties());

		rootWindow.getRootWindowProperties().getDockingWindowProperties()
		.setDragEnabled(false);
		rootWindow.getRootWindowProperties().setRecursiveTabsEnabled(false);
		rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
		rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);

		add(rootWindow);
	}

	private SplitWindow getSplitWindow() {
		if (splitWindow == null) {
			splitWindow = new SplitWindow(true, .3f, getGrestsListPanel(),
					getTabWindow());
		}

		return splitWindow;
	}

	private View getGrestsListPanel() {
		if (grestsListView == null) {
			grestsListView = new View(
					"Grests List",
					new ImageIcon(
							MainFrame.class
							.getResource("/it/conteit/scoresmanager/gui/images/list.png")),
							getGrestsListPanelComp());

			grestsListView.getWindowProperties().setUndockEnabled(false);
			grestsListView.getWindowProperties().setUndockOnDropEnabled(false);
			grestsListView.getWindowProperties().setCloseEnabled(false);
			grestsListView.getWindowProperties().setMaximizeEnabled(false);

			viewMap.addView(0, grestsListView);
		}

		return grestsListView;
	}

	private GrestsListPanel getGrestsListPanelComp() {
		if (grestsListPanel == null) {
			grestsListPanel = new GrestsListPanel();
			grestsListPanel.addListSelectionListener(this);
		}

		return grestsListPanel;
	}

	private TabWindow getTabWindow() {
		if (tabWindow == null) {
			tabWindow = new TabWindow();

			tabWindow.getWindowProperties().setCloseEnabled(false);
			tabWindow.getWindowProperties().setUndockEnabled(false);
			tabWindow.getWindowProperties().setUndockOnDropEnabled(false);
			tabWindow.getWindowProperties().setMaximizeEnabled(true);
			tabWindow.getWindowProperties().setMinimizeEnabled(false);
		}

		return tabWindow;
	}

	public void viewFocusChanged(View oldV, View newV) {
		currentView = newV;
		setComponentsEnabled();
	}

	public void windowAdded(DockingWindow arg0, DockingWindow arg1) {}

	public void windowClosed(DockingWindow v) {
		removeID(v);
		setComponentsEnabled();
	}

	private void removeID(DockingWindow v) {
		viewMap.removeView(ids.remove(v.getTitle()));
		docs.remove(v);
	}

	public void windowClosing(DockingWindow v) throws OperationAbortedException {
		AbstractApplicationPanel p = docs.get(v);

		if (p != null && !p.isContentValid()) {
			if (ApplicationSystem
					.getInstance()
					.showConfirmation(
							"Some informations are invalid.\nPress OK to discard changes and go on.",
							this) == DialogResults.CANCEL) {
				throw new OperationAbortedException();
			}
		} else if (p != null && p.isContentValid()) {
			if (p instanceof EditGrestPanel) {
				try {
					IGrest g = ((EditGrestPanel) p).getRelatedGrest();
					GrestsManager.getInstance().saveGrest(g);

					if (ApplicationSystem.getInstance().autoExportEnabled()){
						try {
							ApplicationSystem.getInstance().execute(
									ExportGrestCommand.createCommand(g,
											ExportGrestCommand.EXPORT_TO_PDF,
											ApplicationSystem.getInstance()
											.getAutoExportDir(), true));
						} catch (CommandExecutionException e) {
							ApplicationSystem.getInstance().showWarning(
									e.getMessage(), this);
						}
					}
				} catch (StorageException e) {
					ApplicationSystem
					.getInstance()
					.showError(
							"Error while saving. Changes will be lost on application closing!",
							this);
				}
			}
		}
	}

	public void windowDocked(DockingWindow arg0) {
	}

	public void windowDocking(DockingWindow arg0)
	throws OperationAbortedException {
	}

	public void windowHidden(DockingWindow arg0) {
	}

	public void windowMaximized(DockingWindow arg0) {
	}

	public void windowMaximizing(DockingWindow arg0)
	throws OperationAbortedException {
	}

	public void windowMinimized(DockingWindow arg0) {
	}

	public void windowMinimizing(DockingWindow arg0)
	throws OperationAbortedException {
	}

	public void windowRemoved(DockingWindow vList, DockingWindow remV) {
	}

	public void windowRestored(DockingWindow clsV) {
		
	}

	public void windowRestoring(DockingWindow arg0)
	throws OperationAbortedException {
	}

	public void windowShown(DockingWindow arg0) {
		this.currentView = arg0;
		setComponentsEnabled();
	}

	private void setComponentsEnabled() {
		boolean en = currentView != null;
		boolean en2 = selectedGrest != null;

		menuItem5.setEnabled(en);
		menuItem6.setEnabled(en);
		menuItem7.setEnabled(en);
		menuItem4.setEnabled(en2);
		pdfButton.setEnabled(en);
		expButton.setEnabled(en);
		btnPresentation.setEnabled(en && (presentationCombo.getModel().getSize() > 0));
	}

	public void windowUndocked(DockingWindow arg0) {
	}

	public void windowUndocking(DockingWindow arg0)
	throws OperationAbortedException {
		throw new OperationAbortedException();
	}

	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}

		return instance;
	}

	public void valueChanged(ListSelectionEvent e) {
		selectedGrest = getGrestsListPanelComp().getSelectedGrest();
		setComponentsEnabled();
	}

	public void setSaveOnExitCheckbox(boolean selected) {
		saveOnExit.setSelected(selected);
	}
}
