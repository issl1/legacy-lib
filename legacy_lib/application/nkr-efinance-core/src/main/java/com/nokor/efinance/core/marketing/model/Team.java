package com.nokor.efinance.core.marketing.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.marketing.service.MTeam;

/**
 * @author youhort.ly
 */
@Entity
@Table(name = "tu_team")
public class Team extends EntityA implements MTeam {
	
	/** */
	private static final long serialVersionUID = -6463998394331460240L;
	
	private String description;
	private List<TeamEmployee> teamEmployees;
	
	/**
     * 
     * @return
     */
    public static Team createInstance() {
    	Team instance = EntityFactory.createInstance(Team.class);
        return instance;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	@Column(name = "tea_description", nullable = true, length=255)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the teamEmployees
	 */
	@OneToMany(mappedBy="employee", fetch = FetchType.LAZY)
	public List<TeamEmployee> getTeamEmployees() {
		return teamEmployees;
	}

	/**
	 * @param teamEmployees the teamEmployees to set
	 */
	public void setTeamEmployees(List<TeamEmployee> teamEmployees) {
		this.teamEmployees = teamEmployees;
	}

}
