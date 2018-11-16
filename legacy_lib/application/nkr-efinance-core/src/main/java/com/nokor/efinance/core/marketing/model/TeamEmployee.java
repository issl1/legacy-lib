package com.nokor.efinance.core.marketing.model;

import javax.persistence.Column;
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

import com.nokor.efinance.core.marketing.service.MTeamEmployee;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_team_employee")
public class TeamEmployee extends EntityA implements MTeamEmployee {
		
	/** */
	private static final long serialVersionUID = -4940892236204587036L;
	
	private Team team;
	private Employee employee;
	
	/**
     * 
     * @return
     */
    public static TeamEmployee createInstance() {
    	TeamEmployee instance = EntityFactory.createInstance(TeamEmployee.class);
        return instance;
    }
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_ter_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the team
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tea_id")
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return the employee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id")
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
