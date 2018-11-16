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

import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * Employee group by VO
 * @author uhout.cheng
 */
public class EmployeeGroupByVO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -6547450581114912077L;

	private static Logger logger = LoggerFactory.getLogger(EmployeeGroupByVO.class);
	
	private static int count = 0;
	
	private Map<String, List<Employee>> groupByEmployees;
	private Map<String, EmployeeGroupByVO> empGroupByVos;

	/**
	 * 
	 * @param employees
	 * @param groupBy
	 * @param groupByCaption
	 * @param level
	 */
	public EmployeeGroupByVO(List<Employee> employees, String[] groupBy, String[] groupByCaption, int level) {
		logger.debug(String.valueOf(count++));
		if (groupBy == null || groupBy.length < 1) { // No Group
			groupByEmployees = new HashMap<String, List<Employee>>();
			groupByEmployees.put("null", employees);
		} else if (level == groupBy.length - 1) { // Leaf Group
			groupByEmployees =  buildGroupByEmployees(employees, groupBy[level], groupByCaption[level]);
		} else { // Branch Group
			empGroupByVos = new HashMap<String, EmployeeGroupByVO>();
			Map<String, List<Employee>> tasksGroup = buildGroupByEmployees(employees, groupBy[level], groupByCaption[level]);
			level++;
			for (String groupKey : tasksGroup.keySet()) {
				empGroupByVos.put(groupKey, new EmployeeGroupByVO(tasksGroup.get(groupKey), groupBy, groupByCaption, level));
			}
		}
	}
	
	/**
	 * 
	 * @param employee
	 * @param fieldName
	 * @param fieldCaption
	 * @return
	 */
	private String getKey(Employee employee, String fieldName, String fieldCaption) {
		PropertyUtilsBean property = new PropertyUtilsBean();
		String key = "No " + fieldCaption;
		try {
			Object obj = property.getNestedProperty(employee, fieldName);
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
			logger.error("Illegal access: " + employee.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (InvocationTargetException e) {
			logger.error("Invocation Target Exception: " + employee.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (NoSuchMethodException e) {
			logger.error("No method: " + fieldName + employee.getClass().getCanonicalName() + "." + fieldName, e);
		} catch (NestedNullException e) {
			return key;
		}
		return key;
	}
	
	/**
	 * 
	 * @param employees
	 * @param key
	 * @param caption
	 * @return
	 */
	private HashMap<String, List<Employee>> buildGroupByEmployees(List<Employee> employees, String key, String caption) {
		HashMap<String, List<Employee>> groupByEmps = new HashMap<String, List<Employee>>();
		if (employees != null) {
			for (Employee employee : employees) {
				String fieldValue = getKey(employee, key, caption);
				if (groupByEmps.get(fieldValue) == null) {
					groupByEmps.put(fieldValue, new ArrayList<Employee>());
				}
				groupByEmps.get(fieldValue).add(employee);
			}
		}
		return groupByEmps;
	}

	/**
	 * @return the empGroupByVos
	 */
	public Map<String, EmployeeGroupByVO> getEmpGroupByVos() {
		return empGroupByVos;
	}

	/**
	 * @return the groupByEmployees
	 */
	public Map<String, List<Employee>> getGroupByEmployees() {
		return groupByEmployees;
	}
}
