package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class HTMLFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		
		String n = f.getName().toLowerCase();
		return n.endsWith("html");		
	}

	@Override
	public String getDescription() {
		return "HTML file";
	}
}
