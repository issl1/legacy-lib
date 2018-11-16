package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;
/**
 * Debt level table in collection IT ADMIN
 * @author uhout.cheng
 */
@Entity
@Table(name = "tu_col_debt_level")
public class ColDebtLevel extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 244322750221767581L;
	
	private int deptLevel;
	private String dueDate;
	private String assignmentDate;
	private String assignmentHour;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_dep_level_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the debtLevel
	 */
    @Column(name = "col_dep_level", nullable = true)
	public int getDeptLevel() {
		return deptLevel;
	}

	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDeptLevel(int deptLevel) {
		this.deptLevel = deptLevel;
	}

	/**
	 * @return the dueDate
	 */
	@Column(name = "col_dep_level_due_date", nullable = true, length = 10)
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the assignmentDate
	 */
	@Column(name = "col_dep_level_assignment_date", nullable = true, length = 10)
	public String getAssignmentDate() {
		return assignmentDate;
	}

	/**
	 * @param assignmentDate the assignmentDate to set
	 */
	public void setAssignmentDate(String assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	/**
	 * @return the assignmentHour
	 */
	@Column(name = "col_dep_level_assignment_hour", nullable = true, length = 10)
	public String getAssignmentHour() {
		return assignmentHour;
	}

	/**
	 * @param assignmentHour the assignmentHour to set
	 */
	public void setAssignmentHour(String assignmentHour) {
		this.assignmentHour = assignmentHour;
	}
}
