package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author uhout.cheng
 */
public class LetterDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -3269878246646890106L;
	
	private Long id;
	private String contractNo;
	private RefDataDTO applicantType;
	private UriDTO letterTemplated;
	private AddressDTO address;
	private String userLogin;
	private String message;
	private String status;
	private Date statusDate;
	private Date sendDate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the applicantType
	 */
	public RefDataDTO getApplicantType() {
		return applicantType;
	}

	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(RefDataDTO applicantType) {
		this.applicantType = applicantType;
	}

	/**
	 * @return the letterTemplated
	 */
	public UriDTO getLetterTemplated() {
		return letterTemplated;
	}

	/**
	 * @param letterTemplated the letterTemplated to set
	 */
	public void setLetterTemplated(UriDTO letterTemplated) {
		this.letterTemplated = letterTemplated;
	}

	/**
	 * @return the address
	 */
	public AddressDTO getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	/**
	 * @return the userLogin
	 */
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the statusDate
	 */
	public Date getStatusDate() {
		return statusDate;
	}

	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	/**
	 * @return the sendDate
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LetterDTO)) {
			 return false;
		 }
		 LetterDTO letterDTO = (LetterDTO) arg0;
		 return getId() != null && getId().equals(letterDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
