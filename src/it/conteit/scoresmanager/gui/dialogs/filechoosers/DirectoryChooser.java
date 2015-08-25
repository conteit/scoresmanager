package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class DirectoryChooser extends JFileChooser {
	private static final long serialVersionUID = -7252622242797974969L;

	public DirectoryChooser(){
		super();
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	public static File showDialog(Component parent){
		JFileChooser fC = new DirectoryChooser();
		if(JFileChooser.APPROVE_OPTION == fC.showOpenDialog(parent)){
			return fC.getSelectedFile();
		}
		
		return null;
	}
}
