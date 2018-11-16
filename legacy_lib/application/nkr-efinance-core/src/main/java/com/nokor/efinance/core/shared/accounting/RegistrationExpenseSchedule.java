package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 */
public class RegistrationExpenseSchedule implements Serializable {
	
	private static final long serialVersionUID = 8242865498762546377L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double cumulativeBalance;
	private double registrationExpenseDistribution2;
	private double registrationExpenseDistribution3;
	private double balanceRegistrationExpense;
	private double registrationPlateNumberFee;
	
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
	 * @return the registrationExpenseDistribution2
	 */
	public double getRegistrationExpenseDistribution2() {
		return registrationExpenseDistribution2;
	}
	/**
	 * @param registrationExpenseDistribution2 the registrationExpenseDistribution2 to set
	 */
	public void setRegistrationExpenseDistribution2(
			double registrationExpenseDistribution2) {
		this.registrationExpenseDistribution2 = registrationExpenseDistribution2;
	}
	/**
	 * @return the registrationExpenseDistribution3
	 */
	public double getRegistrationExpenseDistribution3() {
		return registrationExpenseDistribution3;
	}
	/**
	 * @param registrationExpenseDistribution3 the registrationExpenseDistribution3 to set
	 */
	public void setRegistrationExpenseDistribution3(
			double registrationExpenseDistribution3) {
		this.registrationExpenseDistribution3 = registrationExpenseDistribution3;
	}
	/**
	 * @return the balanceRegistrationExpense
	 */
	public double getBalanceRegistrationExpense() {
		return balanceRegistrationExpense;
	}
	/**
	 * @param balanceRegistrationExpense the balanceRegistrationExpense to set
	 */
	public void setBalanceRegistrationExpense(double balanceRegistrationExpense) {
		this.balanceRegistrationExpense = balanceRegistrationExpense;
	}
	/**
	 * @return the registrationPlateNumberFee
	 */
	public double getRegistrationPlateNumberFee() {
		return registrationPlateNumberFee;
	}
	/**
	 * @param registrationPlateNumberFee the registrationPlateNumberFee to set
	 */
	public void setRegistrationPlateNumberFee(double registrationPlateNumberFee) {
		this.registrationPlateNumberFee = registrationPlateNumberFee;
	}	
	
}
