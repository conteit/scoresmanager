package it.conteit.scoresmanager.control.management.storage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import it.conteit.scoresmanager.data.Color;
import it.conteit.scoresmanager.data.Day;
import it.conteit.scoresmanager.data.Grest;
import it.conteit.scoresmanager.data.IDay;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.data.IScore;
import it.conteit.scoresmanager.data.ITeam;
import it.conteit.scoresmanager.data.Partial;
import it.conteit.scoresmanager.data.Penality;
import it.conteit.scoresmanager.data.Score;
import it.conteit.scoresmanager.data.Team;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class XMLReader extends DefaultHandler implements IStorage {
	private String grestName;
	private Color tempColor;
	private Date tempCDate, tempLMDate;
	private ArrayList<ITeam> teams;
	private Queue<IScore> scores = new LinkedList<IScore>();
	private ArrayList<IDay> days = new ArrayList<IDay>();

	private static IGrest result;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private ArrayList<IStorageListener> listeners = new ArrayList<IStorageListener>();

	public XMLReader(IStorageListener l) {
		teams = new ArrayList<ITeam>();
		scores.clear();
		days.clear();
		tempColor = null;
		tempCDate = null;
		tempLMDate = null;

		if (l != null) {
			addStorageListener(l);
		}

		notifyListeners("Reader initialized");
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		notifyListeners(name + " loaded");
		try {
			if (name.equalsIgnoreCase("grest")) {
				ITeam[] t = new ITeam[teams.size()];
				teams.toArray(t);
				result = Grest.create(grestName, t, tempCDate);

				for (IDay d : days) {
					result.addDay(d);
				}

				result.setLogo(TemporaryStorage.getInstance().retrieveTempFile(
						grestName));
				result.setLastModDate(tempLMDate);

			} else if (name.equalsIgnoreCase("team")) {
				ITeam t = teams.get(teams.size() - 1);
				t.setColor(tempColor);
			} else if (name.equalsIgnoreCase("day")) {
				IDay d = days.get(days.size() - 1);

				while (!scores.isEmpty()) {
					d.addScore(scores.poll());
				}
			}
		} catch (Exception e) {
			throw new SAXException("Data consistency error");
		}
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		notifyListeners("Loading " + name);
		try {
			if (name.equalsIgnoreCase("grest")) {
				parseGrest(atts);
			} else if (name.equalsIgnoreCase("team")) {
				parseTeam(atts);
			} else if (name.equalsIgnoreCase("color")) {
				parseColor(atts);
			} else if (name.equalsIgnoreCase("day")) {
				parseDay(atts);
			} else if (name.equalsIgnoreCase("score")) {
				parseScore(atts);
			}
		} catch (Exception e) {
			throw new SAXException("Data consistency error");
		}
	}

	public static IGrest loadFromFile(String source) throws StorageException {
		return loadFromFile(new File(source), null);
	}

	public static IGrest loadFromFile(String source, IStorageListener l)
			throws StorageException {
		return loadFromFile(new File(source), l);
	}

	public static IGrest loadFromFile(File source) throws StorageException {
		return loadFromFile(source, null);
	}

	public static IGrest loadFromFile(File source, IStorageListener l)
			throws StorageException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;

		try {
			saxParser = factory.newSAXParser();

			saxParser.parse(source, new XMLReader(l));
		} catch (ParserConfigurationException e) {
			throw new StorageException("Error in configuration");
		} catch (SAXException e) {
			throw new StorageException("Error while loading");
		} catch (IOException e) {
			throw new StorageException("Error while retrieving file");
		}

		return result;
	}

	private void parseGrest(Attributes atts) throws Exception {
		notifyListeners("Parsing grest " + atts.getValue("name"));
		grestName = atts.getValue("name");
		tempCDate = dateFormat.parse(atts.getValue("createdOn"));
		tempLMDate = dateFormat.parse(atts.getValue("modifiedOn"));

		byte[] data = Base64.decode(atts.getValue("logo"));
		if (data != null) {
			TemporaryStorage.getInstance().createTempFile(grestName, data);
		}
	}

	private void parseDay(Attributes atts) throws Exception {
		notifyListeners("Parsing day " + atts.getValue("description"));
		days.add(Day.create(atts.getValue("description"), teams.size()));
	}

	private void parseColor(Attributes atts) throws Exception {
		notifyListeners("Parsing color");
		int r = Integer.parseInt(atts.getValue("r"));
		int g = Integer.parseInt(atts.getValue("g"));
		int b = Integer.parseInt(atts.getValue("b"));
		int a = Integer.parseInt(atts.getValue("a"));

		tempColor = new Color(new java.awt.Color(r, g, b, a));
	}

	private void parseTeam(Attributes atts) throws Exception {
		notifyListeners("Parsing team " + atts.getValue("name"));
		ITeam t = Team.create(atts.getValue("name"));

		byte[] data = Base64.decode(atts.getValue("avatar"));
		if (data != null) {
			TemporaryStorage.getInstance().createTempFile(
					grestName + "-" + t.getName(), data);
			t.setAvatar(TemporaryStorage.getInstance().retrieveTempFile(
					grestName + "-" + t.getName()));
		}
		teams.add(t);
	}

	private void parseScore(Attributes atts) throws Exception {
		notifyListeners("Parsing " + atts.getValue("type") + " score: "
				+ atts.getValue("description"));
		String type = atts.getValue("type");
		String name = atts.getValue("description");
		Integer[] vals = retrieveValues(atts);

		IScore s = null;

		if (type.equalsIgnoreCase("summary")) {
			s = Score.create(name, vals);
		} else if (type.equalsIgnoreCase("partial")) {
			s = Partial.create(name, vals);
		} else if (type.equalsIgnoreCase("penality")) {
			s = Penality.create(name, vals);
		}

		scores.offer(s);
	}

	public void addStorageListener(IStorageListener l) {
		listeners.add(l);
	}

	public void removeStorageListener(IStorageListener l) {
		listeners.remove(l);
	}

	private Integer[] retrieveValues(Attributes atts) {
		Integer[] res = new Integer[teams.size()];
		for (int i = 0; i < teams.size(); i++) {
			res[i] = Integer.parseInt(atts.getValue(teams.get(i).getName()));
		}

		notifyListeners("Score values", res);

		return res;
	}

	private void notifyListeners(String description) {
		notifyListeners(description, new Object[0]);
	}

	private void notifyListeners(String description, Object[] data) {
		for (IStorageListener l : listeners) {
			l.storageProgress(new StorageEvent(description, data));
		}
	}
}
