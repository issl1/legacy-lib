package com.nokor.ersys.core.finance.model;

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

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_bank")
public class Bank extends BaseOrganization implements MBank {
	/** */
	private static final long serialVersionUID = 2242816099023817867L;

	private String agencyCode;
	private String swift;
	private Address address;
	private String comment;
	

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
     * create BankInfo's Instance
     * @return
     */
    public static Bank createInstance() {
    	Bank bankInfo = EntityFactory.createInstance(Bank.class);
    	bankInfo.setTypeOrganization(ETypeOrganization.AGENT);
    	bankInfo.setAddress(Address.createInstance());
		bankInfo.setCascadeAtCreation();
		return bankInfo;
	}
    
    /**
     * Set Cascade At Creation
     */
	public void setCascadeAtCreation() {
		addCascadeAtCreation(address);
	}


	/**
	 * @return the agencyCode
	 */
	@Column(name = "ban_agency_code", nullable = true, length = 50)
	public String getAgencyCode() {
		return agencyCode;
	}

	/**
	 * @param agencyCode the agencyCode to set
	 */
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	/**
	 * @return the swift
	 */
	@Column(name = "ban_swift", nullable = true, length = 50)
	public String getSwift() {
		return swift;
	}

	/**
	 * @param swift the swift to set
	 */
	public void setSwift(String swift) {
		this.swift = swift;
	}

	/**
	 * @return the address
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "add_id", nullable = true)
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
	 * @return the comment
	 */
	@Column(name = "ban_comment", nullable = true, length = 255)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


}
