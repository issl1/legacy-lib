package com.nokor.efinance.core.contract.model;

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

import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_contract_profile_queue")
public class ContractProfileQueue extends EntityA {
	
	/** */
	private static final long serialVersionUID = 7558536532147869732L;
	
	private SecProfile profile;
	private Contract contract;
	
	/**
	 * @return
	 */
	public static ContractProfileQueue createInstance() {
		ContractProfileQueue secUserBackup = EntityFactory.createInstance(ContractProfileQueue.class);
        return secUserBackup;
    }
	
	/**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_pro_que_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }	

	/**
	 * @return the profile
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_pro_id")
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
	
}
