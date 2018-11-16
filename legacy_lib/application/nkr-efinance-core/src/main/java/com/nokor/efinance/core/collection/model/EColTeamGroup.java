package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecUser;

/**
 * @author youhort.ly
 */
@Entity
@Table(name = "tu_col_team_group")
public class EColTeamGroup extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = -1689786438480248606L;

	private EColTeam team;
	private EColGroup group;
	private SecUser teamLeader;
	private int deptLevel;
	private String remark;
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_tea_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the team
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_tea_id", nullable = true)
	public EColTeam getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(EColTeam team) {
		this.team = team;
	}

	/**
	 * @return the group
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_grp_id", nullable = true)
	public EColGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(EColGroup group) {
		this.group = group;
	}

	/**
	 * @return the teamLeader
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_leader", nullable = true)
	public SecUser getTeamLeader() {
		return teamLeader;
	}

	/**
	 * @param teamLeader the teamLeader to set
	 */
	public void setTeamLeader(SecUser teamLeader) {
		this.teamLeader = teamLeader;
	}

	/**
	 * @return the deptLevel
	 */
	@Column(name = "col_tea_grp_dept_level", nullable = true)
	public int getDeptLevel() {
		return deptLevel;
	}

	/**
	 * @param deptLevel the deptLevel to set
	 */
	public void setDeptLevel(int deptLevel) {
		this.deptLevel = deptLevel;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "col_tea_grp_remark", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}	
}
