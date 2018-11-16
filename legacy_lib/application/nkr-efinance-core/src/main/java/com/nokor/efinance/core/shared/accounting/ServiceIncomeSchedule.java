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
public class ServiceIncomeSchedule implements Serializable {
		
	private static final long serialVersionUID = -4360843302841186886L;
	
	private int n;
	private Date installmentDate;
	private Date periodStartDate;
	private Date periodEndDate;
	
	private double revenue;
	private double accruedIncome;			
	private double principalBalance;
	
	private int numInstallment;
	
	private boolean contractInOverdueMoreThanOneMonth;
	
	private List<Cashflow> principalRepaymentCashflows;

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
	 * @return the revenue
	 */
	public double getRevenue() {
		return revenue;
	}

	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	/**
	 * @return the accruedIncome
	 */
	public double getAccruedIncome() {
		return accruedIncome;
	}

	/**
	 * @param accruedIncome the accruedIncome to set
	 */
	public void setAccruedIncome(double accruedIncome) {
		this.accruedIncome = accruedIncome;
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
}
