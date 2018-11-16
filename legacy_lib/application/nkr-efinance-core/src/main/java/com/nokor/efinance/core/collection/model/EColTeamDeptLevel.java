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

import org.seuksa.frmk.model.entity.EntityA;

/**
 *
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_col_team_debt_level")
public class EColTeamDeptLevel extends EntityA {

	/** */
	private static final long serialVersionUID = -6079389694423317487L;
	
	private EColTeam colTeam;
	private int debtLevel;

	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_tea_debt_level_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the colTeam
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_tea_id", nullable = false)
	public EColTeam getColTeam() {
		return colTeam;
	}

	/**
	 * @param colTeam the colTeam to set
	 */
	public void setColTeam(EColTeam colTeam) {
		this.colTeam = colTeam;
	}

	/**
	 * @return the debtLevel
	 */
	@Column(name = "col_tea_debt_level", nullable = true)
	public int getDebtLevel() {
		return debtLevel;
	}

	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDebtLevel(int debtLevel) {
		this.debtLevel = debtLevel;
	}
	
}
