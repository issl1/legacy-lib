package com.nokor.ersys.collab.project.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.ersys.collab.project.dao.ProjectDao;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskWkfHistoryItem;
import com.nokor.ersys.collab.project.service.TaskRestriction;
import com.nokor.ersys.collab.project.service.TaskService;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * Project Service Impl
 * @author bunlong.taing
 *
 */
@Service("taskService")
public class TaskServiceImpl extends MainEntityServiceImpl implements TaskService {

	/**  */
	private static final long serialVersionUID = 4316632697494040962L;
	
	@Autowired
	private ProjectDao dao;
	
	public TaskServiceImpl() {
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public ProjectDao getDao() {
		return dao;
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#createProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		super.createProcess(mainEntity);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#updateProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		super.updateProcess(mainEntity);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#deleteProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}

	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#assign(com.nokor.ersys.collab.project.model.Task, com.nokor.ersys.core.hr.model.organization.Employee)
	 */
	@Override
	public void assign(Task task, Employee assignee) {
		assign(task, assignee, null);
	}

	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#assign(com.nokor.ersys.collab.project.model.Task, com.nokor.ersys.core.hr.model.organization.Employee, java.lang.String)
	 */
	@Override
	public void assign(Task task, Employee assignee, String comment) {
		task.setAssignee(assignee);
		if (StringUtils.isNotEmpty(comment)) {
			task.setComment(comment);
		}
		task.setForcedHistory(true);
		updateProcess(task);
	}

	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#addComment(com.nokor.ersys.collab.project.model.Task, java.lang.String)
	 */
	@Override
	public void addComment(Task task, String comment) {
		task.setComment(comment);
		task.setForcedHistory(true);
		updateProcess(task);
	}
	
	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#listByDesc(java.lang.String)
	 */
	@Override
	public List<Task> listByDesc(String desc) {
		TaskRestriction restrictions = new TaskRestriction();
		restrictions.setText(desc);
		return dao.list(restrictions);
	}

	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#getByCode(java.lang.String)
	 */
	@Override
	public Task getByCode(String code) {
		TaskRestriction restrictions = new TaskRestriction();
		restrictions.setCode(code);
		return dao.getFirst(restrictions);
	}
	
	/**
	 * @param task
	 * @return
	 */
	@Override
	public Date getAssignmentDate(Task task) {
		WkfHistoryItemRestriction<TaskWkfHistoryItem> restrictions = new WkfHistoryItemRestriction<TaskWkfHistoryItem>(TaskWkfHistoryItem.class);
		restrictions.setEntity(task.getMainEntity());
		restrictions.setEntityId(task.getId());
		restrictions.setPropertyName(Task.ASSIGNEE);
		restrictions.addOrder(Order.desc(TaskWkfHistoryItem.CHANGEDATE));
		TaskWkfHistoryItem historyItem = getFirst(restrictions);
		return historyItem == null ? null : historyItem.getChangeDate();
	}

	/**
	 * @see com.nokor.ersys.collab.project.service.TaskService#listComments(com.nokor.ersys.collab.project.model.Task)
	 */
	@Override
	public List<TaskWkfHistoryItem> listComments(Task task) {
		WkfHistoryItemRestriction<TaskWkfHistoryItem> restrictions = new WkfHistoryItemRestriction<TaskWkfHistoryItem>(TaskWkfHistoryItem.class);
		restrictions.setEntity(task.getMainEntity());
		restrictions.setEntityId(task.getId());
		restrictions.setPropertyName(Task.COMMENT);
		restrictions.addOrder(Order.asc(TaskWkfHistoryItem.CHANGEDATE));
		return dao.list(restrictions);
	}
	
}
