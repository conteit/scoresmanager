package it.conteit.scoresmanager.presentation.sottosopra;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.SlideShow;

public class ScoresToPPTConverter {
	
	/* Each ID has to be saved as shapeid##slideid */
	
	
	/* Welcome Slide */
	private static final int DATE_TEXT_BOX_ID = 153650;
	
	/* Summary Slide */
	private static final int[] PARTIAL_NAME_ID = 	{163961, 164021, 164081, 164141, 164201, 164261, 164321};
	private static final int[] PARTIAL_SC4_ID = 	{163971, 164031, 164091, 164151, 164211, 164271, 164331};
	private static final int[] PARTIAL_SC3_ID = 	{163981, 164041, 164101, 164161, 164221, 164281, 164341};
	private static final int[] PARTIAL_SC2_ID = 	{163991, 164051, 164111, 164171, 164231, 164291, 164351};
	private static final int[] PARTIAL_SC1_ID = 	{164001, 164061, 164121, 164181, 164241, 164301, 164361};
	private static final int[] PARTIAL_SEP_ID = 	{163911, 164011, 164071, 164131, 164191, 164251, 164311};
	
	/* Scores Slide*/
	private static final int SCORE1_LABEL_ID = 174192;
	private static final int SCORE1_BAR_ID = 174222;
	
	private static final int SCORE2_LABEL_ID = 174182;
	private static final int SCORE2_BAR_ID = 174212;
	
	private static final int SCORE3_LABEL_ID = 174172;
	private static final int SCORE3_BAR_ID = 174202;
	
	private static final int SCORE4_LABEL_ID = 174162;
	private static final int SCORE4_BAR_ID = 174142;
	
	private SlideShow ppt;
	private HashMap<Integer, Shape> shapes; 
	private boolean[] settedPartial = {false, false, false, false, false, false, false}; 
	                
	public ScoresToPPTConverter(String masterPath){
		try {
			ppt = new SlideShow(new HSLFSlideShow(masterPath));
			assert ppt.getSlides().length == 3;
			shapes = new HashMap<Integer, Shape>();
			
			for(int i = 0; i < ppt.getSlides().length; i++){
				Shape[] ss = ppt.getSlides()[i].getShapes();
				for(int j = 0; j < ss.length; j++){
					shapes.put(ss[j].getShapeId() * 10 + i, ss[j]);
				}
			}
		} catch(IOException e){
			throw new IllegalArgumentException("Invalid master path: no master ppt found");
		}
	}
	
