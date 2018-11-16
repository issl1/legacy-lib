package com.nokor.efinance.core.applicant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.service.MDriver;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.BasePerson;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_driver")
public class Driver extends BasePerson implements MDriver {

	/** */
	private static final long serialVersionUID = 3248191516126249345L;

	private Contract contract;
	private Address address;
	
	/**
     * 
     * @return
     */
    public static Driver createInstance() {
    	Driver driver = EntityFactory.createInstance(Driver.class);
    	driver.setWkfStatus(EWkfStatus.NEW);
        return driver;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dri_id", unique = true, nullable = false)
	public Long getId() {
		return id;
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
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id")
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	
	/** 
	 * @see com.nokor.common.app.workflow.model.EntityWkf#isWkfEnabled()
	 */
	@Override
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}
}
