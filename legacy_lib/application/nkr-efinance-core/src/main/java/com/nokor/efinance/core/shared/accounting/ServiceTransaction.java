package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

public class ServiceTransaction implements Serializable, Entity {

	private static final long serialVersionUID = 1539848300930441694L;
	
	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	private String serviceDescEn;
			
	private Amount revenue = new Amount();
	private Amount accruedIncome = new Amount();
	private Amount principalRepayment = new Amount();	
	private Amount principalBalance = new Amount();


	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
		
	/**
	 * @return the contractStartDate
	 */
	public Date getContractStartDate() {
		return contractStartDate;
	}

	/**
	 * @param contractStartDate the contractStartDate to set
	 */
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	/**
	 * @return the firstInstallmentDate
	 */
	public Date getFirstInstallmentDate() {
		return firstInstallmentDate;
	}

	/**
	 * @param firstInstallmentDate the firstInstallmentDate to set
	 */
	public void setFirstInstallmentDate(Date firstInstallmentDate) {
		this.firstInstallmentDate = firstInstallmentDate;
	}	
	
	/**
	 * @return the serviceDescEn
	 */
	public String getServiceDescEn() {
		return serviceDescEn;
	}

	/**
	 * @param serviceDescEn the serviceDescEn to set
	 */
	public void setServiceDescEn(String serviceDescEn) {
		this.serviceDescEn = serviceDescEn;
	}

	/**
	 * @return the revenue
	 */
	public Amount getRevenue() {
		return revenue;
	}

	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(Amount revenue) {
		this.revenue = revenue;
	}

	/**
	 * @return the accruedIncome
	 */
	public Amount getAccruedIncome() {
		return accruedIncome;
	}

	/**
	 * @param accruedIncome the accruedIncome to set
	 */
	public void setAccruedIncome(Amount accruedIncome) {
		this.accruedIncome = accruedIncome;
	}

	/**
	 * @return the principalRepayment
	 */
	public Amount getPrincipalRepayment() {
		return principalRepayment;
	}

	/**
	 * @param principalRepayment the principalRepayment to set
	 */
	public void setPrincipalRepayment(Amount principalRepayment) {
		this.principalRepayment = principalRepayment;
	}

	/**
	 * @return the principalBalance
	 */
	public Amount getPrincipalBalance() {
		return principalBalance;
	}

	/**
	 * @param principalBalance the principalBalance to set
	 */
	public void setPrincipalBalance(Amount principalBalance) {
		this.principalBalance = principalBalance;
	}
	
	
}
