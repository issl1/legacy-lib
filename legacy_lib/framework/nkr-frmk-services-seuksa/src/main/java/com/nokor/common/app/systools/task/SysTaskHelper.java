package com.nokor.common.app.systools.task;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.seuksa.frmk.dao.sql.SqlScript;
import org.seuksa.frmk.dao.sql.SqlScriptFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.systools.model.EnSysTaskType;
import com.nokor.common.app.systools.model.EnSysTaskWhere;
import com.nokor.common.app.systools.model.SysTask;
import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;

/**
 * 
 * @author prasnar
 *
 */
public class SysTaskHelper {
	private static final Logger logger = LoggerFactory.getLogger(SysTaskHelper.class);

	private static final String DIR_DONE = "/_done";
	
	/**
	 * 
	 */
	public static void execTasksBeforeInitSpringContext() {
		if (!SeuksaAppConfigFileHelper.isSysTasksRequired()) {
			return;
		}
		String dirPath = SeuksaAppConfigFileHelper.getSysTaskBeforeSpringContextSqlFolder();
		File[] files = SqlScriptFolder.getSqlFiles(dirPath);
		if (files == null || files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		}
		execTasksSqlFromFiles(files);
		fillTasksFromSqlFiles(files, EnSysTaskWhere.PLACE_01);
		

	}

	/**
	 * 
	 */
	public static void execTasksAfterInitSpringContext() {
		if (!SeuksaAppConfigFileHelper.isSysTasksRequired()) {
			return;
		}
		String dirPath = SeuksaAppConfigFileHelper.getSysTaskAfterSpringContextSqlFolder();
		File[] files = SqlScriptFolder.getSqlFiles(dirPath);
		if (files == null || files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		}
		fillTasksFromSqlFiles(files, EnSysTaskWhere.PLACE_10);
		
		execTasks(EnSysTaskWhere.PLACE_10);
	}
	
	/**
	 * 
	 */
	public static void execTasksAfterStartup() {
		if (!SeuksaAppConfigFileHelper.isSysTasksRequired()) {
			return;
		}
		String dirPath = SeuksaAppConfigFileHelper.getSysTaskAfterStartupSqlFolder();
		File[] files = SqlScriptFolder.getSqlFiles(dirPath);
		if (files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		}
		fillTasksFromSqlFiles(files, EnSysTaskWhere.PLACE_20);
		execTasks(EnSysTaskWhere.PLACE_20);
	}
	
	/**
	 * 
	 */
	private static void fillTasksFromSqlFolder(String dirPath, EnSysTaskWhere where) {
		File[] files = SqlScriptFolder.getSqlFiles(dirPath);
		if (files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		}
		fillTasksFromSqlFiles(files, where);
	}
	
	/**
	 * 
	 * @param files
	 * @param where
	 */
	private static void fillTasksFromSqlFiles(File[] files, EnSysTaskWhere where) {
		if (files == null || files.length == 0) {
			return;
		}
		Arrays.sort(files);
		for (File file : files) {
			if (!SysTaskJdbcDao.isTaskExists(file.getName())) {
				SysTaskJdbcDao.createSqlFileTask(file.getAbsolutePath(), where);
			}
		}
		
	}
	
	/**
	 * 
	 */
	private static void execTasksSqlFromFolder(String dirPath) {
		File[] files = SqlScriptFolder.getSqlFiles(dirPath);
		if (files == null || files.length == 0) {
		    logger.info("The directory [" +  dirPath + "] is empty");
		    return;
		}
		execTasksSqlFromFiles(files);
	}
	
	/**
	 * 
	 */
	private static void execTasksSqlFromFiles(File[] files) {
		Arrays.sort(files);
		for (File file : files) {
			String errMsg = execTaskSQLFile(file.getAbsolutePath());
			if (errMsg != null) {
				throw new IllegalStateException(errMsg);
			}
		}

	}
	
	/**
	 * 
	 */
	private static void execTasks(EnSysTaskWhere where) {
		try {
			List<SysTask> tasks = SysTaskJdbcDao.listTasksNoExecuted(where);
			logger.info("[" + where.getCode() + "] - Nb tasks: " + tasks.size());
			int nbErrors = 0;
			for (SysTask task : tasks) {
				logger.info(task.toString());
				boolean res = true;
				if (task.getType().equals(EnSysTaskType.JAVA)) {
					res = execTaskJava(task);
				} else if (task.getType().equals(EnSysTaskType.SQL_FILE)) {
					res = execTaskSqlFile(task);
				} else if (task.getType().equals(EnSysTaskType.SQL)) {
					res = execTaskSql(task);
				} else {
					throw new IllegalStateException("[" + task.getType().getCode() +  "] not implemented yet.");
				}
				
				if (!res) {
					nbErrors++;
				}
			
			}
			logger.info("Nb tasks: " + tasks.size());
			logger.info("Nb errors: " + nbErrors);
		} catch (Exception e) {
			logger.error("ERROR - " + e.getMessage(), e);
		}
		
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public static boolean execTaskJava(SysTask task) {
		String errMsg = null;
		try {
			Class clazz = Class.forName(task.getCode());
			Method method = clazz.getMethod(task.getMethod(), null);
			Object res = method.invoke(null, null);
		} catch (Exception e) {
			errMsg = displayStackTrace(e.getStackTrace());
			logger.error("ERROR - " + task.toString(), e);
		}
		task.setErrorMsg(errMsg);
		SysTaskJdbcDao.updateExecutedTask(task);
		
		return errMsg == null;
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public static boolean execTaskSqlFile(SysTask task) {
		String errMsg = execTaskSQLFile(task.getCode());
		task.setErrorMsg(errMsg);
		SysTaskJdbcDao.updateExecutedTask(task);
		
		return errMsg != null;
	}
	
	/**
	 * 
	 * @param sqlFile
	 * @return
	 */
	public static String execTaskSQLFile(String sqlFile) {
		String errMsg = null;
		try {
			Connection connection = DbJdbc.getConnection();
			SqlScript slqScript = new SqlScript(connection);
			File file = new File(sqlFile);
			slqScript.runScriptFile(file);
			
			File dirDone = new File(file.getParentFile(), DIR_DONE);
			if (!dirDone.exists()) {
				dirDone.mkdir();
			}
			File fileDone = new File(dirDone, file.getName());
			try {
				FileUtils.moveFile(file, fileDone);
			} catch (Exception e1) {
				logger.error("ERROR - moveFile to [" + fileDone.getAbsolutePath() + "]");
				logger.error(e1.getMessage());
			}
//			file.renameTo(file2);
		} catch (Exception e) {
			errMsg = displayStackTrace(e.getStackTrace());
			logger.error("ERROR - [" + sqlFile + "]", e);
		}
		
		return errMsg;
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public static boolean execTaskSql(SysTask task) {
		String errMsg = null;
		SqlScript.execSql(task.getCode());
		SysTaskJdbcDao.updateExecutedTask(task);
		
		return errMsg == null;
	}
		
		
	/**
	 * 
	 * @param stElements
	 * @return
	 */
	private static String displayStackTrace(StackTraceElement[] stElements) {
		String errMsg = "";
		for (int i = 0 ; i < stElements.length; i++) {
			StackTraceElement elt = stElements[i];
			errMsg += elt.toString() + "\r\n";
		}
		return errMsg;
	}
}
