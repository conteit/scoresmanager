package it.conteit.scoresmanager.gui.registry;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class SpinnerTableCellEditor extends AbstractCellEditor
implements TableCellEditor {

	private static final long serialVersionUID = 7688530898219631374L;
	final JSpinner spinner;

	public SpinnerTableCellEditor() {
		spinner = new JSpinner();
		spinner.setToolTipText("Don't insert text, use the arrows!");
		spinner.setEditor(new JSpinner.NumberEditor(spinner));
		final JFormattedTextField tf = ((JSpinner.NumberEditor)spinner.getEditor()).getTextField();
		tf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

			}
		});
		tf.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				try{
					Integer n = Integer.parseInt(tf.getText());
					if(tf.getText().equals("" + n.intValue())){
						spinner.setValue(n);
					}
				} catch(Exception e1){}
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		int v = (Integer) value;
		if(v > 0){
			spinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		} else if(v < 0){
			spinner.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, 0, 1));
		} else {
			spinner.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		}
		spinner.setValue(value);
		return spinner;
	}

	public Object getCellEditorValue() {
		return spinner.getValue();
	}
}
