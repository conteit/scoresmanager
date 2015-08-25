package it.conteit.scoresmanager.gui.widgets;

import java.awt.Color;

import it.conteit.scoresmanager.gui.renderers.ColorComboRenderer;

import javax.swing.JComboBox;

public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = -5227968301789409788L;

	public ColorComboBox(){
		super();
		setRenderer(new ColorComboRenderer());
		
		addItem("Choose custom color..");
		
		addItem(Color.BLACK);
		addItem(Color.RED);
		addItem(Color.YELLOW);
		addItem(Color.GREEN);
		addItem(Color.BLUE);
	}
	
	public void chooseColor(Color c){
		for(int i=0; i<getItemCount(); i++){
			if(getItemAt(i).equals(c)){
				setSelectedIndex(i);
				return;
			}
		}
		
		addItem(c);
		setSelectedItem(c);
	}
}
