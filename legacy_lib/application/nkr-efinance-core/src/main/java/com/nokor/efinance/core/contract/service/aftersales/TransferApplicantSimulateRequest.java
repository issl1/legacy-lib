package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.efinance.core.applicant.model.Applicant;

public class TransferApplicantSimulateRequest implements Serializable {

	private static final long serialVersionUID = -5866246138376182931L;
	
	private Long aftEvtId;
	private Long cotraId;
	private Date eventDate;
	private String applicationID;
	private Date applicationDate;
	private Date approvalDate;
	private Applicant applicant;
	private List<Applicant> guarantors;
	
	/**
	 * @return the aftEvtId
	 */
	public Long getAftEvtId() {
		return aftEvtId;
	}

	/**
	 * @param aftEvtId the aftEvtId to set
	 */
	public void setAftEvtId(Long aftEvtId) {
		this.aftEvtId = aftEvtId;
	}

	/**
	 * @return the cotraId
	 */
	public Long getCotraId() {
		return cotraId;
	}
	
	/**
	 * @param cotraId the cotraId to set
	 */
	public void setCotraId(Long cotraId) {
		this.cotraId = cotraId;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * @param applicationID the applicationID to set
	 */
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	/**
	 * @return the applicationDate
	 */
	public Date getApplicationDate() {
		return applicationDate;
	}

	/**
	 * @param applicationDate the applicationDate to set
	 */
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * @return the approvalDate
	 */
	public Date getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
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
	 * @return the guarantors
	 */
	public List<Applicant> getGuarantors() {
		return guarantors;
	}

	/**
	 * @param guarantors the guarantors to set
	 */
	public void setGuarantors(List<Applicant> guarantors) {
		this.guarantors = guarantors;
	}
}
