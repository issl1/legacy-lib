package com.nokor.efinance.third.wing.server.payment.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ly.youhort
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfirmRequestMessage")
public class ConfirmRequestMessage implements Serializable {

	private static final long serialVersionUID = 4635232835719639748L;
	
	@XmlElement(name = "serviceHeader", required = true)
	private ServiceHeader serviceHeader;
	
	@XmlElement(name = "reference", required = true)
	private String reference;
	
	@XmlElement(name = "tokenId", required = true)
	private String tokenId;
	
	@XmlElement(name = "paidAmount", required = true)
	private double paidAmount;
	
	@XmlElement(name = "externalPaymentReference", required = false)
	private String externalPaymentReference;
			
	/**
	 * @return the serviceHeader
	 */
	public ServiceHeader getServiceHeader() {
		return serviceHeader;
	}
	/**
	 * @param serviceHeader the serviceHeader to set
	 */
	public void setServiceHeader(ServiceHeader serviceHeader) {
		this.serviceHeader = serviceHeader;
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
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}
	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	/**
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return paidAmount;
	}
	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	/**
	 * @return the externalPaymentReference
	 */
	public String getExternalPaymentReference() {
		return externalPaymentReference;
	}
	/**
	 * @param externalPaymentReference the externalPaymentReference to set
	 */
	public void setExternalPaymentReference(String externalPaymentReference) {
		this.externalPaymentReference = externalPaymentReference;
	}
}
