package com.nokor.common.messaging.ws.resource.cfg.tools.logs;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.rest.BaseResource;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseLogsSrvRsc extends BaseResource implements SeuksaServicesHelper {
	protected static final Logger LOGGER = (Logger) LoggerFactory.getLogger(BaseLogsSrvRsc.class);

	protected static final String EXT_LOG = "@*.log";
	protected static final String WORD_TOKEN = "@W";
	protected static final String REGEXP_CONTAINS_WORD = ".*(" + WORD_TOKEN + ").*";
	protected static final String EXCEPTION = ".*(Exception|error).*";
	protected static final String NUM_LINE_TOKEN = "@L";
	protected static final String NUM_LINE = "[" + NUM_LINE_TOKEN + "]";
	protected static final int NB_LINES_DEFAULT = 200;

	protected static final String SRV_TAIL = "tail";

	protected static Map<String, Integer> mapLastPosition = new HashMap<String, Integer>();
	protected static File currentLogFile;
	protected static File currentLogFolder;
	protected static List<File> logFiles;
	
	/**
	 * 
	 * @param regexp
	 * @param nbLines
	 * @param tail
	 * @return
	 */
	protected Response displayLogfilePage(String regexp, int nbLines, boolean tail) {
		return displayLogfilePage(regexp != null ? Arrays.asList(regexp) : new ArrayList<String>(), nbLines, tail);
	}
	
	/**
	 * 
	 * @param searchedRegexps
	 * @param nbLines
	 * @param tail
	 * @return
	 */
	protected Response displayLogfilePage(List<String> searchedRegexps, int nbLines, boolean tail) {
		File logFile = getCurrentLogFile();
		String displayLines = buildHtmlPageLogfileFromFile(logFile, getLastPosition(logFile.getName()), searchedRegexps, nbLines, tail);
		return ResponseHelper.ok(displayLines);
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	protected static int getLastPosition(String fileName) {
		if (mapLastPosition.get(fileName) == null) {
			resetLastPosition(fileName);
		}
		Object pos = mapLastPosition.get(fileName);
		return pos == null ? 0 : (int) pos;
	}
	
	/**
	 * 
	 */
	protected static void resetLastPosition() {
		mapLastPosition.put(getCurrentLogFile().getName(), 0);
	}
	
	/**
	 * 
	 * @param fileName
	 */
	protected static void resetLastPosition(String fileName) {
		mapLastPosition.put(fileName.toLowerCase(), 0);
	}
	
	/**
	 * 
	 * @param fileName
	 * @param line
	 */
	protected static void setLastPosition(String fileName, int line) {
		mapLastPosition.put(fileName.toLowerCase(), line);
	}
	
	/**
	 * 
	 */
	protected static void resetLogFileNames() {
		logFiles = null;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	protected static File getLogFile(String fileName) {
		if (StringUtils.isNotEmpty(fileName)) {
			for (File file : logFiles) {
				if (file.getName().equalsIgnoreCase(fileName)) {
					return file;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static List<File> getLogFiles() {
		if (logFiles != null) {
			return logFiles;
		}
		
		File logFile = getCurrentLogFile();
		if (logFile == null) {
			return new ArrayList<File>();
		}
		
		// Get file from folder
		logFiles = new ArrayList<File>();
		File[] files = logFile.getParentFile().listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	if (file.getName().toLowerCase().endsWith(".log")) {
		    		logFiles.add(file);
		    	}
		    }
		}
		return logFiles;
	}
	
	/**
	 * 
	 * @param logFile
	 * @param fromPosition
	 * @param searchedRegexps
	 * @param nbLines
	 * @param tail
	 * @return
	 */
	protected String buildHtmlPageLogfileFromFile(File logFile, int fromPosition, List<String> searchedRegexps, int nbLines, boolean tail) {
		
		try {
			
			List<String> lines = Files.readAllLines(Paths.get(logFile.getAbsolutePath()));

			int startLine;
			int endLine;

			if (nbLines == 0) {
				nbLines = NB_LINES_DEFAULT;
			}

			if (tail) {
				if (fromPosition == 0) {
					fromPosition = lines.size();
				}
				startLine =  nbLines < lines.size() ? fromPosition - nbLines : lines.size();
				endLine = startLine + nbLines;
			} else {
				if (fromPosition == 0) {
					fromPosition = 1;
				}
				startLine = fromPosition;
				endLine = nbLines;
			}
			if (startLine <= 0) {
				startLine = 1;
			}
			if (endLine > lines.size()) {
				endLine = lines.size();
			}
			
			
			String displayLines = "";
			for (long nLine = startLine; nLine < endLine && nLine < lines.size(); nLine++) {
				String line = lines.get((int) nLine);
				boolean matched = true;
				int nbRegexp = 0;
				for (String regexp : searchedRegexps) {
					if (StringUtils.isNotEmpty(regexp)) {
						matched &= Pattern.matches(regexp, line);
						nbRegexp++;
					}
				}
				if (nbRegexp == 0) {
					matched = true;
				}
				if (matched) {
					displayLines += toNumLine(nLine) + line + _BR;
				}
			}
			fromPosition = startLine;
			setLastPosition(logFile.getName(), fromPosition);
			
			String last = startLine >= 0 ? "<a href='" + getUriLogsPath() + logFile.getName() + _SLASH + SRV_TAIL + "?n=" + NB_LINES_DEFAULT + "'> &lt;&lt;Last" + "</a>" : "";
			String next = startLine < lines.size() ? "<a href='" + getUriLogsPath() + logFile.getName() + _SLASH + SRV_TAIL + "?n=-" + NB_LINES_DEFAULT + "'> Next&gt;&gt;" + "</a>" : "";
			
			displayLines = 
					  "____________________________________________________________" + _BR
					+ "<a href='" + getUriLogsPath() + "'> Back to log files list" + "</a>" + _BR
					+ " File name: " + logFile.getAbsolutePath() + _BR
					+ " Total lines : " + lines.size() + _BR
					+ " From : " + startLine + _BR
					+ " To   : " + endLine + _BR
					+ last
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ next + _BR
					+ "____________________________________________________________" + _BR + _BR
					+ displayLines;
			
			return displayLines;
			
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOGGER.error(errMsg, e);
			return null;
		}

	}
	
	/**
	 * 
	 * @param nLine
	 * @return
	 */
	protected static String toNumLine(long nLine) {
		return NUM_LINE.replace(NUM_LINE_TOKEN, String.valueOf(nLine));
	}
	
	/**
	 * 
	 * @param word
	 * @return
	 */
	protected static String toContainsWord(String word) {
		return StringUtils.isNotEmpty(word) ? REGEXP_CONTAINS_WORD.replace(WORD_TOKEN, word) : null;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static File getCurrentLogFile() {
		if (currentLogFile == null) {
			currentLogFile = fetchLogFile();
			if (currentLogFile !=null) { 
				currentLogFolder = currentLogFile.getParentFile();
			}
		}

		LoggerFactory.getLogger(BaseLogsSrvRsc.class).info("Logfile path [", currentLogFile.getAbsolutePath() + "]");
		
		return currentLogFile;
	}
	
	/**
	 * 
	 * @param file
	 */
	protected static void setCurrentLogFile(File file) {
		currentLogFile = file;
	}
	
	/**
	 * 
	 * @param fileName
	 */
	protected static void setCurrentLogFile(String fileName) {
		File file = getLogFile(fileName);
		if (file != null) {
			setCurrentLogFile(file);
		}
	}

	/**
	 * 
	 * @return
	 */
	protected static File fetchLogFile() {
		FileAppender<?> fileAppender = null;
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (Logger logger : context.getLoggerList()) {
		     fileAppender = findFileAppender(logger.iteratorForAppenders());
		     return new File(fileAppender.getFile());
		}

		return null;
	}
	
	/**
	 * 
	 * @param iteratorApp
	 * @return
	 */
	private static FileAppender<?> findFileAppender(Iterator<Appender<ILoggingEvent>> iteratorApp) {
		 while (iteratorApp.hasNext()) {
	           Object appender = iteratorApp.next();
	           LOGGER.debug("Appender [" + appender + "]");
	           if (appender instanceof FileAppender) {
	                return(FileAppender<?>) appender;
	           } else if (appender instanceof SiftingAppender) {
	        	   return findFileAppender(((SiftingAppender)appender).getAppenderTracker().allComponents().iterator());
	           }
		 }
		 return null;
	}
}
