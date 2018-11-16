package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author uhout.cheng
 */
public class CashflowDTO implements Serializable {
	
	/**
	 */
	private static final long serialVersionUID = -3805179292649035944L;

	private String payeeFirstName;
	private String payeeFirstNameEn;
	private String payeeLastName;
	private String payeeLastNameEn;
	private String payeeReference;	
	private String contractNo;
	private Double tiAmount;
	private double vat;
	private Date installmentDate;
	private boolean applyPenalty;
	private String eventCode;
	private PaymentType paymentType;
	private List<String> callBackUrls;
	
	/**
	 * @return the payeeFirstName
	 */
	public String getPayeeFirstName() {
		return payeeFirstName;
	}
	/**
	 * @param payeeFirstName the payeeFirstName to set
	 */
	public void setPayeeFirstName(String payeeFirstName) {
		this.payeeFirstName = payeeFirstName;
	}
	/**
	 * @return the payeeFirstNameEn
	 */
	public String getPayeeFirstNameEn() {
		return payeeFirstNameEn;
	}
	/**
	 * @param payeeFirstNameEn the payeeFirstNameEn to set
	 */
	public void setPayeeFirstNameEn(String payeeFirstNameEn) {
		this.payeeFirstNameEn = payeeFirstNameEn;
	}
	/**
	 * @return the payeeLastName
	 */
	public String getPayeeLastName() {
		return payeeLastName;
	}
	/**
	 * @param payeeLastName the payeeLastName to set
	 */
	public void setPayeeLastName(String payeeLastName) {
		this.payeeLastName = payeeLastName;
	}
	/**
	 * @return the payeeLastNameEn
	 */
	public String getPayeeLastNameEn() {
		return payeeLastNameEn;
	}
	/**
	 * @param payeeLastNameEn the payeeLastNameEn to set
	 */
	public void setPayeeLastNameEn(String payeeLastNameEn) {
		this.payeeLastNameEn = payeeLastNameEn;
	}
	/**
	 * @return the payeeReference
	 */
	public String getPayeeReference() {
		return payeeReference;
	}
	/**
	 * @param payeeReference the payeeReference to set
	 */
	public void setPayeeReference(String payeeReference) {
		this.payeeReference = payeeReference;
	}
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * @return the tiAmount
	 */
	public Double getTiAmount() {
		return tiAmount;
	}
	/**
	 * @param tiAmount the tiAmount to set
	 */
	public void setTiAmount(Double tiAmount) {
		this.tiAmount = tiAmount;
	}
	/**
	 * @return the vat
	 */
	public double getVat() {
		return vat;
	}
	/**
	 * @param vat the vat to set
	 */
	public void setVat(double vat) {
		this.vat = vat;
	}
	/**
	 * @return the applyPenalty
	 */
	public boolean isApplyPenalty() {
		return applyPenalty;
	}
	/**
	 * @param applyPenalty the applyPenalty to set
	 */
	public void setApplyPenalty(boolean applyPenalty) {
		this.applyPenalty = applyPenalty;
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
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}
	/**
	 * @param eventCode the eventCode to set
	 */
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the callBackUrls
	 */
	public List<String> getCallBackUrls() {
		return callBackUrls;
	}
	/**
	 * @param callBackUrls the callBackUrls to set
	 */
	public void setCallBackUrls(List<String> callBackUrls) {
		this.callBackUrls = callBackUrls;
	}
}
