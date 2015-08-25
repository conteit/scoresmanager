package it.conteit.scoresmanager.control.management.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import it.conteit.scoresmanager.control.management.visitors.IAcceptDataVisitor;
import it.conteit.scoresmanager.control.management.visitors.IDataVisitor;
import it.conteit.scoresmanager.data.Color;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.data.Team;

public class XMLWriter implements IDataVisitor, IStorage{
	private FileOutputStream fos;
	private OutputFormat of;
	private XMLSerializer serializer;
	private ContentHandler handler;
	private ArrayList<String> errors = new ArrayList<String>();
	private ITeam[] teams;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private ArrayList<IStorageListener> listeners = new ArrayList<IStorageListener>();
	
	public XMLWriter(String path) throws StorageException{
		try {
			fos = new FileOutputStream(path);

			of = new OutputFormat("XML", "UTF-8", true);
			of.setIndent(4);

			serializer = new XMLSerializer(fos,of);
			handler = serializer.asContentHandler();
			
			notifyListeners("Writer initialized");
		} catch (FileNotFoundException e) {
			throw new StorageException("Cannot open stream");
		} catch (IOException e) {
			throw new StorageException("Cannot retrieve serializer handler");
		}
	}

	public void save(IAcceptDataVisitor v) throws StorageException{
		try {
			notifyListeners("Saving " + v.toString());
			handler.startDocument();

			v.accept(this);
			if(errors.size() > 0){
				String details = " ";

				for(String s : errors){
					details.concat(s + ", ");
				}

				throw new StorageException("Error while saving:" + details.substring(0, details.length() - 1));
			}

			handler.endDocument();
			notifyListeners(v.toString() + " saved");
		} catch (SAXException e) {
			throw new StorageException("Error while saving");
		}
	}

	public void closeStream() throws StorageException{
		try {
			fos.close();
			notifyListeners("Stream closed");
		} catch (IOException e) {
			throw new StorageException("Cannot close stream");
		}
	}

	public void visit(Score s) {
		notifyListeners("Visiting " + s);
		AttributesImpl atts = new AttributesImpl();

		atts.clear();
		
		if(s instanceof Partial){
			atts.addAttribute("", "", "type", "String", "partial");
		} else if (s instanceof Penality){
			atts.addAttribute("", "", "type", "String", "penality");
		} else {
			atts.addAttribute("", "", "type", "String", "summary");
		}
		
		atts.addAttribute("", "", "description", "String", s.getDescription());
		serializeScore(atts, s);

		try {
			handler.startElement("", "", "score", atts);

			handler.endElement("", "", "score");
		} catch (SAXException e) {
			errors.add("Cannot serialize Score " + s.getDescription());
		}
	}

	public void visit(Day d) {
		notifyListeners("Visiting " + d);
		AttributesImpl atts = new AttributesImpl();

		atts.clear();
		atts.addAttribute("", "", "description", "String", d.getDescription());

		try {
			handler.startElement("", "", "day", atts);

			for(IScore s : d.scores()){
				s.accept(this);
			}

			handler.endElement("", "", "day");
		} catch (SAXException e) {
			errors.add("Cannot serialize Day " + d.getDescription());
		}
	}

	public void visit(Grest g) {
		notifyListeners("Visiting " + g);
		AttributesImpl atts = new AttributesImpl();

		atts.clear();
		atts.addAttribute("", "", "name", "String", g.getName());
		atts.addAttribute("", "", "createdOn", "String", dateFormat.format(g.getCreationDate()));
		atts.addAttribute("", "", "modifiedOn", "String", dateFormat.format(g.getLastModDate()));
		
		try {
			serializeLogo(g, atts);
			
			handler.startElement("", "", "grest", atts);
			
			teams = g.teams();
			for(ITeam t : g.teams()){
				t.accept(this);
			}

			for(IDay d : g.days()){
				d.accept(this);
			}

			handler.endElement("", "", "grest");
		} catch (SAXException e) {
			errors.add("Cannot serialize Grest " + g.getName());
		} catch (Exception e) {
			errors.add("Cannot serialize Grest's logo (" + g.getName() + ")");
		}
	}
	
	private void serializeLogo(Grest g, AttributesImpl atts) throws Exception{		
		if(g.getLogo() != null && g.getLogo().exists() && g.getLogo().isFile()){
			notifyListeners("Serializing grest's logo");
			atts.addAttribute("", "", "logo", "CDATA", encodeData(g.getLogo()));
		}
	}
	
	private void serializeAvatar(Team t, AttributesImpl atts) throws Exception{
		if(t.getAvatar() != null && t.getAvatar().exists() && t.getAvatar().isFile()){
			notifyListeners("Serializing team's avatar");
			atts.addAttribute("", "", "avatar", "CDATA", encodeData(t.getAvatar()));
		}
	}
	
	private String encodeData(File f) throws Exception{
		FileInputStream fin = new FileInputStream(f);
		byte[] data = new byte[fin.available()];
		
		if(fin.read(data) < data.length){
			throw new Exception("Error while reading logo's data");
		}
		
		return Base64.encode(data);
	}
	
	public void visit(Color color) {
		notifyListeners("Visiting " + color);
		AttributesImpl atts = new AttributesImpl();

		atts.clear();
		atts.addAttribute("", "", "r", "String", "" + color.getValue().getRed());
		atts.addAttribute("", "", "g", "String", "" + color.getValue().getGreen());
		atts.addAttribute("", "", "b", "String", "" + color.getValue().getBlue());
		atts.addAttribute("", "", "a", "String", "" + color.getValue().getAlpha());

		try {
			handler.startElement("", "", "color", atts);

			handler.endElement("", "", "color");
		} catch (SAXException e) {
			errors.add("Cannot serialize Color " + color.toString());
		}
	}

	public void visit(Team team) {
		notifyListeners("Visiting " + team);
		AttributesImpl atts = new AttributesImpl();

		atts.clear();
		atts.addAttribute("", "", "name", "String", team.getName());

		try {
			serializeAvatar(team, atts);
			
			handler.startElement("", "", "team", atts);

			team.getColor().accept(this);

			handler.endElement("", "", "team");
		} catch (SAXException e) {
			errors.add("Cannot serialize Team " + team.getName());
		} catch (Exception e) {
			errors.add("Cannot serialize Team's avatar (" + team.getName() + ")");
		}
	}
	
	public void addStorageListener(IStorageListener l) {
		listeners.add(l);
	}

	public void removeStorageListener(IStorageListener l) {
		listeners.remove(l);
	}
	
	public String[] getErrorsList(){
		String[] res = new String[errors.size()];
		errors.toArray(res);
		
		return res;
	}
	
	public static void serializeToFile(IAcceptDataVisitor source, String destination, IStorageListener l) throws StorageException{
		XMLWriter xmlVisitor = new XMLWriter(destination);
		
		if(l != null){
			xmlVisitor.addStorageListener(l);
		}
		
		try{
			xmlVisitor.save(source);
		} finally {
			xmlVisitor.closeStream();
		}
	}

	public static void serializeToFile(IAcceptDataVisitor source, String destination) throws StorageException{
		serializeToFile(source, destination, null);	
	}

	private void serializeScore(AttributesImpl atts, IScore s){
		notifyListeners("Serializing score's values");
		for(int i=0; i<teams.length; i++){
			atts.addAttribute("", "", teams[i].getName(), "String", s.getValue(i).toString());
		}
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
