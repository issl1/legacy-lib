package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class InsuranceIncome implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	
	private Amount cumulativeBalance = new Amount();
	private Amount insuranceIncomeDistribution2 = new Amount();
	private Amount insuranceIncomeDistribution3 = new Amount();
	private Amount unearnedInsuranceIncome = new Amount();
	private Amount accountReceivable = new Amount();
	private Amount insuranceIncomeReceived = new Amount();
	
	private Amount insuranceIncomeInSuspend = new Amount();
	private Amount insuranceIncomeInSuspendCumulated = new Amount();
	
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
	 * @return the cumulativeBalance
	 */
	public Amount getCumulativeBalance() {
		return cumulativeBalance;
	}

	/**
	 * @param cumulativeBalance the cumulativeBalance to set
	 */
	public void setCumulativeBalance(Amount cumulativeBalance) {
		this.cumulativeBalance = cumulativeBalance;
	}
	
	/**
	 * @return the insuranceIncomeDistribution2
	 */
	public Amount getInsuranceIncomeDistribution2() {
		return insuranceIncomeDistribution2;
	}

	/**
	 * @param insuranceIncomeDistribution2 the insuranceIncomeDistribution2 to set
	 */
	public void setInsuranceIncomeDistribution2(Amount insuranceIncomeDistribution2) {
		this.insuranceIncomeDistribution2 = insuranceIncomeDistribution2;
	}

	/**
	 * @return the insuranceIncomeDistribution3
	 */
	public Amount getInsuranceIncomeDistribution3() {
		return insuranceIncomeDistribution3;
	}

	/**
	 * @param insuranceIncomeDistribution3 the insuranceIncomeDistribution3 to set
	 */
	public void setInsuranceIncomeDistribution3(Amount insuranceIncomeDistribution3) {
		this.insuranceIncomeDistribution3 = insuranceIncomeDistribution3;
	}

	/**
	 * @return the unearnedInsuranceIncome
	 */
	public Amount getUnearnedInsuranceIncome() {
		return unearnedInsuranceIncome;
	}

	/**
	 * @param unearnedInsuranceIncome the unearnedInsuranceIncome to set
	 */
	public void setUnearnedInsuranceIncome(Amount unearnedInsuranceIncome) {
		this.unearnedInsuranceIncome = unearnedInsuranceIncome;
	}

	/**
	 * @return the accountReceivable
	 */
	public Amount getAccountReceivable() {
		return accountReceivable;
	}

	/**
	 * @param accountReceivable the accountReceivable to set
	 */
	public void setAccountReceivable(Amount accountReceivable) {
		this.accountReceivable = accountReceivable;
	}

	/**
	 * @return
	 */
	public Amount getRealInsuranceIncomeDistributed() {
		Amount insuranceIncomeDistributed = new Amount();
		insuranceIncomeDistributed.plus(getInsuranceIncomeDistribution2());
		insuranceIncomeDistributed.plus(getInsuranceIncomeDistribution3());
		return insuranceIncomeDistributed;
	}


	/**
	 * @return the insuranceIncomeReceived
	 */
	public Amount getInsuranceIncomeReceived() {
		return insuranceIncomeReceived;
	}

	/**
	 * @param insuranceIncomeReceived the insuranceIncomeReceived to set
	 */
	public void setInsuranceIncomeReceived(Amount insuranceIncomeReceived) {
		this.insuranceIncomeReceived = insuranceIncomeReceived;
	}

	/**
	 * @return the insuranceIncomeInSuspend
	 */
	public Amount getInsuranceIncomeInSuspend() {
		return insuranceIncomeInSuspend;
	}

	/**
	 * @param insuranceIncomeInSuspend the insuranceIncomeInSuspend to set
	 */
	public void setInsuranceIncomeInSuspend(Amount insuranceIncomeInSuspend) {
		this.insuranceIncomeInSuspend = insuranceIncomeInSuspend;
	}

	/**
	 * @return the insuranceIncomeInSuspendCumulated
	 */
	public Amount getInsuranceIncomeInSuspendCumulated() {
		return insuranceIncomeInSuspendCumulated;
	}

	/**
	 * @param insuranceIncomeInSuspendCumulated the insuranceIncomeInSuspendCumulated to set
	 */
	public void setInsuranceIncomeInSuspendCumulated(
			Amount insuranceIncomeInSuspendCumulated) {
		this.insuranceIncomeInSuspendCumulated = insuranceIncomeInSuspendCumulated;
	}
}
