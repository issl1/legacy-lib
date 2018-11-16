package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;


/**
 * @author sun.vanndy
 *
 */
public class RspReport implements Serializable{

	private static final long serialVersionUID = -4456661407636830283L;
	
	private String enquiryType;
	private String reportDate;
	private String enquiryNo;
	private String productType;
	private Integer noOfApplicants;
	private String accountType;
	private String enquiryReference;
	private Double amount;
	private String currency;
	private Consumer consumer;
	private Disclaimer disclaimer;
	
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
	 * @return the reportDate
	 */
	public String getReportDate() {
		return reportDate;
	}
	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * @return the enquiryNo
	 */
	public String getEnquiryNo() {
		return enquiryNo;
	}
	/**
	 * @param enquiryNo the enquiryNo to set
	 */
	public void setEnquiryNo(String enquiryNo) {
		this.enquiryNo = enquiryNo;
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
	 * @return the noOfApplicants
	 */
	public Integer getNoOfApplicants() {
		return noOfApplicants;
	}
	/**
	 * @param noOfApplicants the noOfApplicants to set
	 */
	public void setNoOfApplicants(Integer noOfApplicants) {
		this.noOfApplicants = noOfApplicants;
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
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
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
	/**
	 * @return the disclaimer
	 */
	public Disclaimer getDisclaimer() {
		return disclaimer;
	}
	/**
	 * @param disclaimer the disclaimer to set
	 */
	public void setDisclaimer(Disclaimer disclaimer) {
		this.disclaimer = disclaimer;
	}
}
