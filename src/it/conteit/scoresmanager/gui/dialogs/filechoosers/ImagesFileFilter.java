package it.conteit.scoresmanager.gui.dialogs.filechoosers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImagesFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
			return true;
		}
		
		String n = f.getName().toLowerCase();
		return n.endsWith("png") || n.endsWith("jpg") || n.endsWith("bmp") || n.endsWith("jpeg");		
	}

	@Override
	public String getDescription() {
		return "Images";
	}
}
