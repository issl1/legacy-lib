package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 */
public class InsuranceIncomeSchedule implements Serializable {
	
	private static final long serialVersionUID = 8242865498762546377L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double cumulativeBalance;
	private double insuranceIncomeDistribution2;
	private double insuranceIncomeDistribution3;
	private double unearnedInsuranceIncome;
	private double accountReceivable;
	private double insuranceIncomeReceived;
	
	private double insuranceIncomeRevenueLatePayment;
	
	//adjustment modification
	private double insuranceIncomeInSuspend;
	private double insuranceIncomeInSuspendCumulated;
	private double realInsuranceIncomeDistributed;
	
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
	 * @return the insuranceIncomeDistribution2
	 */
	public double getInsuranceIncomeDistribution2() {
		return insuranceIncomeDistribution2;
	}
	/**
	 * @param insuranceIncomeDistribution2 the insuranceIncomeDistribution2 to set
	 */
	public void setInsuranceIncomeDistribution2(double insuranceIncomeDistribution2) {
		this.insuranceIncomeDistribution2 = insuranceIncomeDistribution2;
	}
	/**
	 * @return the insuranceIncomeDistribution3
	 */
	public double getInsuranceIncomeDistribution3() {
		return insuranceIncomeDistribution3;
	}
	/**
	 * @param insuranceIncomeDistribution3 the insuranceIncomeDistribution3 to set
	 */
	public void setInsuranceIncomeDistribution3(double insuranceIncomeDistribution3) {
		this.insuranceIncomeDistribution3 = insuranceIncomeDistribution3;
	}
	/**
	 * @return the unearnedInsuranceIncome
	 */
	public double getUnearnedInsuranceIncome() {
		return unearnedInsuranceIncome;
	}
	/**
	 * @param unearnedInsuranceIncome the unearnedInsuranceIncome to set
	 */
	public void setUnearnedInsuranceIncome(double unearnedInsuranceIncome) {
		this.unearnedInsuranceIncome = unearnedInsuranceIncome;
	}
	/**
	 * @return the accountReceivable
	 */
	public double getAccountReceivable() {
		return accountReceivable;
	}
	/**
	 * @param accountReceivable the accountReceivable to set
	 */
	public void setAccountReceivable(double accountReceivable) {
		this.accountReceivable = accountReceivable;
	}
	/**
	 * @return the insuranceIncomeReceived
	 */
	public double getInsuranceIncomeReceived() {
		return insuranceIncomeReceived;
	}
	/**
	 * @param insuranceIncomeReceived the insuranceIncomeReceived to set
	 */
	public void setInsuranceIncomeReceived(double insuranceIncomeReceived) {
		this.insuranceIncomeReceived = insuranceIncomeReceived;
	}
	/**
	 * @return the insuranceIncomeInSuspend
	 */
	public double getInsuranceIncomeInSuspend() {
		return insuranceIncomeInSuspend;
	}
	/**
	 * @param insuranceIncomeInSuspend the insuranceIncomeInSuspend to set
	 */
	public void setInsuranceIncomeInSuspend(double insuranceIncomeInSuspend) {
		this.insuranceIncomeInSuspend = insuranceIncomeInSuspend;
	}
	/**
	 * @return the insuranceIncomeInSuspendCumulated
	 */
	public double getInsuranceIncomeInSuspendCumulated() {
		return insuranceIncomeInSuspendCumulated;
	}
	/**
	 * @param insuranceIncomeInSuspendCumulated the insuranceIncomeInSuspendCumulated to set
	 */
	public void setInsuranceIncomeInSuspendCumulated(
			double insuranceIncomeInSuspendCumulated) {
		this.insuranceIncomeInSuspendCumulated = insuranceIncomeInSuspendCumulated;
	}
	/**
	 * @return the realInsuranceIncomeDistributed
	 */
	public double getRealInsuranceIncomeDistributed() {
		return realInsuranceIncomeDistributed;
	}
	/**
	 * @param realInsuranceIncomeDistributed the realInsuranceIncomeDistributed to set
	 */
	public void setRealInsuranceIncomeDistributed(
			double realInsuranceIncomeDistributed) {
		this.realInsuranceIncomeDistributed = realInsuranceIncomeDistributed;
	}
	/**
	 * @return the insuranceIncomeRevenueLatePayment
	 */
	public double getInsuranceIncomeRevenueLatePayment() {
		return insuranceIncomeRevenueLatePayment;
	}
	/**
	 * @param insuranceIncomeRevenueLatePayment the insuranceIncomeRevenueLatePayment to set
	 */
	public void setInsuranceIncomeRevenueLatePayment(
			double insuranceIncomeRevenueLatePayment) {
		this.insuranceIncomeRevenueLatePayment = insuranceIncomeRevenueLatePayment;
	}	
}
