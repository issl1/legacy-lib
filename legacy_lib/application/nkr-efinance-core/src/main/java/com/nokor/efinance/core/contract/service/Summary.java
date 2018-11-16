package com.nokor.efinance.core.contract.service;

import java.io.Serializable;

import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author youhort.ly
 *
 */
public class Summary implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -364680549221412137L;

	private Amount balanceInstallment;
	private Amount balanceOthers;
	private Amount dueInstallmentToDate;
	private Amount dueInstallmentIn30Days;
	private Amount balanceDueToDate;
	/**
	 * @return the balanceInstallment
	 */
	public Amount getBalanceInstallment() {
		return balanceInstallment;
	}
	/**
	 * @param balanceInstallment the balanceInstallment to set
	 */
	public void setBalanceInstallment(Amount balanceInstallment) {
		this.balanceInstallment = balanceInstallment;
	}
	/**
	 * @return the balanceOthers
	 */
	public Amount getBalanceOthers() {
		return balanceOthers;
	}
	/**
	 * @param balanceOthers the balanceOthers to set
	 */
	public void setBalanceOthers(Amount balanceOthers) {
		this.balanceOthers = balanceOthers;
	}
	/**
	 * @return the dueInstallmentToDate
	 */
	public Amount getDueInstallmentToDate() {
		return dueInstallmentToDate;
	}
	/**
	 * @param dueInstallmentToDate the dueInstallmentToDate to set
	 */
	public void setDueInstallmentToDate(Amount dueInstallmentToDate) {
		this.dueInstallmentToDate = dueInstallmentToDate;
	}
	/**
	 * @return the dueInstallmentIn30Days
	 */
	public Amount getDueInstallmentIn30Days() {
		return dueInstallmentIn30Days;
	}
	/**
	 * @param dueInstallmentIn30Days the dueInstallmentIn30Days to set
	 */
	public void setDueInstallmentIn30Days(Amount dueInstallmentIn30Days) {
		this.dueInstallmentIn30Days = dueInstallmentIn30Days;
	}
	/**
	 * @return the balanceDueToDate
	 */
	public Amount getBalanceDueToDate() {
		return balanceDueToDate;
	}
	/**
	 * @param balanceDueToDate the balanceDueToDate to set
	 */
	public void setBalanceDueToDate(Amount balanceDueToDate) {
		this.balanceDueToDate = balanceDueToDate;
	}
}
