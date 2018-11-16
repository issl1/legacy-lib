package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class ServicingIncome implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	
	private Amount cumulativeBalance = new Amount();
	private Amount servicingIncomeDistribution2 = new Amount();
	private Amount servicingIncomeDistribution3 = new Amount();
	private Amount unearnedServicingIncome = new Amount();
	private Amount accountReceivable = new Amount();
	private Amount servicingIncomeReceived = new Amount();
	
	private Amount servicingIncomeInSuspend = new Amount();
	private Amount servicingIncomeInSuspendCumulated = new Amount();
	
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
	 * @return the servicingIncomeDistribution2
	 */
	public Amount getServicingIncomeDistribution2() {
		return servicingIncomeDistribution2;
	}

	/**
	 * @param servicingIncomeDistribution2 the servicingIncomeDistribution2 to set
	 */
	public void setServicingIncomeDistribution2(Amount servicingIncomeDistribution2) {
		this.servicingIncomeDistribution2 = servicingIncomeDistribution2;
	}

	/**
	 * @return the servicingIncomeDistribution3
	 */
	public Amount getServicingIncomeDistribution3() {
		return servicingIncomeDistribution3;
	}

	/**
	 * @param servicingIncomeDistribution3 the servicingIncomeDistribution3 to set
	 */
	public void setServicingIncomeDistribution3(Amount servicingIncomeDistribution3) {
		this.servicingIncomeDistribution3 = servicingIncomeDistribution3;
	}

	/**
	 * @return the unearnedServicingIncome
	 */
	public Amount getUnearnedServicingIncome() {
		return unearnedServicingIncome;
	}

	/**
	 * @param unearnedServicingIncome the unearnedServicingIncome to set
	 */
	public void setUnearnedServicingIncome(Amount unearnedServicingIncome) {
		this.unearnedServicingIncome = unearnedServicingIncome;
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
	public Amount getRealServicingIncomeDistributed() {
		Amount servicingIncomeDistributed = new Amount();
		servicingIncomeDistributed.plus(getServicingIncomeDistribution2());
		servicingIncomeDistributed.plus(getServicingIncomeDistribution3());
		return servicingIncomeDistributed;
	}


	/**
	 * @return the servicingIncomeReceived
	 */
	public Amount getServicingIncomeReceived() {
		return servicingIncomeReceived;
	}

	/**
	 * @param servicingIncomeReceived the servicingIncomeReceived to set
	 */
	public void setServicingIncomeReceived(Amount servicingIncomeReceived) {
		this.servicingIncomeReceived = servicingIncomeReceived;
	}

	/**
	 * @return the servicingIncomeInSuspend
	 */
	public Amount getServicingIncomeInSuspend() {
		return servicingIncomeInSuspend;
	}

	/**
	 * @param servicingIncomeInSuspend the servicingIncomeInSuspend to set
	 */
	public void setServicingIncomeInSuspend(Amount servicingIncomeInSuspend) {
		this.servicingIncomeInSuspend = servicingIncomeInSuspend;
	}

	/**
	 * @return the servicingIncomeInSuspendCumulated
	 */
	public Amount getServicingIncomeInSuspendCumulated() {
		return servicingIncomeInSuspendCumulated;
	}

	/**
	 * @param servicingIncomeInSuspendCumulated the servicingIncomeInSuspendCumulated to set
	 */
	public void setServicingIncomeInSuspendCumulated(
			Amount servicingIncomeInSuspendCumulated) {
		this.servicingIncomeInSuspendCumulated = servicingIncomeInSuspendCumulated;
	}
}
