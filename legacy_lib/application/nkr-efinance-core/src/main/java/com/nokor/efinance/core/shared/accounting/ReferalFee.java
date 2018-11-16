package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class ReferalFee implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	
	private Amount cumulativeBalance = new Amount();
	private Amount referalFeeDistribution2 = new Amount();
	private Amount referalFeeDistribution3 = new Amount();
	private Amount deferredCommissionReferalFee = new Amount();
	private Amount acrrualExpenses = new Amount();
	private Amount paymentToDealer = new Amount();
	
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
	 * @return the referalFeeDistribution2
	 */
	public Amount getReferalFeeDistribution2() {
		return referalFeeDistribution2;
	}

	/**
	 * @param referalFeeDistribution2 the referalFeeDistribution2 to set
	 */
	public void setReferalFeeDistribution2(Amount referalFeeDistribution2) {
		this.referalFeeDistribution2 = referalFeeDistribution2;
	}

	/**
	 * @return the referalFeeDistribution3
	 */
	public Amount getReferalFeeDistribution3() {
		return referalFeeDistribution3;
	}

	/**
	 * @param referalFeeDistribution3 the referalFeeDistribution3 to set
	 */
	public void setReferalFeeDistribution3(Amount referalFeeDistribution3) {
		this.referalFeeDistribution3 = referalFeeDistribution3;
	}

	/**
	 * @return the deferredCommissionReferalFee
	 */
	public Amount getDeferredCommissionReferalFee() {
		return deferredCommissionReferalFee;
	}

	/**
	 * @param deferredCommissionReferalFee the deferredCommissionReferalFee to set
	 */
	public void setDeferredCommissionReferalFee(Amount deferredCommissionReferalFee) {
		this.deferredCommissionReferalFee = deferredCommissionReferalFee;
	}

	/**
	 * @return the acrrualExpenses
	 */
	public Amount getAcrrualExpenses() {
		return acrrualExpenses;
	}

	/**
	 * @param acrrualExpenses the acrrualExpenses to set
	 */
	public void setAcrrualExpenses(Amount acrrualExpenses) {
		this.acrrualExpenses = acrrualExpenses;
	}

	/**
	 * @return the paymentToDealer
	 */
	public Amount getPaymentToDealer() {
		return paymentToDealer;
	}

	/**
	 * @param paymentToDealer the paymentToDealer to set
	 */
	public void setPaymentToDealer(Amount paymentToDealer) {
		this.paymentToDealer = paymentToDealer;
	}
	
	/**
	 * @return
	 */
	public Amount getRealReferalFeeDistributed() {
		Amount realReferalFeeDistributed = new Amount();
		realReferalFeeDistributed.plus(getReferalFeeDistribution2());
		realReferalFeeDistributed.plus(getReferalFeeDistribution3());
		return realReferalFeeDistributed;
	}



}
