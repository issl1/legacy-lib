package com.nokor.ersys.collab.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_project_assignee")
public class ProjectAssignee extends EntityA implements MProjectAssignee {
	/** */
	private static final long serialVersionUID = -3339954926587542483L;

	private Employee employee;
	private Project project;
	private Date startDate;
	private Date endDate;
	private EProjectRole role;
	
	/**
	 * 
	 * @return
	 */
	public static ProjectAssignee createInstance() {
		ProjectAssignee empAssignment = EntityFactory.createInstance(ProjectAssignee.class);
        return empAssignment;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_ass_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @return the employee
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="emp_id", nullable = false)
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "emp_ass_start_date", nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "emp_ass_end_date", nullable = false)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the project
	 */
	/**
	 * @return the assignment
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="prj_id", nullable = false)
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
	 * @return the role
	 */
    @Column(name = "prj_rol_id", nullable = false)
    @Convert(converter = EProjectRole.class)
	public EProjectRole getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(EProjectRole role) {
		this.role = role;
	}
	
}
