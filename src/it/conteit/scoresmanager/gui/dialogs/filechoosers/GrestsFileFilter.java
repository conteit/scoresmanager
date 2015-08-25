package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GrestsFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		
		String n = f.getName().toLowerCase();
		return n.endsWith("grest") || n.endsWith("xml");		
	}

	@Override
	public String getDescription() {
		return "Grest file";
	}
}
