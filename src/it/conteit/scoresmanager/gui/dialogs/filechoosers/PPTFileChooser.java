package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class PPTFileChooser extends JFileChooser {
	private static final long serialVersionUID = -7252622242797974969L;

	public PPTFileChooser(){
		super();
		setFileFilter(new PPTFileFilter());
	}
	
	public static File showDialog(Component parent){
		JFileChooser fC = new PPTFileChooser();
		if(JFileChooser.APPROVE_OPTION == fC.showSaveDialog(parent)){
			return fC.getSelectedFile();
		}
		
		return null;
	}
}
