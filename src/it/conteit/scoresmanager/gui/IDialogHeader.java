package it.conteit.scoresmanager.gui;

import java.awt.Color;

import javax.swing.ImageIcon;

public interface IDialogHeader {
	public void setTitle(String title);
	public void setDescription(String desc);
	public void setBackgroundColor(Color bkg);
	public void setSeparatorVisibility(boolean isVisible);
	public void setImage(ImageIcon img);
	public void setHeight(int h);
}