	public void produce(String path) throws Exception{
		clearUnusedPartials();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			ppt.write(out);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Invalid path");
		} catch (IOException e) {
			throw new Exception("Error while producing ppt");
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					throw new Exception("Error while closing ppt file");
				}
			}
		}
	}
	
	public void setDate(Date d){
		TextBox date_tb = (TextBox) shapes.get(DATE_TEXT_BOX_ID);
		
		SimpleDateFormat d_f = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		String d_str = d_f.format(d).replace('“', 'i');
		date_tb.setText(d_str);
	}
	
	public boolean addPartial(String name, int sc1, int sc2, int sc3, int sc4){
		for (int i = 0; i < 7; i++){
			if(!settedPartial[i]){
				setPartial(i, name, sc1, sc2, sc3, sc4);
				return true;
			}
		}
		
		return false;
	}
	
	public void setPartial(int num, String name, int sc1, int sc2, int sc3, int sc4){
		if (num < 0 || num > 6){
			throw new IllegalArgumentException("No partial is associated");
		}
		
		if (name == null){
			throw new IllegalArgumentException("Name cannot be null");
		}
		
		((TextBox) shapes.get(PARTIAL_NAME_ID[num])).setText(name);
		((TextBox) shapes.get(PARTIAL_SC1_ID[num])).setText(""+sc1);
		((TextBox) shapes.get(PARTIAL_SC2_ID[num])).setText(""+sc2);
		((TextBox) shapes.get(PARTIAL_SC3_ID[num])).setText(""+sc3);
		((TextBox) shapes.get(PARTIAL_SC4_ID[num])).setText(""+sc4);
		settedPartial[num] = true;
	}
	
	public void setTotalScores(int sc1, int sc2, int sc3, int sc4){
		setTotalScores(sc1, sc2, sc3, sc4, 1.0f);
	}
	
	public void setTotalScores(int sc1, int sc2, int sc3, int sc4, float dim_ratio){
		int[] sc = new int[]{sc1,sc2,sc3,sc4};
		
		int max = computeMax(sc);
		int min = computeMin(sc);
		if (min > 0){
			min = 0;
		} else {
			min -= Math.round(0.1f * max);
		}
		
		float[] perc = new float[4];
		for(int i = 0; i < 4; i++){
			perc[i] = ((float) (sc[i] - min)) / (max - min);
		}
		
		setPosition(SCORE1_LABEL_ID, scaleWidth(SCORE1_BAR_ID, perc[0] * dim_ratio));
		setPosition(SCORE2_LABEL_ID, scaleWidth(SCORE2_BAR_ID, perc[1] * dim_ratio));
		setPosition(SCORE3_LABEL_ID, scaleWidth(SCORE3_BAR_ID, perc[2] * dim_ratio));
		setPosition(SCORE4_LABEL_ID, scaleWidth(SCORE4_BAR_ID, perc[3] * dim_ratio));
		
		TextBox lbl1 = (TextBox) shapes.get(SCORE1_LABEL_ID);
		lbl1.setText(""+sc1);
		TextBox lbl2 = (TextBox) shapes.get(SCORE2_LABEL_ID);
		lbl2.setText(""+sc2);
		TextBox lbl3 = (TextBox) shapes.get(SCORE3_LABEL_ID);
		lbl3.setText(""+sc3);
		TextBox lbl4 = (TextBox) shapes.get(SCORE4_LABEL_ID);
		lbl4.setText(""+sc4);
	}
	
	private int computeMin(int[] is) {
		int res = is[0];
		for (int i = 1; i < 4; i++){
			if (is[i] < res){
				res = is[i];
			}
		}
		
		return res;
	}
	
	private int computeMax(int[] is) {
		int res = is[0];
		for (int i = 1; i < 4; i++){
			if (is[i] > res){
				res = is[i];
			}
		}
		
		return res;
	}

	private int scaleWidth(int id, float perc){
		Shape s = shapes.get(id);
		Rectangle rect = s.getAnchor();
		rect.width = Math.round(perc * rect.width);
		s.setAnchor(rect);
		
		return rect.width;
	}
	
	private void setPosition(int id, int value){
		Shape s = shapes.get(id);
		Rectangle rect = s.getAnchor();
		rect.x = value + 135;
		s.setAnchor(rect);
	}
	
	private void clearUnusedPartials(){
		int c = 0;
		for(int i = 0; i < 7; i++){
			if(!settedPartial[i]){
				c++;
				((TextBox) shapes.get(PARTIAL_NAME_ID[i])).setText("");
				((TextBox) shapes.get(PARTIAL_SC1_ID[i])).setText("");
				((TextBox) shapes.get(PARTIAL_SC2_ID[i])).setText("");
				((TextBox) shapes.get(PARTIAL_SC3_ID[i])).setText("");
				((TextBox) shapes.get(PARTIAL_SC4_ID[i])).setText("");
				scaleWidth(PARTIAL_SEP_ID[i], 0.0f);
			}
		}
		
		if (c==7){
			ppt.removeSlide(1);
		}
	}
	
	public static void main(String[] args) throws Exception {
		String path = ScoresToPPTConverter.class.getResource("sp10-ss-master.ppt").getPath();
		ScoresToPPTConverter poi = new ScoresToPPTConverter(path);
		poi.setDate(new Date());
		poi.addPartial("P1", 11, 12, 13, 14);
		poi.addPartial("P2", 21, 22, 23, 24);
		poi.addPartial("P3", 31, 32, 33, 34);
		poi.addPartial("P4", 41, 42, 43, 44);
		poi.addPartial("P5", 51, 52, 53, 54);
		
		poi.setTotalScores(50, 200, 300, 100);
		poi.produce("slideshow_mod10.ppt");
	}

}
