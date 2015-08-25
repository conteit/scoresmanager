package it.conteit.scoresmanager.control;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.conteit.scoresmanager.control.commands.Command;
import it.conteit.scoresmanager.control.commands.CommandExecutionException;
import it.conteit.scoresmanager.presentation.AbstractPresentation;

public class ApplicationSystem {
	private static final String APPLICATION_NAME = "Scores Manager";
	
	private static ApplicationSystem instance = null;
	private static Logger logger = Logger.getLogger("ScoresManager-ApplicationSystem");

	private static boolean IS_MAC;
	private static boolean SAVE_ON_EXIT = true;
	private static String AUTO_EXPORT = null;
	
	private LinkedList<IPresentationListListener> listeners = new LinkedList<IPresentationListListener>();
	
	private ArrayList<Class<? extends AbstractPresentation>> pres = new ArrayList<Class<? extends AbstractPresentation>>();
	private int defaultPres = 0;
	
	public ApplicationSystem(){
		logger.setLevel(Level.ALL);
		Handler handler = new ConsoleHandler();
		
		logger.addHandler(handler);
	}
	
	public String showInput(String message, String defaultValue, Component parent){
		return JOptionPane.showInputDialog(parent, message, defaultValue);
	}
	
	public DialogResults showQuestion(String message, Component parent){
		int res = JOptionPane.showConfirmDialog(parent, message, APPLICATION_NAME, JOptionPane.YES_NO_OPTION);
		
		
		if(res == JOptionPane.CANCEL_OPTION){
			return DialogResults.CANCEL;
		} else if(res == JOptionPane.YES_OPTION){
			return DialogResults.YES;
		} else {
			return DialogResults.NO;
		}
	}

	public DialogResults showConfirmation(String message, Component parent){
		int res = JOptionPane.showConfirmDialog(parent, message, APPLICATION_NAME, JOptionPane.OK_CANCEL_OPTION);
		
		if(res == JOptionPane.CANCEL_OPTION){
			return DialogResults.CANCEL;
		} else {
			return DialogResults.OK;
		}
	}
	
	public DialogResults showQuestionWithCancel(String message, Component parent){
		int res = JOptionPane.showConfirmDialog(parent, message, APPLICATION_NAME, JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(res == JOptionPane.YES_OPTION){
			return DialogResults.YES;
		} else {
			return DialogResults.NO;
		}
	}
	
	public int showOptionDialog(String message, Object[] options, Component parent){
		return JOptionPane.showOptionDialog(parent, message, APPLICATION_NAME, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE , null, options, -1);
	}
	
	public void showInformation(final String message, final Component parent){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				JOptionPane.showMessageDialog(parent, message, APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
	}
	
	public void showWarning(final String message, final Component parent){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				JOptionPane.showMessageDialog(parent, message, APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
			}
			
		});
	}
	
