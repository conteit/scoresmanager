package it.conteit.scoresmanager.control.management.storage;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;
import it.conteit.scoresmanager.data.Color;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.InconsistencyException;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.data.Team;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.Table;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ExportToDoc implements IDataVisitor, IStorage{
	public static final String PDF = "pdf";
	public static final String HTML = "html";

	private ArrayList<IStorageListener> listeners = new ArrayList<IStorageListener>();
	private Document document;
	private SimpleTable table;
	private SimpleCell header;
	private int dayIndex;

	private java.awt.Color headerColor = new java.awt.Color(198, 198, 198);
	private java.awt.Color dayHeaderColor = new java.awt.Color(228, 228, 228);
	private java.awt.Color totalColor = new java.awt.Color(228, 228, 228);

	private String type;

	private static final String BASE_PATH = System.getProperty("user.dir") + "/bin/it/conteit/scoresmanager/gui/images/";
	private ArrayList<String> errors = new ArrayList<String>();

	private int columns;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public ExportToDoc(String destination, PageOrientation o, String type) throws StorageException{
		this.type = type;
		if(type.equals(HTML) || o == PageOrientation.VERTICAL){
			document = new Document(PageSize.A4);
		} else {
			document = new Document(PageSize.A4.rotate());
		}

		try {
			createWriter(destination, type);
		} catch (FileNotFoundException e) {
			throw new StorageException("Error while creating file");
		} catch (DocumentException e) {
			throw new StorageException("Error while creating PDF writer");
		}

		notifyListeners("Exporter initialized");
	}

	protected void createWriter(String destination, String type) throws FileNotFoundException, DocumentException{
		if(type.equals(PDF)){
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
			writer.setPdfVersion(PdfWriter.VERSION_1_7);
		} else if(type.equals(HTML)){
			HtmlWriter.getInstance(document, new FileOutputStream(destination));
		}
	}

	public void save(IAcceptDataVisitor v) throws StorageException{
		notifyListeners("Exporting " + v.toString());

		document.open();
		try {
			dayIndex = 0;
			table = new SimpleTable();
			table.setBorderWidth(1.3f);
			table.setWidthpercentage(100f);

			v.accept(this);
			
			float[] widths = new float[columns];
			widths[0] = 2f;
			for(int i=1; i<widths.length; i++){
				widths[i] = 0.5f;
			}
			
			if(type.equals(PDF)){
				PdfPTable pdfT = table.createPdfPTable();
				pdfT.setWidths(widths);
				document.add(pdfT);
			} else {
				Table t = table.createTable();
				t.setWidths(widths);
				document.add(t);
			}
			
			notifyListeners(v.toString() + " exported");
		} catch (Exception e) {
			String err = "";
			for(String s : errors){
				err += s + "; ";
			}
			err += "\b\b";

			throw new StorageException("Cannot export: " + err);
		}
	}

	public void closeStream() throws StorageException{
		try {
			document.close();
			notifyListeners("Stream closed");
		} catch (Exception e) {
			throw new StorageException("Cannot close stream");
		}
	}

	public void visit(Score s) {
		notifyListeners("Exploring score: " + s.toString());
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		String path = "";

		if(s instanceof Partial){
			path = BASE_PATH + "partial.png";
		} else if(s instanceof Penality){
			path = BASE_PATH + "penality.png";
		}

		SimpleCell cell = createCell(path, s.getDescription());
		cell.setSpacing_left(20f);
		row.add(cell);
		for(int i=0; i<s.teamCount(); i++){
			row.add(createCell(String.format("%d", s.getValue(i)), Element.ALIGN_RIGHT));
		}

		try {
			table.addElement(row);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}
	}

	public SimpleCell createEmptyCell(){
		return createCell(null);
	}

	public SimpleCell createCell(String imagefile, String s){
		return createCell(imagefile, s, Element.ALIGN_LEFT);
	}

	public SimpleCell createCell(String s){
		return createCell(null, s, Element.ALIGN_LEFT);
	}

	public SimpleCell createCell(String imagefile, String s, Font f){
		return createCell(imagefile, s, Element.ALIGN_LEFT, f);
	}

	public SimpleCell createCell(String s, int align){
		return createCell(null, s, align);
	}

	public SimpleCell createCell(String s, Font f){
		return createCell(null, s, Element.ALIGN_LEFT, f);
	}

	public SimpleCell createCell(String s, int align, Font f){
		return createCell(null, s, align, f);
	}

	public SimpleCell createCell(String imagefile, String s, int align){
		return createCell(imagefile, s, align, FontFactory.getFont(BaseFont.HELVETICA, 10));
	}

	public SimpleCell createCell(String imagefile, String s, int align, Font f){
		SimpleCell cell = new SimpleCell(SimpleCell.CELL);
		Paragraph p;
		cell.setBorder(SimpleCell.BOX);
		cell.setBorderWidth(0.3f);

		if(s != null){
			p = new Paragraph(s, f);
			if(imagefile != null && imagefile.equals("")){
				Image img1 = null;
				try {
					img1 = Image.getInstance(imagefile);
				} catch (Exception e) {
					errors.add(e.getMessage());
				}
				cell.add(img1);
			}
			p.setAlignment(align);
			cell.add(p);
		} else {
			cell.setWidthpercentage(0.1f);
		}

		cell.setPadding_bottom(7);
		cell.setPadding_left(5);
		cell.setPadding_right(5);

		return cell;
	}

	public void visit(Day d) {
		notifyListeners("Exploring day: " + d.toString());
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		SimpleCell cell = createCell(BASE_PATH + "date.png", d.getDescription(), FontFactory.getFont(BaseFont.HELVETICA_BOLD, 10));
		cell.setBorder(SimpleCell.TOP);
		cell.setBackgroundColor(dayHeaderColor);
		row.add(cell);

		for(int i=0; i<d.teamCount(); i++){
			SimpleCell cell2 = createEmptyCell();
			cell2.setBorder(SimpleCell.TOP);
			cell2.setBackgroundColor(dayHeaderColor);
			row.add(cell2);
		}

		try {
			table.addElement(row);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}

		for(int i=0; i<d.scores().length; i++){
			d.scores()[i].accept(this);
		}

		SimpleCell row2 = new SimpleCell(SimpleCell.ROW);
		row2.setBackgroundColor(dayHeaderColor);
		SimpleCell cell2 = createCell("Day total", FontFactory.getFont(BaseFont.HELVETICA_OBLIQUE, 10));
		cell2.setSpacing_left(20f);
		row2.add(cell2);

		IScore sum;
		try {
			sum = d.totalScore(); 
			for(int i=0; i<sum.teamCount(); i++){
				SimpleCell cell3 = createCell(String.format("%d", sum.getValue(i)), Element.ALIGN_RIGHT, FontFactory.getFont(BaseFont.HELVETICA_OBLIQUE, 10));

				row2.add(cell3);
			}
		} catch (InconsistencyException e1) {
			errors.add(e1.getMessage());
		}

		try {
			table.addElement(row2);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}

		SimpleCell row3 = new SimpleCell(SimpleCell.ROW);
		SimpleCell cell4 = createCell("Subtotal", FontFactory.getFont(BaseFont.HELVETICA_BOLDOBLIQUE, 10));
		cell4.setBorder(SimpleCell.NO_BORDER);
		cell4.setSpacing_left(20f);
		row3.add(cell4);

		IScore sum2;
		try {
			sum2 = d.getGrest().getSummaryAt(dayIndex); 
			dayIndex++;
			for(int i=0; i<sum2.teamCount(); i++){
				SimpleCell cell5 = createCell(String.format("%d", sum2.getValue(i)), Element.ALIGN_RIGHT, FontFactory.getFont(BaseFont.HELVETICA_BOLDOBLIQUE, 10));

				row3.add(cell5);
			}
		} catch (InconsistencyException e1) {
			errors.add(e1.getMessage());
		}

		try {
			table.addElement(row3);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}
	}

	public void visit(Grest g) {
		if(type.equals(PDF)){
			document.addTitle(g.getName() + " - Grest summary");
			document.addCreator("Scores Manager v. 5.0");
			document.addAuthor("Scores Manager v. 5.0");
			document.addSubject("Summary of grest's teams' scores, updated to " + dateFormat.format(new Date()));
		}
		
		try { 
			document.add(new Paragraph(g.getName(), FontFactory.getFont(BaseFont.HELVETICA_BOLD, 14)));
		} catch (DocumentException e1) {
			errors.add(e1.getMessage());
		}
		
		header = new SimpleCell(SimpleCell.ROW);
		SimpleCell cell = createCell("Description", FontFactory.getFont(BaseFont.HELVETICA_BOLD, 12));
		header.setBackgroundColor(headerColor);
		header.add(cell);

		columns = g.teamCount() + 1; 
			
		for(int i=0; i<g.teamCount(); i++){
			g.teams()[i].accept(this);
		}

		try {
			table.addElement(header);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}

		for(int i=0; i<g.days().length; i++){
			g.days()[i].accept(this);
		}

		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		SimpleCell cell2 = createCell("Total", FontFactory.getFont(BaseFont.HELVETICA_BOLD, 10));
		cell2.setBackgroundColor(totalColor);
		row.add(cell2);

		try {
			IScore tot = g.getTotalScore();

			for(int i=0; i<tot.teamCount(); i++){
				SimpleCell cell3 = createCell(String.format("%d", tot.getValue(i)), Element.ALIGN_RIGHT, FontFactory.getFont(BaseFont.HELVETICA_BOLD, 10));
				cell3.setBackgroundColor(totalColor);
				row.add(cell3);
			}
		} catch (InconsistencyException e) {
			errors.add(e.getMessage());
		}

		try {
			table.addElement(row);
		} catch (BadElementException e) {
			errors.add(e.getMessage());
		}
	}

	public void visit(Color color) {}

	public void visit(Team team) {
		SimpleCell cell = createCell(team.getName(), Element.ALIGN_RIGHT, FontFactory.getFont(BaseFont.HELVETICA_BOLD, 12));
		header.add(cell);
	}

	public void addStorageListener(IStorageListener l) {
		listeners.add(l);
	}

	public void removeStorageListener(IStorageListener l) {
		listeners.remove(l);
	}

	public static void exportToFile(IAcceptDataVisitor source, String destination, IStorageListener l, String type) throws StorageException{
		exportToFile(source, destination, l, PageOrientation.HORIZONTAL, type);
	}

	public static void exportToFile(IAcceptDataVisitor source, String destination, IStorageListener l, PageOrientation o, String type) throws StorageException{
		ExportToDoc exportToPDF = new ExportToDoc(destination, o, type); 

		if(l != null){
			exportToPDF.addStorageListener(l);
		}

		try{
			exportToPDF.save(source);
		} finally {
			exportToPDF.closeStream();
		}
	}

	public static void exportToFile(IAcceptDataVisitor source, String destination, String type) throws StorageException{
		exportToFile(source, destination, null, type);	
	}

	private void notifyListeners(String description){
		notifyListeners(description, new Object[0]);
	}

	private void notifyListeners(String description, Object[] data){
		for(IStorageListener l : listeners){
			l.storageProgress(new StorageEvent(description, data));
		}
	}
}
