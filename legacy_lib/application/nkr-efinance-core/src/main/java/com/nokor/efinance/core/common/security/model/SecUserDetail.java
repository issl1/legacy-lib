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

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_sec_user_detail")
public class SecUserDetail extends EntityA implements MSecUserDetail {

	private static final long serialVersionUID = -6827859499142608219L;
	
	public static final String NAME = "Session.UserDetail";
	
	private SecUser secUser;
	private Dealer dealer;
	
	private boolean enableAssignContracts;
	
	/**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_usr_det_id", unique = true, nullable = false)
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
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	

	/**
	 * @return the enableAssignContracts
	 */
	@Column(name = "sec_usr_det_enable_assign_contracts", nullable = true, columnDefinition = "boolean default true")
	public boolean isEnableAssignContracts() {
		return enableAssignContracts;
	}

	/**
	 * @param enableAssignContracts the enableAssignContracts to set
	 */
	public void setEnableAssignContracts(boolean enableAssignContracts) {
		this.enableAssignContracts = enableAssignContracts;
	}
}
