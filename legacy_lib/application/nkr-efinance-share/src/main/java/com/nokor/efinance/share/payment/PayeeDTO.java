package com.nokor.efinance.share.payment;

import java.io.Serializable;

import com.nokor.efinance.share.bank.BankAccountDTO;

public class PayeeDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -5369691315176077425L;
	
	private String payeeName;
	private double amount;
	private BankAccountDTO bankAccount;
	private String paymentMethod;
	private String requester;
	
	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}
	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the bankAccount
	 */
	public BankAccountDTO getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(BankAccountDTO bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}
	/**
	 * @param requester the requester to set
	 */
	public void setRequester(String requester) {
		this.requester = requester;
	}	
}
