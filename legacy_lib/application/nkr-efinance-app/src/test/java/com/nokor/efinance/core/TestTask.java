package com.nokor.efinance.core;

import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.service.WkfHistoConfigRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskClassification;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.testing.BaseTestCase;


/**
 * 
 * @author prasnar
 *
 */
public class TestTask extends BaseTestCase implements FinServicesHelper {

	/**
	 * 
	 */
	public TestTask() {
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#isRequiredAuhentication()
	 */
	@Override
	protected boolean isRequiredAuhentication() {
		return false;
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#setAuthentication()
	 */
	@Override
	protected void setAuthentication() {
		login = "admin";
		password = "admin@EFIN";
	}
	
	
	
	/**
	 * 
	 */
	public void testAssignTask() {
		try {
			authenticate();
			String taskCode = "TASK1";
			Task task = TASK_SRV.getByCode(taskCode);
			if (task == null) {
				task = createTask(taskCode, taskCode + "-Desc");
				TASK_SRV.createProcess(task);
			}
			Employee assignee = EMPL_SRV.getById(Employee.class, 1);
			
			task.setComment("HELLO-" + DateUtils.todayFull());
			TASK_SRV.assign(task, assignee);
	//		TASK_SRV.addComment();
			logger.info("************SUCCESS**********");
	    	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	private TaskClassification createClassification(String code, String desc) {
		TaskClassification classification = new TaskClassification();
		classification.setCode("code");
		classification.setDesc("desc");
		classification.setDescEn("descEn");
		ENTITY_SRV.saveOrUpdate(classification);
		return classification;
	}
	
	/**
	 * 
	 */
	public void xxtestRestriction() {
		WkfHistoConfigRestriction restrictions = new WkfHistoConfigRestriction();
		restrictions.setEntity(EMainEntity.getById(9l));
		WKF_SRV.getUnique(restrictions);
		WKF_SRV.getHistoConfig(EMainEntity.getByCode(Task.class.getCanonicalName()));
	}
	
	/**
	 * 
	 * @return
	 */
	private Task createTask(String code, String title) {
		Task task = Task.createInstance();
		task.setCode(code);
		task.setDesc(title);
		task.setDescEn(task.getDesc());
		String classificationCode = "MainClass";
		TaskClassification classification = ENTITY_SRV.getByCode(TaskClassification.class, classificationCode);
		if (classification == null) {
			classification = createClassification(classificationCode, classificationCode + "-Desc");
		}
		task.setClassification(ENTITY_SRV.getById(TaskClassification.class, 1l));
		task.setType(ETaskType.BUG);
		return task;
	}
	
}
