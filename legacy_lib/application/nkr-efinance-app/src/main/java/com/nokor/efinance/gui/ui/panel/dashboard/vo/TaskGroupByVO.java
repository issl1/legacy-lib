package com.nokor.efinance.gui.ui.panel.dashboard.vo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.ersys.collab.project.model.Task;

/**
 * Task Group By VO
 * @author bunlong.taing
 */
public class TaskGroupByVO implements Serializable {
	/** */
	private static final long serialVersionUID = -8093728963360245232L;
	
	private static Logger logger = LoggerFactory.getLogger(TaskGroupByVO.class);
	
	private static int count = 0;
	
	private Map<String, List<Task>> groupByTasks;
	private Map<String, TaskGroupByVO> taskGroupByVos;
	
	/**
	 * @param tasks
	 * @param groupBy
	 * @param level
	 */
	public TaskGroupByVO(List<Task> tasks, String[] groupBy, String[] groupByCaption, int level) {
		logger.debug(String.valueOf(count++));
		if (groupBy == null || groupBy.length < 1) { // No Group
			groupByTasks = new HashMap<String, List<Task>>();
			groupByTasks.put("null", tasks);
		} else if (level == groupBy.length - 1) { // Leaf Group
			groupByTasks =  buildGroupByTasks(tasks, groupBy[level], groupByCaption[level]);
		} else { // Branch Group
			taskGroupByVos = new HashMap<String, TaskGroupByVO>();
			Map<String, List<Task>> tasksGroup = buildGroupByTasks(tasks, groupBy[level], groupByCaption[level]);
			level++;
			for (String groupKey : tasksGroup.keySet()) {
				taskGroupByVos.put(groupKey, new TaskGroupByVO(tasksGroup.get(groupKey), groupBy, groupByCaption, level));
			}
		}
	}
	
	/**
	 * 
	 * @param task
	 * @param fieldName
	 * @return
	 */
	private String getKey(Task task, String fieldName, String fieldCaption) {
		PropertyUtilsBean property = new PropertyUtilsBean();
		String key = "No " + fieldCaption;
		try {
			Object obj = property.getNestedProperty(task, fieldName);
			if (obj == null) {
	    		return key;
	    	}
	    	if (obj instanceof EntityA) {
	    		return String.valueOf(((EntityA) obj).getId());
	    	}
	    	if (obj instanceof RefDataId) {
	    		return String.valueOf(((RefDataId) obj).getId());
	    	}
	    	if (obj instanceof Date) {
	    		return DateUtils.getDateLabel((Date) obj, DateUtils.FORMAT_DDMMYYYY_SLASH);
	    	}
	        return String.valueOf(obj);
		} catch (IllegalAccessException e) {
			logger.error("Illegal access: " + task.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (InvocationTargetException e) {
			logger.error("Invocation Target Exception: " + task.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (NoSuchMethodException e) {
			logger.error("No method: " + fieldName + task.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (NestedNullException e) {
			return key;
		}
		return key;
	}
	
	/**
	 * Build Group By Tasks
	 * @param tasks
	 */
	private HashMap<String, List<Task>> buildGroupByTasks(List<Task> tasks, String key, String caption) {
		HashMap<String, List<Task>> groupByTasks = new HashMap<String, List<Task>>();
		if (tasks != null) {
			for (Task task : tasks) {
				String fieldValue = getKey(task, key, caption);
				if (groupByTasks.get(fieldValue) == null) {
					groupByTasks.put(fieldValue, new ArrayList<Task>());
				}
				groupByTasks.get(fieldValue).add(task);
			}
		}
		return groupByTasks;
	}
	
	/**
	 * @return
	 */
	public Map<String, TaskGroupByVO> getTaskGroupByVOs() {
		return taskGroupByVos;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, List<Task>> getGroupByTasks() {
		return groupByTasks;
	}

}
