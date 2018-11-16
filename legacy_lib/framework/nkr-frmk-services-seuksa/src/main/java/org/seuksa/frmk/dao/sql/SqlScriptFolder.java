package org.seuksa.frmk.dao.sql;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;

/**
 * 
 * @author prasnar
 *
 */
public class SqlScriptFolder extends SqlScript {
	private String dirPath;
	
	/**
	 * 
	 * @param connection
	 */
	public SqlScriptFolder(Connection connection, String dirPath) {
		super(connection);
		this.dirPath = dirPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public static File[] getSqlFiles(String dirPath) {
		File dir = new File(dirPath);
		
		FilenameFilter sqlFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.endsWith(".sql");
			}
		};
		
		File[] files = dir.listFiles(sqlFilter);
		
		return files;
	}

	/**
	 * 
	 * @param dirPath
	 */
	public void runScripts() {
		File[] files = getSqlFiles(dirPath);
		if (files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		}
		
		for (File file : files) {
			runScriptFile(file);
		}
	}

}
