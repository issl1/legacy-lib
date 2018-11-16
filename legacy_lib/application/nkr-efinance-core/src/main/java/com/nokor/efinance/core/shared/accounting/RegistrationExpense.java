package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.Date;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class RegistrationExpense implements Serializable, Entity {

	private static final long serialVersionUID = -7063842802687613607L;

	private Long id;
	private String reference;
	private Date contractStartDate;
	private Date firstInstallmentDate;
	
	private String lastNameEn;
	private String firstNameEn;
	
	private Amount cumulativeBalance = new Amount();
	private Amount registrationExpenseDistribution2 = new Amount();
	private Amount registrationExpenseDistribution3 = new Amount();
	private Amount balanceRegistrationExpense = new Amount();
	
	private Amount registrationPlateNumberFee =  new Amount();
	
	
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
	 * @return the registrationExpenseDistribution2
	 */
	public Amount getRegistrationExpenseDistribution2() {
		return registrationExpenseDistribution2;
	}

	/**
	 * @param registrationExpenseDistribution2 the registrationExpenseDistribution2 to set
	 */
	public void setRegistrationExpenseDistribution2(
			Amount registrationExpenseDistribution2) {
		this.registrationExpenseDistribution2 = registrationExpenseDistribution2;
	}

	/**
	 * @return the registrationExpenseDistribution3
	 */
	public Amount getRegistrationExpenseDistribution3() {
		return registrationExpenseDistribution3;
	}

	/**
	 * @param registrationExpenseDistribution3 the registrationExpenseDistribution3 to set
	 */
	public void setRegistrationExpenseDistribution3(
			Amount registrationExpenseDistribution3) {
		this.registrationExpenseDistribution3 = registrationExpenseDistribution3;
	}

	/**
	 * @return the balanceRegistrationExpense
	 */
	public Amount getBalanceRegistrationExpense() {
		return balanceRegistrationExpense;
	}

	/**
	 * @param balanceRegistrationExpense the balanceRegistrationExpense to set
	 */
	public void setBalanceRegistrationExpense(Amount balanceRegistrationExpense) {
		this.balanceRegistrationExpense = balanceRegistrationExpense;
	}

	/**
	 * @return
	 */
	public Amount getRealRegistrationExpenseDistributed() {
		Amount registrationExpenseDistributed = new Amount();
		registrationExpenseDistributed.plus(getRegistrationExpenseDistribution2());
		registrationExpenseDistributed.plus(getRegistrationExpenseDistribution3());
		return registrationExpenseDistributed;
	}

	/**
	 * @return the registrationPlateNumberFee
	 */
	public Amount getRegistrationPlateNumberFee() {
		return registrationPlateNumberFee;
	}

	/**
	 * @param registrationPlateNumberFee the registrationPlateNumberFee to set
	 */
	public void setRegistrationPlateNumberFee(Amount registrationPlateNumberFee) {
		this.registrationPlateNumberFee = registrationPlateNumberFee;
	}

}
