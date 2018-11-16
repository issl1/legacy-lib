package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 */
public class ReferalFeeSchedule implements Serializable {
	
	private static final long serialVersionUID = 8242865498762546377L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double cumulativeBalance;
	private double referalFeeDistribution2;
	private double referalFeeDistribution3;
	private double deferredCommissionReferalFee;
	private double acrrualExpenses;
	private double paymentToDealer;
	
	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}
	/**
	 * @param n the n to set
	 */
	public void setN(int n) {
		this.n = n;
	}
	/**
	 * @return the installmentDate
	 */
	public Date getInstallmentDate() {
		return installmentDate;
	}
	/**
	 * @param installmentDate the installmentDate to set
	 */
	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}
	/**
	 * @return the periodStartDate
	 */
	public Date getPeriodStartDate() {
		return periodStartDate;
	}
	/**
	 * @param periodStartDate the periodStartDate to set
	 */
	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}
	/**
	 * @return the periodEndDate
	 */
	public Date getPeriodEndDate() {
		return periodEndDate;
	}
	/**
	 * @param periodEndDate the periodEndDate to set
	 */
	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}
	/**
	 * @return the cumulativeBalance
	 */
	public double getCumulativeBalance() {
		return cumulativeBalance;
	}
	/**
	 * @param cumulativeBalance the cumulativeBalance to set
	 */
	public void setCumulativeBalance(double cumulativeBalance) {
		this.cumulativeBalance = cumulativeBalance;
	}
	/**
	 * @return the referalFeeDistribution2
	 */
	public double getReferalFeeDistribution2() {
		return referalFeeDistribution2;
	}
	/**
	 * @param referalFeeDistribution2 the referalFeeDistribution2 to set
	 */
	public void setReferalFeeDistribution2(double referalFeeDistribution2) {
		this.referalFeeDistribution2 = referalFeeDistribution2;
	}
	/**
	 * @return the referalFeeDistribution3
	 */
	public double getReferalFeeDistribution3() {
		return referalFeeDistribution3;
	}
	/**
	 * @param referalFeeDistribution3 the referalFeeDistribution3 to set
	 */
	public void setReferalFeeDistribution3(double referalFeeDistribution3) {
		this.referalFeeDistribution3 = referalFeeDistribution3;
	}
	/**
	 * @return the deferredCommissionReferalFee
	 */
	public double getDeferredCommissionReferalFee() {
		return deferredCommissionReferalFee;
	}
	/**
	 * @param deferredCommissionReferalFee the deferredCommissionReferalFee to set
	 */
	public void setDeferredCommissionReferalFee(double deferredCommissionReferalFee) {
		this.deferredCommissionReferalFee = deferredCommissionReferalFee;
	}
	/**
	 * @return the acrrualExpenses
	 */
	public double getAcrrualExpenses() {
		return acrrualExpenses;
	}
	/**
	 * @param acrrualExpenses the acrrualExpenses to set
	 */
	public void setAcrrualExpenses(double acrrualExpenses) {
		this.acrrualExpenses = acrrualExpenses;
	}
	/**
	 * @return the paymentToDealer
	 */
	public double getPaymentToDealer() {
		return paymentToDealer;
	}
	/**
	 * @param paymentToDealer the paymentToDealer to set
	 */
	public void setPaymentToDealer(double paymentToDealer) {
		this.paymentToDealer = paymentToDealer;
	}
	
}
