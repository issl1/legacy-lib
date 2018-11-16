package com.nokor.efinance.core.marketing.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.marketing.model.TeamEmployee;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * @author uhout.cheng
 */
public class TeamEmployeeRestriction extends BaseRestrictions<TeamEmployee> {

	/** */
	private static final long serialVersionUID = 3182742561720021586L;
	
	private Long teamId;
	private Long empId;

	/**
	 */
	public TeamEmployeeRestriction() {
		super(TeamEmployee.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (teamId != null) {
			addCriterion(Restrictions.eq(TeamEmployee.TEAM + DOT + Team.ID, teamId));
		}
		if (empId != null) {
			addCriterion(Restrictions.eq(TeamEmployee.EMPLOYEE + DOT + Employee.ID, empId));
		}
	}

	/**
	 * @return the teamId
	 */
	public Long getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	/**
	 * @return the empId
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

}
