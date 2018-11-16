package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.efinance.share.applicant.ApplicantDTO;

/**
 * @author youhort.ly
 *
 */
public class TransferApplicantDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 3577541312441170832L;
	
	private String contractID;
	private String applicationID;
	private Date applicationDate;
	private Date approvalDate;
	private Date eventDate;
	private ApplicantDTO lessee;
	private List<ApplicantDTO> guarantors;
	
	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}
	
	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
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
	 * @return the lessee
	 */
	public ApplicantDTO getLessee() {
		return lessee;
	}
	/**
	 * @param lessee the lessee to set
	 */
	public void setLessee(ApplicantDTO lessee) {
		this.lessee = lessee;
	}
	
	/**
	 * @return the guarantors
	 */
	public List<ApplicantDTO> getGuarantors() {
		return guarantors;
	}

	/**
	 * @param guarantors the guarantors to set
	 */
	public void setGuarantors(List<ApplicantDTO> guarantors) {
		this.guarantors = guarantors;
	}

}
