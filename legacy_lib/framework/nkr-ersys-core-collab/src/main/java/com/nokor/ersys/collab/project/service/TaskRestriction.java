package com.nokor.ersys.collab.project.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskClassification;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.MStaff;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author prasnar
 * 
 */
public class TaskRestriction extends BaseRestrictions<Task> {
    /** */
	private static final long serialVersionUID = -7323270945512358330L;
	
	private String code;
    private String text;
    private ETaskType taskType;
    private EWkfStatus status;
    private SecProfile role;
    private EJobPosition jobPosition;
    private Date deadline;
    private Project project;
    private TaskClassification classification;
    private Boolean groupBy;

    /**
	 * @return the role
	 */
	public SecProfile getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(SecProfile role) {
		this.role = role;
	}

	/**
     *
     */
    public TaskRestriction() {
        super(Task.class);
    }

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
//    	addAssociation(TaskComment.class, JoinType.LEFT_OUTER_JOIN);

    	if (StringUtils.isNotEmpty(text)) {
    		addCriterion(Restrictions.or(
    					Restrictions.ilike(Task.CODE, text, MatchMode.ANYWHERE),
    					Restrictions.ilike(Task.DESC, text, MatchMode.ANYWHERE),
    					Restrictions.ilike(Task.DESCEN, text, MatchMode.ANYWHERE)
    						)
    				);
    	}
    	
    	if (taskType != null) {
    		addCriterion(Restrictions.eq(Task.TYPE, taskType));
    	}
    	if (status != null) {
    		addCriterion(Restrictions.eq(Task.WKFSTATUS, status));
    	}
    	if (role != null || jobPosition != null) {
    		addAssociation(Task.ASSIGNEE, "ass", JoinType.INNER_JOIN);
    	}
    	if (role != null) {
    		addAssociation("ass." + MStaff.SECUSER, "sec", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("sec." + SecUser.DEFAULTPROFILE, role));
    	}
    	if (jobPosition != null) {
    		addCriterion(Restrictions.eq("ass." + Employee.JOBPOSITION, jobPosition));
    	}
    	if (deadline != null) {
    		addCriterion(Restrictions.ge(Task.DEADLINE, DateUtils.getDateAtBeginningOfDay(deadline)));
    		addCriterion(Restrictions.le(Task.DEADLINE, DateUtils.getDateAtEndOfDay(deadline)));
    	}
    	if (project != null) {
    		addCriterion(Restrictions.eq(Task.PROJECT, project));
    	}
    	if (classification != null) {
    		addCriterion(Restrictions.eq(Task.CLASSIFICATION, classification));
    	}
    	
    	if (StringUtils.isNotEmpty(code)) {
    		addCriterion(Restrictions.eq(Task.CODE, code));
    	}
    	
    	if (!Boolean.TRUE.equals(groupBy)) {
    		addCriterion(Restrictions.isNull(Task.PARENT));
    	} 	
    }

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the taskType
	 */
	public ETaskType getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(ETaskType taskType) {
		this.taskType = taskType;
	}

	/**
	 * @return the status
	 */
	public EWkfStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EWkfStatus status) {
		this.status = status;
	}

	/**
	 * @return the jobPosition
	 */
	public EJobPosition getJobPosition() {
		return jobPosition;
	}

	/**
	 * @param jobPosition the jobPosition to set
	 */
	public void setJobPosition(EJobPosition jobPosition) {
		this.jobPosition = jobPosition;
	}

	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the classification
	 */
	public TaskClassification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(TaskClassification classification) {
		this.classification = classification;
	}

	/**
	 * @return the groupBy
	 */
	public Boolean getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy the groupBy to set
	 */
	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}
}
