package com.nokor.efinance.share.payment;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class OrgDealerPaymentMethodDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 5656417997903636063L;
	
	private UriDTO paymentMethod;
	private RefDataDTO paymentFlowType;
	private UriDTO accountHolder;
	private UriDTO bankAccount;
	
	/**
	 * @return the paymentMethod
	 */
	public UriDTO getPaymentMethod() {
		return paymentMethod;
	}
	
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(UriDTO paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	/**
	 * @return the paymentFlowType
	 */
	public RefDataDTO getPaymentFlowType() {
		return paymentFlowType;
	}
	
	/**
	 * @param paymentFlowType the paymentFlowType to set
	 */
	public void setPaymentFlowType(RefDataDTO paymentFlowType) {
		this.paymentFlowType = paymentFlowType;
	}

	/**
	 * @return the accountHolder
	 */
	public UriDTO getAccountHolder() {
		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(UriDTO accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 * @return the bankAccount
	 */
	public UriDTO getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(UriDTO bankAccount) {
		this.bankAccount = bankAccount;
	}
	
}
