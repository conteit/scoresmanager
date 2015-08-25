package it.conteit.scoresmanager.tools;

import java.awt.Dimension;
import java.awt.Point;

public class GuiUtils {
	public static Point centerScreenFrame(Dimension size){
		return centerScreenFrame(size.width, size.height);
	}
	
	public static Point centerScreenFrame(int width, int height){
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit()
		.getScreenSize();
		int xPos= new Double((screensize.getWidth() - width) / 2).intValue();
		int yPos = new Double((screensize.getHeight() - height) / 2).intValue();
		
		return new Point(xPos, yPos);
	}
}
