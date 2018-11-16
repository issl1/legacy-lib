package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly.youhort
 */
public class InsuranceExpenseSchedule implements Serializable {
	
	private static final long serialVersionUID = 8242865498762546377L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double cumulativeBalance;
	private double insuranceExpenseDistribution2;
	private double insuranceExpenseDistribution3;
	private double balanceInsuranceExpense;
	private double insuranceExpensePaid;
	
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
	 * @return the insuranceExpenseDistribution2
	 */
	public double getInsuranceExpenseDistribution2() {
		return insuranceExpenseDistribution2;
	}
	/**
	 * @param insuranceExpenseDistribution2 the insuranceExpenseDistribution2 to set
	 */
	public void setInsuranceExpenseDistribution2(double insuranceExpenseDistribution2) {
		this.insuranceExpenseDistribution2 = insuranceExpenseDistribution2;
	}
	/**
	 * @return the insuranceExpenseDistribution3
	 */
	public double getInsuranceExpenseDistribution3() {
		return insuranceExpenseDistribution3;
	}
	/**
	 * @param insuranceExpenseDistribution3 the insuranceExpenseDistribution3 to set
	 */
	public void setInsuranceExpenseDistribution3(double insuranceExpenseDistribution3) {
		this.insuranceExpenseDistribution3 = insuranceExpenseDistribution3;
	}
	/**
	 * @return the balanceInsuranceExpense
	 */
	public double getBalanceInsuranceExpense() {
		return balanceInsuranceExpense;
	}
	/**
	 * @param balanceInsuranceExpense the balanceInsuranceExpense to set
	 */
	public void setBalanceInsuranceExpense(double balanceInsuranceExpense) {
		this.balanceInsuranceExpense = balanceInsuranceExpense;
	}
	/**
	 * @return the insuranceExpensePaid
	 */
	public double getInsuranceExpensePaid() {
		return insuranceExpensePaid;
	}
	/**
	 * @param insuranceExpensePaid the insuranceExpensePaid to set
	 */
	public void setInsuranceExpensePaid(double insuranceExpensePaid) {
		this.insuranceExpensePaid = insuranceExpensePaid;
	}
}
