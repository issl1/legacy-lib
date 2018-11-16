package com.nokor.common.messaging.share.tools.logs;

import java.io.Serializable;

/**
 * 
 * @author prasnar
 *
 */
public class LevelDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -3440133648690451923L;

	private String logPath;
	private String level;
	
	/**
	 * 
	 * @param logPath
	 * @param level
	 */
	public LevelDTO(String logPath, String level) {
		this.logPath = logPath;
		this.level = level;
	}

	

	/**
	 * @return the logPath
	 */
	public String getLogPath() {
		return logPath;
	}



	/**
	 * @param logPath the logPath to set
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}



	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
