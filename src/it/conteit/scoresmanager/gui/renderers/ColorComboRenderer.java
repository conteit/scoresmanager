package it.conteit.scoresmanager.gui.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ColorComboRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = -7157509281881478326L;
	
	private JPanel colorPreview;
	protected Color m_c = Color.BLACK;

	public ColorComboRenderer() {
		super(new BorderLayout());
		
		colorPreview = new JPanel();
		add(colorPreview, BorderLayout.CENTER);
	}

	public Component getListCellRendererComponent(JList list, Object obj,
			int row, boolean sel, boolean hasFocus) {
		if(sel){
			setBorder(new CompoundBorder(
					new MatteBorder(2, 10, 2, 10, list.getSelectionBackground()), new LineBorder(
							list.getSelectionForeground())));
		} else {
			setBorder(new CompoundBorder(
					new MatteBorder(2, 10, 2, 10, list.getBackground()), new LineBorder(
							list.getForeground())));
		}
		
		if (obj instanceof Color){
			m_c = (Color) obj;
			colorPreview.setBackground(m_c);
		} else if(obj instanceof String){
			return (new DefaultListCellRenderer()).getListCellRendererComponent(list, obj, row, sel, hasFocus);
		}
		
		setToolTipText(String.format("(R=%d, G=%d, B=%d, A=%d)", m_c.getRed(), m_c.getGreen(), m_c.getBlue(), m_c.getAlpha()));
		
		return this;
	}
}