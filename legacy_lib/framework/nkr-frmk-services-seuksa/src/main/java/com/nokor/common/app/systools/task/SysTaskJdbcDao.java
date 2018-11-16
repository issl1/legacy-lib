package com.nokor.common.app.systools.task;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.sql.SqlScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.systools.model.EnSysTaskType;
import com.nokor.common.app.systools.model.EnSysTaskWhere;
import com.nokor.common.app.systools.model.SysTask;

/**
 * 
 * @author prasnar
 *
 */
public class SysTaskJdbcDao {
	private static final Logger logger = LoggerFactory.getLogger(SysTaskJdbcDao.class);

	/**
	 * 
	 * @param where
	 * @return
	 */
	public static List<SysTask> listTasksNoExecuted(EnSysTaskWhere where) {
		List<SysTask> tasks = new ArrayList<>();
		String sql = "select sys_tas_id, sys_tas_code, sys_tas_desc, sys_tas_method, sys_tas_typ_code, sys_tas_where_code "
					+ " from tu_sys_task "
					+ " where sys_tas_where_code = '" + where + "'"
					+ " and sys_tas_executed_date is null"
					+ " order by sort_index, sys_tas_id;";
		try {
			List<Object[]> results = SqlScript.execSql(sql);
			for (Object[] row : results) {
				SysTask task = new SysTask();
				
				task.setId(Long.valueOf((String) row[0]));
				task.setCode((String) row[1]);
				task.setDesc((String) row[2]);
				task.setMethod((String) row[3]);
				if (row[4] != null) {
					task.setType(EnSysTaskType.valueOf((String) row[4]));
				}
				if (row[5] != null) {
					task.setWhere(EnSysTaskWhere.valueOf((String) row[5]));
				}
				
				tasks.add(task);
			}
			
		} catch (Exception e) {
			logger.error("Failed to listTasksNoExecuted [" + sql + "]", e);
		}
		return tasks;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static boolean isTaskExists(String code) {
		String sql = "select count (1) from tu_sys_task where sys_tas_code like '%" + code + "';";
		try {
			List<Object[]> results = SqlScript.execSql(sql);
			Integer nb = Integer.valueOf((String)results.get(0)[0]);
			logger.debug("Nb [" + code + "] found: " + nb);
			return nb > 0;
			
		} catch (Exception e) {
			logger.error("Failed isTaskExists [" + code + "]", e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param code
	 * @param where
	 */
	public static boolean createSqlFileTask(String code, EnSysTaskWhere where) {
		String sql = "insert into tu_sys_task (sys_tas_code, sys_tas_desc, sys_tas_method, sys_tas_typ_code, sys_tas_where_code, sort_index, dt_cre, usr_cre, dt_upd, usr_upd)"
				+ " values ('" + code + "', null, null, '" + EnSysTaskType.SQL_FILE + "', '" + where + "', "
				+			"1, now(), 'systask', now(), 'systask'"
				+ ");";
		
		List<Object[]> results = SqlScript.execSql(sql);
		return results != null;
	}
	
	/**
	 * 
	 * @param task
	 */
	public static void updateExecutedTask(SysTask task) {
		String sql = "update tu_sys_task "
				+ " set sys_tas_executed_date = now() " 
				+ (task.getErrorMsg() != null ? ", sys_tas_error_msg = '" + task.getErrorMsg() + "' " : "")
				+ "	where sys_tas_id = " + task.getId() 
				+ ";";
		
		List<Object[]> results = SqlScript.execSql(sql);
	}
	
}
