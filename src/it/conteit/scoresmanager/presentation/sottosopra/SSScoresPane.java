package it.conteit.scoresmanager.presentation.sottosopra;

import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.Score;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.ListSelectionModel;

public class SSScoresPane extends JPanel {
	private static final long serialVersionUID = 7828415631645112298L;
	
	private JComboBox comboBox;
	private DefaultListModel shownModel = new DefaultListModel();
	private DefaultListModel hiddenModel = new DefaultListModel();
	private JButton btnAdd;
	private JButton btnRemove;
	
	private Object selHidden;
	private Object selShown;

	public SSScoresPane(){
		this(null);
	}
	
	public SSScoresPane(IGrest g){
		setLayout(new BorderLayout(0, 0));
		setBorder(new CompoundBorder(UIManager.getBorder("TitledBorder.aquaVariant"), new EmptyBorder(5, 5, 5, 5)));
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			add(panel, BorderLayout.NORTH);
			{
				Object[] days = new Object[]{};
				if (g != null){
					days = g.days();
				}
				
				comboBox = new JComboBox(days);
				comboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dayChanged((IDay) comboBox.getSelectedItem());
					}
				});
				
				comboBox.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				if (comboBox.getModel().getSize() > 0){
					comboBox.setSelectedIndex(days.length - 1);
				}
				panel.add(comboBox);
			}
			{
				JLabel label = new JLabel("Day to be displayed:");
				label.setBorder(new EmptyBorder(2,10,2,2));
				label.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				panel.add(label, BorderLayout.NORTH);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("100dlu"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("center:40dlu"),
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("100dlu"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					RowSpec.decode("fill:default:grow"),}));
			panel.setBorder(new EmptyBorder(5,5,5,5));
			add(panel, BorderLayout.CENTER);
			{
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane, "2, 2");
					{
						final JList list = new JList(hiddenModel);
						list.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
						list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						list.setLayoutOrientation(JList.VERTICAL);
						list.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								btnAdd.setEnabled(list.getModel().getSize() > 0 && list.getSelectedIndex() >= 0);
								selHidden = list.getSelectedValue();
							}
						});
						scrollPane.setViewportView(list);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, "4, 2");
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
				{
					btnAdd = new JButton("Show >");
					btnAdd.setEnabled(false);
					btnAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							showPart();
						}
					});
					btnAdd.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					panel_1.add(btnAdd);
				}
				{
					btnRemove = new JButton("< Hide  ");
					btnRemove.setEnabled(false);
					btnRemove.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
					btnRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							hidePart();
						}
					});
					panel_1.add(btnRemove);
				}
			}
			{
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane, "6,2");
					{
						final JList list = new JList(shownModel);
						list.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
						list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						list.setLayoutOrientation(JList.VERTICAL);
						list.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								btnRemove.setEnabled(list.getModel().getSize() > 0 && list.getSelectedIndex() >= 0);
								selShown = list.getSelectedValue();
							}
						});
						scrollPane.setViewportView(list);
				}
			}
		}
		
		dayChanged((IDay) comboBox.getSelectedItem());
	}
	
	private void showPart() {
		shownModel.addElement(selHidden);
		hiddenModel.removeElement(selHidden);
	}
	
	private void hidePart() {
		hiddenModel.addElement(selShown);
		shownModel.removeElement(selShown);
	}

	private void dayChanged(IDay day) {
		shownModel.clear();
		hiddenModel.clear();
		
		if (day == null){
			return;
		}
		
		IScore[] scs = day.scores();
		for (int i = 0; i < scs.length; i++){
			hiddenModel.addElement(scs[i]);
		}
	}
	
	public Score[] getPartToBeShown(){
		Score[] res = new Score[shownModel.getSize()];
		for (int i = 0; i < shownModel.getSize(); i++){
			res[i] = (Score) shownModel.get(i);
		}
		
		return res;
	}

	public IDay getChoosedDay() {
		return (IDay) comboBox.getSelectedItem();
	}
}
