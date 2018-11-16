package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * @author ly.youhort
 */
public class InsuranceIncomeAdjustment implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	private EWkfStatus contractStatus;
	
	private Amount balanceInsuranceIncomeInSuspend = new Amount();
	private Amount unpaidUnearnedInsuranceIncome = new Amount();
	private Amount unpaidAccrualReceivable = new Amount();

	
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
	 * @return the balanceInsuranceIncomeInSuspend
	 */
	public Amount getBalanceInsuranceIncomeInSuspend() {
		return balanceInsuranceIncomeInSuspend;
	}

	/**
	 * @param balanceInsuranceIncomeInSuspend the balanceInsuranceIncomeInSuspend to set
	 */
	public void setBalanceInsuranceIncomeInSuspend(
			Amount balanceInsuranceIncomeInSuspend) {
		this.balanceInsuranceIncomeInSuspend = balanceInsuranceIncomeInSuspend;
	}

	/**
	 * @return the unpaidUnearnedInsuranceIncome
	 */
	public Amount getUnpaidUnearnedInsuranceIncome() {
		return unpaidUnearnedInsuranceIncome;
	}

	/**
	 * @param unpaidUnearnedInsuranceIncome the unpaidUnearnedInsuranceIncome to set
	 */
	public void setUnpaidUnearnedInsuranceIncome(
			Amount unpaidUnearnedInsuranceIncome) {
		this.unpaidUnearnedInsuranceIncome = unpaidUnearnedInsuranceIncome;
	}

	/**
	 * @return the unpaidAccrualReceivable
	 */
	public Amount getUnpaidAccrualReceivable() {
		return unpaidAccrualReceivable;
	}

	/**
	 * @param unpaidAccrualReceivable the unpaidAccrualReceivable to set
	 */
	public void setUnpaidAccrualReceivable(Amount unpaidAccrualReceivable) {
		this.unpaidAccrualReceivable = unpaidAccrualReceivable;
	}

}