	public void showError(final String message, final Component parent){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				JOptionPane.showMessageDialog(parent, message, APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
			}
			
		});	
	}
	
	public void logInfo(String info){
		logger.log(Level.INFO, info);
	}
	
	public void logWarning(String warning){
		logger.log(Level.WARNING, warning);
	}
	
	public void logError(String error){
		logger.log(Level.SEVERE, error);
	}

	public void execute(Command cmd) throws CommandExecutionException{
		cmd.execute();
	}
	
	// Da definire bene, anche la gestione degli argomenti, forse meglio parsing esterno e passaggio di un oggetto
	@SuppressWarnings("unchecked")
	public static void initialize(String[] args){
		instance = new ApplicationSystem();
		String lcOSName = System.getProperty("os.name").toLowerCase();
		IS_MAC = lcOSName.startsWith("mac os x");
		
		if(IS_MAC){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", APPLICATION_NAME);
			//new it.conteit.scoresmanager.gui.MacMenuHandler();
		}
		
		File f = new File(System.getProperty("user.dir") + "/options");
		BufferedReader bufRd = null;
		
		try {
			FileReader rd = new FileReader(f);
			bufRd = new BufferedReader(rd);
			
			SAVE_ON_EXIT = Boolean.parseBoolean(bufRd.readLine());
			
			AUTO_EXPORT = bufRd.readLine();
			File auto_exp = new File(AUTO_EXPORT);
			if(!auto_exp.isDirectory() || !auto_exp.exists()){
				AUTO_EXPORT = null;
			}
			
			instance.setDefaultPresIndex(Integer.parseInt(bufRd.readLine()));
			String pkg_class;
			while((pkg_class = bufRd.readLine()) != null){
				instance.addPresentation((Class<? extends AbstractPresentation>) Class.forName(pkg_class));
			}
		} catch (Exception e) {
			instance.logWarning("Cannot register presentation module: " + e.getMessage());
		} finally {
			if(bufRd != null){
				try {
					bufRd.close();
				} catch (IOException e) {
					ApplicationSystem.getInstance().logError(e.getMessage());
				}
			}
		}
	}
	
	public boolean isMac(){
		return IS_MAC;
	}
	
	public String getApplicationDirectory(){
		return System.getProperty("user.dir");
	}
	
	public void setSaveOnExit(boolean save){
		SAVE_ON_EXIT = save;
		save();
	}
	
	public boolean saveOnExit(){
		return SAVE_ON_EXIT;
	}
	
	public void setAutoExportDir(String dir){
		AUTO_EXPORT = dir;
		save();
	}
	
	public void setAutoExportDir(File dir){
		if(dir != null){
			setAutoExportDir(dir.getPath());
		} else {
			setAutoExportDir((String) null);
		}
	}
	
	public boolean autoExportEnabled(){
		return (AUTO_EXPORT != null);
	}
	
	public String getAutoExportDir(){
		return AUTO_EXPORT;
	}
	
	private void save(){
		File f = new File(System.getProperty("user.dir") + "/options");
		if(f.exists()){
			f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				ApplicationSystem.getInstance().logError(e.getMessage());
			}
		}
		
		BufferedWriter bufWr = null;
		try {
			FileWriter wr = new FileWriter(f);
			bufWr = new BufferedWriter(wr);
			
			bufWr.write("" + SAVE_ON_EXIT);
			bufWr.newLine();
			
			if(AUTO_EXPORT != null){
				bufWr.write(AUTO_EXPORT);
				bufWr.newLine();
			}
			
			bufWr.write(""+defaultPres);
			bufWr.newLine();
			
			for (Class<? extends AbstractPresentation> p : pres){
				bufWr.write(p.getCanonicalName());
				bufWr.newLine();
			}
			
			bufWr.flush();
		} catch (Exception e) {
			ApplicationSystem.getInstance().logError(e.getMessage());
		} finally {
			if(bufWr != null){
				try {
					bufWr.close();
				} catch (IOException e) {
					ApplicationSystem.getInstance().logError(e.getMessage());
				}
			}
		}
	}
	
	public void addPresentation(Class<? extends AbstractPresentation> presClass){
		pres.add(presClass);
		
		for (IPresentationListListener m : listeners){
			m.presentationAdded(presClass, isDefaultPresentation(presClass));
		}
	}
	
	public void removePresentation(Class<? extends AbstractPresentation> presClass){
		pres.remove(presClass);
		
		if (isDefaultPresentation(presClass)){
			setDefaultPresIndex(0);
		}
		
		for (IPresentationListListener m : listeners){
			m.presentationRemoved(presClass);
		}
	}
	
	public boolean isDefaultPresentation(Class<?> pres_class){
		return pres.get(defaultPres).equals(pres_class);
	}
	
	public int getPresentationClassesCount(){
		return pres.size();
	}
	
	public Class<? extends AbstractPresentation> getDefaultPresentationClass(){
		return pres.get(defaultPres);
	}
	
	public Class<? extends AbstractPresentation> getPresentationClass(int index){
		return pres.get(index);
	}
	
	public int getDefaultPresIndex(){
		return defaultPres;
	}
	
	public void setDefaultPresIndex(int index){
		defaultPres = index;
		
		for (IPresentationListListener m : listeners){
			m.defaultPresentationChanged(getPresentationClass(index));
		}
	}
	
	public void setDefaultPresentation(
			Class<? extends AbstractPresentation> defPres) {
		for (int i = 0; i < pres.size(); i++){
			if (getPresentationClass(i).equals(defPres)){
				setDefaultPresIndex(i);
			}
		}
	}
	
	public void addListener(IPresentationListListener model){
		listeners.add(model);
	}
	
	public void removeListener(IPresentationListListener model){
		listeners.remove(model);
	}
	
	public static synchronized ApplicationSystem getInstance() throws NotInitializedException{
		if(instance == null){
			throw new NotInitializedException("System must be initialized before the use");
		}
		
		return instance;
	}
	
}
