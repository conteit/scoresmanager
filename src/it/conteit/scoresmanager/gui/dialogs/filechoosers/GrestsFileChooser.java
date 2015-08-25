package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class GrestsFileChooser extends JFileChooser {
	private static final long serialVersionUID = -7252622242797974969L;

	public GrestsFileChooser(){
		super();
		setFileFilter(new GrestsFileFilter());
	}
	
	public static File showChooseDialog(Component parent){
		JFileChooser fC = new GrestsFileChooser();
		if(JFileChooser.APPROVE_OPTION == fC.showOpenDialog(parent)){
			return fC.getSelectedFile();
		}
		
		return null;
	}
	
	public static File showWhereToSaveDialog(Component parent){
		JFileChooser fC = new GrestsFileChooser();
		if(JFileChooser.APPROVE_OPTION == fC.showSaveDialog(parent)){
			return fC.getSelectedFile();
		}
		
		return null;
	}
}
