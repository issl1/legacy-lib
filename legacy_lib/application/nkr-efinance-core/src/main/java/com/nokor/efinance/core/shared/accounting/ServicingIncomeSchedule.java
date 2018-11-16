package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 */
public class ServicingIncomeSchedule implements Serializable {
	
	private static final long serialVersionUID = 8242865498762546377L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double cumulativeBalance;
	private double servicingIncomeDistribution2;
	private double servicingIncomeDistribution3;
	private double unearnedServicingIncome;
	private double accountReceivable;
	private double servicingIncomeReceived;
	
	//adjustment modification
	private double servicingIncomeInSuspend;
	private double servicingIncomeInSuspendCumulated;
	private double realServicingIncomeDistributed;
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
	 * @return the servicingIncomeDistribution2
	 */
	public double getServicingIncomeDistribution2() {
		return servicingIncomeDistribution2;
	}
	/**
	 * @param servicingIncomeDistribution2 the servicingIncomeDistribution2 to set
	 */
	public void setServicingIncomeDistribution2(double servicingIncomeDistribution2) {
		this.servicingIncomeDistribution2 = servicingIncomeDistribution2;
	}
	/**
	 * @return the servicingIncomeDistribution3
	 */
	public double getServicingIncomeDistribution3() {
		return servicingIncomeDistribution3;
	}
	/**
	 * @param servicingIncomeDistribution3 the servicingIncomeDistribution3 to set
	 */
	public void setServicingIncomeDistribution3(double servicingIncomeDistribution3) {
		this.servicingIncomeDistribution3 = servicingIncomeDistribution3;
	}
	/**
	 * @return the unearnedServicingIncome
	 */
	public double getUnearnedServicingIncome() {
		return unearnedServicingIncome;
	}
	/**
	 * @param unearnedServicingIncome the unearnedServicingIncome to set
	 */
	public void setUnearnedServicingIncome(double unearnedServicingIncome) {
		this.unearnedServicingIncome = unearnedServicingIncome;
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
	 * @return the servicingIncomeReceived
	 */
	public double getServicingIncomeReceived() {
		return servicingIncomeReceived;
	}
	/**
	 * @param servicingIncomeReceived the servicingIncomeReceived to set
	 */
	public void setServicingIncomeReceived(double servicingIncomeReceived) {
		this.servicingIncomeReceived = servicingIncomeReceived;
	}
	/**
	 * @return the servicingIncomeInSuspend
	 */
	public double getServicingIncomeInSuspend() {
		return servicingIncomeInSuspend;
	}
	/**
	 * @param servicingIncomeInSuspend the servicingIncomeInSuspend to set
	 */
	public void setServicingIncomeInSuspend(double servicingIncomeInSuspend) {
		this.servicingIncomeInSuspend = servicingIncomeInSuspend;
	}
	/**
	 * @return the servicingIncomeInSuspendCumulated
	 */
	public double getServicingIncomeInSuspendCumulated() {
		return servicingIncomeInSuspendCumulated;
	}
	/**
	 * @param servicingIncomeInSuspendCumulated the servicingIncomeInSuspendCumulated to set
	 */
	public void setServicingIncomeInSuspendCumulated(
			double servicingIncomeInSuspendCumulated) {
		this.servicingIncomeInSuspendCumulated = servicingIncomeInSuspendCumulated;
	}
	/**
	 * @return the realServicingIncomeDistributed
	 */
	public double getRealServicingIncomeDistributed() {
		return realServicingIncomeDistributed;
	}
	/**
	 * @param realServicingIncomeDistributed the realServicingIncomeDistributed to set
	 */
	public void setRealServicingIncomeDistributed(
			double realServicingIncomeDistributed) {
		this.realServicingIncomeDistributed = realServicingIncomeDistributed;
	}
}
