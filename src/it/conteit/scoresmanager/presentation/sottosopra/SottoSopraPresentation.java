package it.conteit.scoresmanager.presentation.sottosopra;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.PPTFileChooser;
import it.conteit.scoresmanager.gui.valiators.AbstractApplicationPanel;
import it.conteit.scoresmanager.presentation.AbstractPresentation;
import it.conteit.scoresmanager.presentation.PresentationException;

public class SottoSopraPresentation extends AbstractPresentation {

	@Override
	public AbstractApplicationPanel configurationPanel() {
		throw new UnsupportedOperationException("No configuration panel: nothing to be configured");
	}

	@Override
	public void loadConfiguration() throws PresentationException {}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public void execute(Grest g) throws PresentationException {
		String path = ScoresToPPTConverter.class.getResource("sp10-ss-master.ppt").getPath();
		ScoresToPPTConverter converter = new ScoresToPPTConverter(path);
		
		SottoSopraPresDialog dialog = new SottoSopraPresDialog(null, g);
		dialog.setVisible(true);
		
		Entry[] data = dialog.getResult();
		converter.setDate(new java.util.Date());
		
		if (data == null){
			return;
		}
		
		for (int i = 0; i < data.length; i++){
			if(i == data.length - 1){
				converter.setTotalScores(data[i].s1, data[i].s2, data[i].s3, data[i].s4);
			} else {
				converter.addPartial(data[i].name, data[i].s1, data[i].s2, data[i].s3, data[i].s4);
			}
		}
		
		File pres_file = PPTFileChooser.showDialog(null);
		
		if (pres_file == null){
			return;
		}
		
		try {
			converter.produce(pres_file.getPath());
		} catch (Exception e) {
			ApplicationSystem.getInstance().showError("Cannot produce presentation!\n" + e.getMessage(), null);
		}
		
		if (Desktop.isDesktopSupported()) {
	        Desktop desktop = Desktop.getDesktop();
	        if (desktop.isSupported(Desktop.Action.OPEN)) {
	        	try {
					desktop.open(pres_file);
				} catch (IOException e) {
					throw new PresentationException(e);
				}
	        }
		}
	}

}
