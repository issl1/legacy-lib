package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class InsuranceExpense implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	
	private Amount cumulativeBalance = new Amount();
	private Amount insuranceExpenseDistribution2 = new Amount();
	private Amount insuranceExpenseDistribution3 = new Amount();
	private Amount balanceInsuranceExpense = new Amount();
	private Amount insuranceExpensePaid = new Amount();
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the contractStartDate
	 */
	public Date getContractStartDate() {
		return contractStartDate;
	}

	/**
	 * @param contractStartDate the contractStartDate to set
	 */
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	/**
	 * @return the firstInstallmentDate
	 */
	public Date getFirstInstallmentDate() {
		return firstInstallmentDate;
	}

	/**
	 * @param firstInstallmentDate the firstInstallmentDate to set
	 */
	public void setFirstInstallmentDate(Date firstInstallmentDate) {
		this.firstInstallmentDate = firstInstallmentDate;
	}	

	/**
	 * @return the lastNameEn
	 */
	public String getLastNameEn() {
		return lastNameEn;
	}

	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}

	/**
	 * @return the firstNameEn
	 */
	public String getFirstNameEn() {
		return firstNameEn;
	}

	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}
	
	/**
	 * @return the cumulativeBalance
	 */
	public Amount getCumulativeBalance() {
		return cumulativeBalance;
	}

	/**
	 * @param cumulativeBalance the cumulativeBalance to set
	 */
	public void setCumulativeBalance(Amount cumulativeBalance) {
		this.cumulativeBalance = cumulativeBalance;
	}
	
	/**
	 * @return the insuranceExpenseDistribution2
	 */
	public Amount getInsuranceExpenseDistribution2() {
		return insuranceExpenseDistribution2;
	}

	/**
	 * @param insuranceExpenseDistribution2 the insuranceExpenseDistribution2 to set
	 */
	public void setInsuranceExpenseDistribution2(Amount insuranceExpenseDistribution2) {
		this.insuranceExpenseDistribution2 = insuranceExpenseDistribution2;
	}

	/**
	 * @return the insuranceExpenseDistribution3
	 */
	public Amount getInsuranceExpenseDistribution3() {
		return insuranceExpenseDistribution3;
	}

	/**
	 * @param insuranceExpenseDistribution3 the insuranceExpenseDistribution3 to set
	 */
	public void setInsuranceExpenseDistribution3(Amount insuranceExpenseDistribution3) {
		this.insuranceExpenseDistribution3 = insuranceExpenseDistribution3;
	}

	/**
	 * @return the balanceInsuranceExpense
	 */
	public Amount getBalanceInsuranceExpense() {
		return balanceInsuranceExpense;
	}

	/**
	 * @param balanceInsuranceExpense the balanceInsuranceExpense to set
	 */
	public void setBalanceInsuranceExpense(Amount balanceInsuranceExpense) {
		this.balanceInsuranceExpense = balanceInsuranceExpense;
	}

	/**
	 * @return
	 */
	public Amount getRealInsuranceExpenseDistributed() {
		Amount insuranceExpenseDistributed = new Amount();
		insuranceExpenseDistributed.plus(getInsuranceExpenseDistribution2());
		insuranceExpenseDistributed.plus(getInsuranceExpenseDistribution3());
		return insuranceExpenseDistributed;
	}


	/**
	 * @return the insuranceExpensePaid
	 */
	public Amount getInsuranceExpensePaid() {
		return insuranceExpensePaid;
	}

	/**
	 * @param insuranceExpensePaid the insuranceExpensePaid to set
	 */
	public void setInsuranceExpensePaid(Amount insuranceExpensePaid) {
		this.insuranceExpensePaid = insuranceExpensePaid;
	}
}
