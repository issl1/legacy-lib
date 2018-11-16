package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

public class LeaseTransaction implements Serializable, Entity {

	private static final long serialVersionUID = 1539848300930441694L;
	
	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	private Double interestRate;
	private Double irrRate;
	
	private Amount interestRevenue = new Amount();
	private Amount interestIncome = new Amount();
	private Amount principalRepayment = new Amount();
	
	private Amount principalBalance = new Amount();
	private Amount interestReceivable = new Amount();
	private Amount unearnedInterestBalance = new Amount();
	private Amount interestInSuspend = new Amount();
	private Amount interestInSuspendCumulated = new Amount();
	private Amount penalty = new Amount();

	
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
	 * @return the lastNameEn
	 */
	public String getLastNameEn() {
		return lastNameEn;
	}

	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}

	/**
	 * @return the firstNameEn
	 */
	public String getFirstNameEn() {
		return firstNameEn;
	}

	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}

	/**
	 * @return the interestRate
	 */
	public Double getInterestRate() {
		return interestRate;
	}

	/**
	 * @param interestRate the interestRate to set
	 */
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * @return the irrRate
	 */
	public Double getIrrRate() {
		return irrRate;
	}

	/**
	 * @param irrRate the irrRate to set
	 */
	public void setIrrRate(Double irrRate) {
		this.irrRate = irrRate;
	}	
	
	/**
	 * @return the interestRevenue
	 */
	public Amount getInterestRevenue() {
		return interestRevenue;
	}

	/**
	 * @param interestRevenue the interestRevenue to set
	 */
	public void setInterestRevenue(Amount interestRevenue) {
		this.interestRevenue = interestRevenue;
	}

	/**
	 * @return the interestIncome
	 */
	public Amount getInterestIncome() {
		return interestIncome;
	}

	/**
	 * @param interestIncome the interestIncome to set
	 */
	public void setInterestIncome(Amount interestIncome) {
		this.interestIncome = interestIncome;
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

	/**
	 * @return the interestReceivable
	 */
	public Amount getInterestReceivable() {
		return interestReceivable;
	}

	/**
	 * @param interestReceivable the interestReceivable to set
	 */
	public void setInterestReceivable(Amount interestReceivable) {
		this.interestReceivable = interestReceivable;
	}

	/**
	 * @return the unearnedInterestBalance
	 */
	public Amount getUnearnedInterestBalance() {
		return unearnedInterestBalance;
	}

	/**
	 * @param unearnedInterestBalance the unearnedInterestBalance to set
	 */
	public void setUnearnedInterestBalance(Amount unearnedInterestBalance) {
		this.unearnedInterestBalance = unearnedInterestBalance;
	}

	/**
	 * @return the total balance
	 */
	public Amount getTotalBalance() {
		Amount revenue = new Amount();
		revenue.plus(principalBalance);
		revenue.plus(unearnedInterestBalance);
		return revenue;
	}

	/**
	 * @return the interestInSuspend
	 */
	public Amount getInterestInSuspend() {
		return interestInSuspend;
	}

	/**
	 * @param interestInSuspend the interestInSuspend to set
	 */
	public void setInterestInSuspend(Amount interestInSuspend) {
		this.interestInSuspend = interestInSuspend;
	}

	/**
	 * @return the interestInSuspendCumulated
	 */
	public Amount getInterestInSuspendCumulated() {
		return interestInSuspendCumulated;
	}

	/**
	 * @param interestInSuspendCumulated the interestInSuspendCumulated to set
	 */
	public void setInterestInSuspendCumulated(Amount interestInSuspendCumulated) {
		this.interestInSuspendCumulated = interestInSuspendCumulated;
	}

	/**
	 * @return the penalty
	 */
	public Amount getPenalty() {
		return penalty;
	}

	/**
	 * @param penalty the penalty to set
	 */
	public void setPenalty(Amount penalty) {
		this.penalty = penalty;
	}	
}
