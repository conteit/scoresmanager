package it.conteit.scoresmanager.gui.renderers;

import it.conteit.scoresmanager.control.IPresentationListListener;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class PresentationListCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 3368031727253581531L;
		
	/**
	 * Create the panel.
	 */
	public PresentationListCellRenderer() {
		super("");
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
		setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		setOpaque(true);
	}

	@SuppressWarnings("unchecked")
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		Class val = (Class) value;
		
		if(isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		setText(val.getSimpleName() + (((IPresentationListListener) list.getModel()).isDefaultPresentation(val) ? " (Default)" : ""));
		setToolTipText(val.getCanonicalName());
		
		return this;
	}
}
