package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;


/**
 * 
 * @author ly.youhort
 *
 */
public class Enquiry implements Serializable {

	private static final long serialVersionUID = -769526914370907123L;

	public static final String ENQUIRY_REFERENCE = "005_123456789";
		
	private String enquiryType;
	private String language;
	private String productType;
	private int noOfApplicant;
	private String accountType;
	private String enquiryReference;
	private double amount;
	private String currency;
	private Consumer consumer;
	/**
	 * @return the enquiryType
	 */
	public String getEnquiryType() {
		return enquiryType;
	}
	/**
	 * @param enquiryType the enquiryType to set
	 */
	public void setEnquiryType(String enquiryType) {
		this.enquiryType = enquiryType;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the noOfApplicant
	 */
	public int getNoOfApplicant() {
		return noOfApplicant;
	}
	/**
	 * @param noOfApplicant the noOfApplicant to set
	 */
	public void setNoOfApplicant(int noOfApplicant) {
		this.noOfApplicant = noOfApplicant;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the enquiryReference
	 */
	public String getEnquiryReference() {
		return enquiryReference;
	}
	/**
	 * @param enquiryReference the enquiryReference to set
	 */
	public void setEnquiryReference(String enquiryReference) {
		this.enquiryReference = enquiryReference;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the consumer
	 */
	public Consumer getConsumer() {
		return consumer;
	}
	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
}

