package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;

public class LeaseAdjustment implements Serializable, Entity {

	private static final long serialVersionUID = 1539848300930441694L;
	
	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	private Date changeStatusDate;
	
	private String lastNameEn;
	private String firstNameEn;
	private EWkfStatus contractStatus;
	
	private Amount balanceInterestInSuspend = new Amount();
	private Amount unpaidAccruedInterestReceivable = new Amount();
	private Amount unpaidPrincipalBalance = new Amount();
	private Amount unpaidInterestBalance = new Amount();
	


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
	 * @return the changeStatusDate
	 */
	public Date getChangeStatusDate() {
		return changeStatusDate;
	}

	/**
	 * @param changeStatusDate the changeStatusDate to set
	 */
	public void setChangeStatusDate(Date changeStatusDate) {
		this.changeStatusDate = changeStatusDate;
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
	 * @return the contractStatus
	 */
	public EWkfStatus getWkfStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setWkfStatus(EWkfStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	/**
	 * @return the balanceInterestInSuspend
	 */
	public Amount getBalanceInterestInSuspend() {
		return balanceInterestInSuspend;
	}

	/**
	 * @param balanceInterestInSuspend the balanceInterestInSuspend to set
	 */
	public void setBalanceInterestInSuspend(Amount balanceInterestInSuspend) {
		this.balanceInterestInSuspend = balanceInterestInSuspend;
	}

	/**
	 * @return the unpaidAccruedInterestReceivable
	 */
	public Amount getUnpaidAccruedInterestReceivable() {
		return unpaidAccruedInterestReceivable;
	}

	/**
	 * @param unpaidAccruedInterestReceivable the unpaidAccruedInterestReceivable to set
	 */
	public void setUnpaidAccruedInterestReceivable(
			Amount unpaidAccruedInterestReceivable) {
		this.unpaidAccruedInterestReceivable = unpaidAccruedInterestReceivable;
	}

	/**
	 * @return the unpaidPrincipalBalance
	 */
	public Amount getUnpaidPrincipalBalance() {
		return unpaidPrincipalBalance;
	}

	/**
	 * @param unpaidPrincipalBalance the unpaidPrincipalBalance to set
	 */
	public void setUnpaidPrincipalBalance(Amount unpaidPrincipalBalance) {
		this.unpaidPrincipalBalance = unpaidPrincipalBalance;
	}

	/**
	 * @return the unpaidInterestBalance
	 */
	public Amount getUnpaidInterestBalance() {
		return unpaidInterestBalance;
	}

	/**
	 * @param unpaidInterestBalance the unpaidInterestBalance to set
	 */
	public void setUnpaidInterestBalance(Amount unpaidInterestBalance) {
		this.unpaidInterestBalance = unpaidInterestBalance;
	}

	/**
	 * @return the total balance
	 */
	public Amount getUnpaidTotalBalance() {
		Amount unpaidTotalBalance = new Amount();
		unpaidTotalBalance.plus(unpaidPrincipalBalance);
		unpaidTotalBalance.plus(unpaidInterestBalance);
		return unpaidTotalBalance;
	}
}
