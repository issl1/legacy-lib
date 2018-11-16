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
@XmlType(name = "InfoResponseMessage")
public class InfoResponseMessage implements Serializable {

	private static final long serialVersionUID = 7944410016042301788L;
	
	@XmlElement(name = "status", required = true)
	private int status;
	
	@XmlElement(name = "errorMessage", required = false)
	private Message errorMessage;
	
	@XmlElement(name = "serviceHeader", required = true)
	private ServiceHeader serviceHeader;
	@XmlElement(name = "reference", required = true)
	private String reference;
	@XmlElement(name = "tokenId", required = true)
	private String tokenId;
	@XmlElement(name = "applicant", required = true)
	private Applicant applicant;
	@XmlElement(name = "financial", required = true)
	private Financial financial;
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the errorMessage
	 */
	public Message getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(Message errorMessage) {
		this.errorMessage = errorMessage;
	}
	
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
	 * @return the applicant
	 */
	public Applicant getApplicant() {
		return applicant;
	}
	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	/**
	 * @return the financial
	 */
	public Financial getFinancial() {
		return financial;
	}
	/**
	 * @param financial the financial to set
	 */
	public void setFinancial(Financial financial) {
		this.financial = financial;
	}
}
