package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class HTMLFileChooser extends JFileChooser {
	private static final long serialVersionUID = -7252622242797974969L;

	public HTMLFileChooser(){
		super();
		setFileFilter(new HTMLFileFilter());
	}
	
	public static File showDialog(Component parent){
		JFileChooser fC = new HTMLFileChooser();
		if(JFileChooser.APPROVE_OPTION == fC.showSaveDialog(parent)){
			return fC.getSelectedFile();
		}
		
		return null;
	}
}
