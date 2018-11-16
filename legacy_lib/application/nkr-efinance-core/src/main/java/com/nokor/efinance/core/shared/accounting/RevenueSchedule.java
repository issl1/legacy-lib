package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 *
 */
public class RevenueSchedule implements Serializable {

	private static final long serialVersionUID = 8399679891906367528L;
	
	private int n;
	private double revenueAmount;
	private double balanceAmount;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;
		
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
	 * @return the revenueAmount
	 */
	public double getRevenueAmount() {
		return revenueAmount;
	}
	/**
	 * @param revenueAmount the revenueAmount to set
	 */
	public void setRevenueAmount(double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}
	/**
	 * @return the balanceAmount
	 */
	public double getBalanceAmount() {
		return balanceAmount;
	}
	/**
	 * @param balanceAmount the balanceAmount to set
	 */
	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
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
}
