package com.nokor.efinance.core.contract.model;

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

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_contract_user_simul_inbox")
public class ContractUserSimulInbox extends EntityA implements MContractUserSimulInbox {

	/**	 
	 */
	private static final long serialVersionUID = 6950368319871841953L;
	
	private SecProfile profile;
	private SecUser secUser;
	private Contract contract;
	private Area area;
	private EColType colType;
	private Integer debtLevel;
	
	/**
	 * @return
	 */
	public static ContractUserSimulInbox createInstance() {
		ContractUserSimulInbox contractUserQueue = EntityFactory.createInstance(ContractUserSimulInbox.class);
        return contractUserQueue;
    }
	
	/**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_usr_sim_inb_id", unique = true, nullable = false)
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
	 * @return the profile
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_pro_id", nullable = true)
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
	}

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the area
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "are_id")
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * @return the colType
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}

	/**
	 * @return the debtLevel
	 */
	@Column(name = "con_usr_sim_inb_nu_debt_level", nullable = true)
	public Integer getDebtLevel() {
		return debtLevel;
	}

	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDebtLevel(Integer debtLevel) {
		this.debtLevel = debtLevel;
	}
}
