package com.nokor.finance.services.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 *
 */
public class Schedule implements Serializable {

	private static final long serialVersionUID = 8399679891906367528L;
	
	private int n;
	private double installmentPayment;
	private double interestAmount;
	private double principalAmount;
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
	 * @return the installmentPayment
	 */
	public double getInstallmentPayment() {
		return installmentPayment;
	}
	/**
	 * @param installmentPayment the installmentPayment to set
	 */
	public void setInstallmentPayment(double installmentPayment) {
		this.installmentPayment = installmentPayment;
	}
	/**
	 * @return the interestAmount
	 */
	public double getInterestAmount() {
		return interestAmount;
	}
	/**
	 * @param interestAmount the interestAmount to set
	 */
	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}
	/**
	 * @return the principalAmount
	 */
	public double getPrincipalAmount() {
		return principalAmount;
	}
	/**
	 * @param principalAmount the principalAmount to set
	 */
	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
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
