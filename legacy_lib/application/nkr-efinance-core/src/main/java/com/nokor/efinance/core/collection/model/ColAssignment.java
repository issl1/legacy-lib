package com.nokor.efinance.core.collection.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author bunlong.taing
 *
 */
@Entity
@Table(name = "tu_col_assfile")
public class ColAssignment extends EntityA {

	/** */
	private static final long serialVersionUID = 4497571547024622545L;
	
	private SecUser assignee;
	private SecUser assignedBy;
	private boolean defaultAssignee;
	private Long overdueFrom;
	private Long overdueTo;
	private EColTask task;
	private List<EWkfStatus> statuses;
	private List<EColGroup> groups;

	/** */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "col_ass_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the overdueFrom
	 */
	@Column(name = "col_ass_overdue_from", nullable = true)
	public Long getOverdueFrom() {
		return overdueFrom;
	}

	/**
	 * @param overdueFrom the overdueFrom to set
	 */
	public void setOverdueFrom(Long overdueFrom) {
		this.overdueFrom = overdueFrom;
	}

	/**
	 * @return the overdueTo
	 */
	@Column(name = "col_ass_overdue_to", nullable = true)
	public Long getOverdueTo() {
		return overdueTo;
	}

	/**
	 * @param overdueTo the overdueTo to set
	 */
	public void setOverdueTo(Long overdueTo) {
		this.overdueTo = overdueTo;
	}

	/**
	 * @return the task
	 */
    @Column(name = "col_sta_id", nullable = true)
    @Convert(converter = EColTask.class)
	public EColTask getCollectionTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setCollectionTask(EColTask task) {
		this.task = task;
	}

	/**
	 * @return the statuses
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_col_assignment_status",
				joinColumns = { @JoinColumn(name = "col_ass_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "wkf_sta_id") })
	public List<EWkfStatus> getStatuses() {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		return statuses;
	}

	/**
	 * @param statuses the statuses to set
	 */
	public void setStatuses(List<EWkfStatus> statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return the groups
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_col_assignment_group",
				joinColumns = { @JoinColumn(name = "col_ass_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "col_grp_id") })
	public List<EColGroup> getGroups() {
		if (groups == null) {
			groups = new ArrayList<>();
		}
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<EColGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the assignee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_sec_usr_id", nullable = true)
	public SecUser getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(SecUser assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the assignedBy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_sec_usr_id", nullable = false)
	public SecUser getAssignedBy() {
		return assignedBy;
	}

	/**
	 * @param assignedBy the assignedBy to set
	 */
	public void setAssignedBy(SecUser assignedBy) {
		this.assignedBy = assignedBy;
	}

	/**
	 * @return the task
	 */
    @Column(name = "col_tas_id", nullable = true)
    @Convert(converter = EColTask.class)
	public EColTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(EColTask task) {
		this.task = task;
	}

	/**
	 * @return the defaultAssignee
	 */
	@Column(name = "col_ass_bl_default_assignee", nullable = false, columnDefinition = "boolean default false")
	public boolean isDefaultAssignee() {
		return defaultAssignee;
	}

	/**
	 * @param defaultAssignee the defaultAssignee to set
	 */
	public void setDefaultAssignee(boolean defaultAssignee) {
		this.defaultAssignee = defaultAssignee;
	}

}
