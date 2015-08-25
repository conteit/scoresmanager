package it.conteit.scoresmanager.control.commands;

import java.io.File;

import it.conteit.scoresmanager.control.ApplicationSystem;
import it.conteit.scoresmanager.control.management.storage.PageOrientation;
import it.conteit.scoresmanager.control.workers.GrestSerializer;
import it.conteit.scoresmanager.control.workers.HTMLExporter;
import it.conteit.scoresmanager.control.workers.PDFExporter;
import it.conteit.scoresmanager.data.IGrest;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.GrestsFileChooser;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.HTMLFileChooser;
import it.conteit.scoresmanager.gui.dialogs.filechoosers.PDFFileChooser;

public class ExportGrestCommand extends Command {
	public static final String EXPORT_TO_PDF = "pdf";
	public static final String EXPORT_TO_HTML = "html";
	public static final String EXPORT_TO_XML = "xml";

	public static final String GREST_TO_BE_OPENED = "grest";
	public static final String TYPE = "type";
	public static final String DIRECTORY = "dir";
	public static final String UNSUPERVISED = "unsup";

	public ExportGrestCommand() {
		super("ExportGrest");
	}

	@Override
	public void execute() throws CommandExecutionException {
		IGrest grest = (IGrest) getParameter(GREST_TO_BE_OPENED);
		String type = (String) getParameter(TYPE);
		String dir = (String) getParameter(DIRECTORY);

		if (type.equals(EXPORT_TO_PDF)) {
			File fc = null;
			if (dir != null) {
				fc = new File(dir + "/" + grest.getName() + ".pdf");
			} else {
				fc = PDFFileChooser.showDialog(getSource());
			}

			if (fc != null) {
				if (getParameter(UNSUPERVISED) == Boolean.TRUE) {
					PDFExporter.export(grest, fc, PageOrientation.HORIZONTAL);
				} else {
					if (ApplicationSystem.getInstance().showOptionDialog(
							"Choose page orientation:",
							new String[] { "Vertical", "Horizontal" },
							getSource()) == 1) {
						PDFExporter.export(grest, fc,
								PageOrientation.HORIZONTAL);
					} else {
						PDFExporter.export(grest, fc, PageOrientation.VERTICAL);
					}
				}
			}
		} else if (type.equals(EXPORT_TO_HTML)) {
			File fc = null;
			if (dir != null) {
				fc = new File(dir + "/" + grest.getName() + ".html");
			} else {
				fc = HTMLFileChooser.showDialog(getSource());
			}

			if (fc != null) {
				HTMLExporter.export(grest, fc);
			}
		} else if (type.equals(EXPORT_TO_XML)) {
			File fc = null;
			if (dir != null) {
				fc = new File(dir + "/" + grest.getName() + ".grest");
			} else {
				fc = GrestsFileChooser.showWhereToSaveDialog(getSource());
			}

			if (fc != null) {
				GrestSerializer.save(grest, fc);
			}
		}
	}

	public static Command createCommand(IGrest grest, String format) {
		Command cmd = new ExportGrestCommand();
		cmd.setParameter(GREST_TO_BE_OPENED, grest);
		cmd.setParameter(TYPE, format);

		return cmd;
	}

	public static Command createCommand(IGrest grest, String format, String dir) {
		return createCommand(grest, format, dir, false);
	}

	public static Command createCommand(IGrest grest, String format,
			String dir, boolean unsupervised) {
		Command cmd = new ExportGrestCommand();
		cmd.setParameter(GREST_TO_BE_OPENED, grest);
		cmd.setParameter(TYPE, format);
		cmd.setParameter(DIRECTORY, dir);
		cmd.setParameter(UNSUPERVISED, unsupervised);

		return cmd;
	}
}
