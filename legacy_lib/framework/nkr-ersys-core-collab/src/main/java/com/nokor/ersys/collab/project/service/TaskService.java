package com.nokor.ersys.collab.project.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskWkfHistoryItem;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * Project Service
 * @author bunlong.taing
 *
 */
public interface TaskService extends MainEntityService {
	
	void assign(Task task, Employee assignee);
	
	void assign(Task task, Employee assignee, String comment);
	
	void addComment(Task task, String comment);

	List<Task> listByDesc(String desc);

	Task getByCode(String code);

	Date getAssignmentDate(Task task);
	
	List<TaskWkfHistoryItem> listComments(Task task);

}
