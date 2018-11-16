package com.nokor.efinance.core.common.security.model;

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

import com.nokor.frmk.security.model.SecUser;

/**
 *
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_sec_user_debt_level")
public class SecUserDeptLevel extends EntityA implements MSecUserDeptLevel {

	/** */
	private static final long serialVersionUID = -6079389694423317487L;
	
	private SecUser secUser;
	private int debtLevel;

	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_usr_debt_lvl_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the secUser
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
	/**
	 * @return the debtLevel
	 */
	@Column(name = "sec_usr_debt_lvl", nullable = true)
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
