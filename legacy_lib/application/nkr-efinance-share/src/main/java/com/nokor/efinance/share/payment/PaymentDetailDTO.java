package com.nokor.efinance.share.payment;

import java.io.Serializable;
import java.util.List;

import com.nokor.common.messaging.share.UriDTO;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentDetailDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 28489388386475489L;
	
	private List<UriDTO> accountHolders;
	private List<UriDTO> bankAccounts;
	private List<OrgDealerPaymentMethodDTO> paymentMethods;
	
	/**
	 * @return the accountHolders
	 */
	public List<UriDTO> getAccountHolders() {
		return accountHolders;
	}
	
	/**
	 * @param accountHolders the accountHolders to set
	 */
	public void setAccountHolders(List<UriDTO> accountHolders) {
		this.accountHolders = accountHolders;
	}
	
	/**
	 * @return the bankAccounts
	 */
	public List<UriDTO> getBankAccounts() {
		return bankAccounts;
	}
	
	/**
	 * @param bankAccounts the bankAccounts to set
	 */
	public void setBankAccounts(List<UriDTO> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	/**
	 * @return the paymentMethods
	 */
	public List<OrgDealerPaymentMethodDTO> getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods the paymentMethods to set
	 */
	public void setPaymentMethods(List<OrgDealerPaymentMethodDTO> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}
