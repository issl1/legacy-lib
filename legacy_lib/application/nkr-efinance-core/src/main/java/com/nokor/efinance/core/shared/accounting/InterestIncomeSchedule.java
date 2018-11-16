package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;

/**
 * 
 * @author meng
 *
 */
public class InterestIncomeSchedule implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;

	private double interestRevenue;
	private double interestRevenueLatePayment;
	private double interestIncomeReceivable;
	private double balanceUnearedIncome;
	
	private double interestInSuspend;
	private double interestInSuspendCumulated;
	
	private double totalBalance;
	private double principalBalance;
	
	private int numInstallment;
	
	private boolean contractInOverdueMoreThanOneMonth;
	
	private List<Cashflow> interestRepaymentCashflows;
	private List<Cashflow> principalRepaymentCashflows;
	private List<Cashflow> penaltyCashflows;

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
	 * @return the interestRevenue
	 */
	public double getInterestRevenue() {
		return interestRevenue;
	}

	/**
	 * @param interestRevenue the interestRevenue to set
	 */
	public void setInterestRevenue(double interestRevenue) {
		this.interestRevenue = interestRevenue;
	}

	/**
	 * @return the interestIncomeReceivable
	 */
	public double getInterestIncomeReceivable() {
		return interestIncomeReceivable;
	}

	/**
	 * @param interestIncomeReceivable the interestIncomeReceivable to set
	 */
	public void setInterestIncomeReceivable(double interestIncomeReceivable) {
		this.interestIncomeReceivable = interestIncomeReceivable;
	}

	/**
	 * @return the principalRepayment
	 */
	public double getPenalty() {
		double penalty = 0d;
		if (penaltyCashflows != null) {
			for (Cashflow cashflow : penaltyCashflows) {
				penalty += cashflow.getTiInstallmentAmount();
			}
		}
		return penalty;
	}
	
	/**
	 * @return the principalRepayment
	 */
	public double getPrincipalRepayment() {
		double principalRepayment = 0d;
		if (principalRepaymentCashflows != null) {
			for (Cashflow cashflow : principalRepaymentCashflows) {
				principalRepayment += cashflow.getTiInstallmentAmount();
			}
		}
		return principalRepayment;
	}

	/**
	 * @return the interestIncomeRepayment
	 */
	public double getInterestIncomeRepayment() {
		double interestIncomeRepayment = 0d;
		if (interestRepaymentCashflows != null) {
			for (Cashflow cashflow : interestRepaymentCashflows) {
				interestIncomeRepayment += cashflow.getTiInstallmentAmount();
			}
		}
		return interestIncomeRepayment;		
	}

	/**
	 * @return the balanceUnearedIncome
	 */
	public double getBalanceUnearedIncome() {
		return balanceUnearedIncome;
	}

	/**
	 * @param balanceUnearedIncome the balanceUnearedIncome to set
	 */
	public void setBalanceUnearedIncome(double balanceUnearedIncome) {
		this.balanceUnearedIncome = balanceUnearedIncome;
	}

	/**
	 * @return the totalBalance
	 */
	public double getTotalBalance() {
		return totalBalance;
	}

	/**
	 * @param totalBalance the totalBalance to set
	 */
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	/**
	 * @return the interestInSuspend
	 */
	public double getInterestInSuspend() {
		return interestInSuspend;
	}

	/**
	 * @param interestInSuspend the interestInSuspend to set
	 */
	public void setInterestInSuspend(double interestInSuspend) {
		this.interestInSuspend = interestInSuspend;
	}

	/**
	 * @return the interestInSuspendCumulated
	 */
	public double getInterestInSuspendCumulated() {
		return interestInSuspendCumulated;
	}

	/**
	 * @param interestInSuspendCumulated the interestInSuspendCumulated to set
	 */
	public void setInterestInSuspendCumulated(double interestInSuspendCumulated) {
		this.interestInSuspendCumulated = interestInSuspendCumulated;
	}

	/**
	 * @return the principalBalance
	 */
	public double getPrincipalBalance() {
		return principalBalance;
	}

	/**
	 * @param principalBalance the principalBalance to set
	 */
	public void setPrincipalBalance(double principalBalance) {
		this.principalBalance = principalBalance;
	}

	/**
	 * @return the numInstallment
	 */
	public int getNumInstallment() {
		return numInstallment;
	}

	/**
	 * @param numInstallment the numInstallment to set
	 */
	public void setNumInstallment(int numInstallment) {
		this.numInstallment = numInstallment;
	}

	/**
	 * @return the interestRevenueLatePayment
	 */
	public double getInterestRevenueLatePayment() {
		return interestRevenueLatePayment;
	}

	/**
	 * @param interestRevenueLatePayment the interestRevenueLatePayment to set
	 */
	public void setInterestRevenueLatePayment(double interestRevenueLatePayment) {
		this.interestRevenueLatePayment = interestRevenueLatePayment;
	}
	
	/**
	 * @return the contractInOverdueMoreThanOneMonth
	 */
	public boolean isContractInOverdueMoreThanOneMonth() {
		return contractInOverdueMoreThanOneMonth;
	}

	/**
	 * @param contractInOverdueMoreThanOneMonth the contractInOverdueMoreThanOneMonth to set
	 */
	public void setContractInOverdueMoreThanOneMonth(
			boolean contractInOverdueMoreThanOneMonth) {
		this.contractInOverdueMoreThanOneMonth = contractInOverdueMoreThanOneMonth;
	}

	/**
	 * @return the interestRepaymentCashflows
	 */
	public List<Cashflow> getInterestRepaymentCashflows() {
		return interestRepaymentCashflows;
	}

	/**
	 * @param interestRepaymentCashflows the interestRepaymentCashflows to set
	 */
	public void setInterestRepaymentCashflows(
			List<Cashflow> interestRepaymentCashflows) {
		this.interestRepaymentCashflows = interestRepaymentCashflows;
	}

	/**
	 * @return the principalRepaymentCashflows
	 */
	public List<Cashflow> getPrincipalRepaymentCashflows() {
		return principalRepaymentCashflows;
	}

	/**
	 * @param principalRepaymentCashflows the principalRepaymentCashflows to set
	 */
	public void setPrincipalRepaymentCashflows(
			List<Cashflow> principalRepaymentCashflows) {
		this.principalRepaymentCashflows = principalRepaymentCashflows;
	}

	/**
	 * @return the penaltyCashflows
	 */
	public List<Cashflow> getPenaltyCashflows() {
		return penaltyCashflows;
	}

	/**
	 * @param penaltyCashflows the penaltyCashflows to set
	 */
	public void setPenaltyCashflows(List<Cashflow> penaltyCashflows) {
		this.penaltyCashflows = penaltyCashflows;
	}
}
