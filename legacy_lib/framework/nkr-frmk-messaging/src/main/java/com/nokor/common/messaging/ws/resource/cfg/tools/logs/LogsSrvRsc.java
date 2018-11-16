package com.nokor.common.messaging.ws.resource.cfg.tools.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.nokor.common.messaging.share.tools.logs.LevelDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
@Path("/logs")
public class LogsSrvRsc extends BaseLogsSrvRsc {
	private static List<String> LOG_PATHS = Arrays.asList("com.nokor.efinance", "com.nokor.frmk", "org.seuksa.frmk");

	/**
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response files() {
		
		try {
			resetLastPosition();
			resetLogFileNames();

			setCurrentLogFile((File) null); 
			File logFile = getCurrentLogFile();
			getLogFiles();
			
			String output = START_HTML;
			output += "Current log file: " + logFile.getAbsolutePath() + _BR + _BR;
			output += "<table>";
			output += "<tr>";
			output += "<td style='center' colspan='100%'>";
			output += "List of log files in [" + currentLogFolder.getAbsolutePath() + "]";
			output += "</td>";
			output += "</tr>";
			for (File file : logFiles) {
				String fileSize = "";
				long bytes = file.length();
				boolean si = false;
				int unit = si ? 1000 : 1024;
			    if (bytes < unit) {
			    	fileSize = bytes + " B";
			    } else {
				    int exp = (int) (Math.log(bytes) / Math.log(unit));
				    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
				    fileSize = String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
			    }
			    
				output += "<tr>";
				output += "<td style='left'>";
				output += "  . " + "<a href='" + uriInfo.getAbsolutePath() + "/" + file.getName() + "/tail'>" + file.getName() + "</a>";
				output += "</td>";
				output += "<td style='center;width:20px'>&nbsp;</td>";
				output += "<td style='center'>";
				output += DateUtils.date2StringYYYYMMDD_HHMMSS(new Date(file.lastModified()));
				output += "</td>";
				output += "<td style='center;width:20px'>&nbsp;</td>";
				output += "<td style='center'>";
				output += fileSize;
				output += "</td>";
				output += "</tr>";
			}
			output += "</table>" + _BR;
			output += END_HTML;
			
			return ResponseHelper.ok(output);
			
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}

	}
	
	/**
	 * 
	 * @param refDataTableName
	 * @param ide
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{slash:/?}{fileName:.*}/tail")
	public Response tail(@PathParam("fileName") String fileName, @QueryParam("n") int nbLines) {
		setCurrentLogFile(fileName);
		return displayLogfilePage((String) null, nbLines, true);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/head")
	public Response head(@QueryParam("n") int nbLines) {
		return displayLogfilePage((String) null, nbLines, false);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/grep/{searchedWord}")
	public Response grep(@PathParam("searchedWord") String searchedWord, @QueryParam("n") int nbLines) {
		resetLastPosition();
		return displayLogfilePage(toContainsWord(searchedWord), nbLines, false);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/grepTail/{searchedWord}")
	public Response grepFromTail(@PathParam("searchedWord") String searchedWord, @QueryParam("n") int nbLines) {
		resetLastPosition();
		return displayLogfilePage(toContainsWord(searchedWord), nbLines, true);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/grepError{slash:/?}{searchedWord:.*}")
	public Response grepError(@PathParam("searchedWord") String searchedWord, @QueryParam("n") int nbLines) {
		resetLastPosition();
		return displayLogfilePage(Arrays.asList(EXCEPTION, toContainsWord(searchedWord)), -1, false);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/grepTailError{slash:/?}{searchedWord:.*}")
	public Response grepErrorFromTail(@PathParam("searchedWord") String searchedWord, @QueryParam("n") int nbLines) {
		resetLastPosition();
		return displayLogfilePage(Arrays.asList(EXCEPTION, toContainsWord(searchedWord)), nbLines, true);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/levels")
	public Response levels() {
		List<LevelDTO> levelDTOs = new ArrayList<LevelDTO>();
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (String logPath : LOG_PATHS) {
			Logger log = loggerContext.getLogger(logPath);
			levelDTOs.add(new LevelDTO(logPath, log.getLevel().toString()));
		}
		
		return ResponseHelper.ok(levelDTOs);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/levels/{levelCode}")
	public Response setLevel(@PathParam("levelCode") String levelCode) {
		
		List<LevelDTO> levelDTOs = new ArrayList<LevelDTO>();
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (String logPath : LOG_PATHS) {
			Logger log = loggerContext.getLogger(logPath);
			log.setLevel(Level.valueOf(levelCode.toUpperCase()));
			levelDTOs.add(new LevelDTO(logPath, log.getLevel().toString()));
		}
		
		return ResponseHelper.ok(levelDTOs);
	}
	

}
