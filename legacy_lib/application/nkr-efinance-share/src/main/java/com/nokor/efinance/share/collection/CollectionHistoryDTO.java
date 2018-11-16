package com.nokor.efinance.share.collection;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionHistoryDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -3008644209899905741L;
	
	private Long id;
	private String creationUser;
	private Date creationDate;
	private String contractNo;
	private RefDataDTO callType;
	private RefDataDTO applicantType;
	private RefDataDTO contactInfoType;
	private RefDataDTO contactPerson;
	private String comment;
	private String otherValue;
	private String contactInfoValue;
	private boolean callIn;
	private UriDTO colResult;
	private UriDTO colSubject;
	private AddressDTO address;

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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationUser
	 */
	public String getCreationUser() {
		return creationUser;
	}

	/**
	 * @param creationUser the creationUser to set
	 */
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
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
	 * @return the callType
	 */
	public RefDataDTO getCallType() {
		return callType;
	}

	/**
	 * @param callType the callType to set
	 */
	public void setCallType(RefDataDTO callType) {
		this.callType = callType;
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
	 * @return the contactInfoType
	 */
	public RefDataDTO getContactInfoType() {
		return contactInfoType;
	}

	/**
	 * @param contactInfoType the contactInfoType to set
	 */
	public void setContactInfoType(RefDataDTO contactInfoType) {
		this.contactInfoType = contactInfoType;
	}

	/**
	 * @return the contactPerson
	 */
	public RefDataDTO getContactPerson() {
		return contactPerson;
	}

	/**
	 * @param contactPerson the contactPerson to set
	 */
	public void setContactPerson(RefDataDTO contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the otherValue
	 */
	public String getOtherValue() {
		return otherValue;
	}

	/**
	 * @param otherValue the otherValue to set
	 */
	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
	}

	/**
	 * @return the contactInfoValue
	 */
	public String getContactInfoValue() {
		return contactInfoValue;
	}

	/**
	 * @param contactInfoValue the contactInfoValue to set
	 */
	public void setContactInfoValue(String contactInfoValue) {
		this.contactInfoValue = contactInfoValue;
	}

	/**
	 * @return the callIn
	 */
	public boolean isCallIn() {
		return callIn;
	}

	/**
	 * @param callIn the callIn to set
	 */
	public void setCallIn(boolean callIn) {
		this.callIn = callIn;
	}

	/**
	 * @return the colResult
	 */
	public UriDTO getColResult() {
		return colResult;
	}

	/**
	 * @param colResult the colResult to set
	 */
	public void setColResult(UriDTO colResult) {
		this.colResult = colResult;
	}

	/**
	 * @return the colSubject
	 */
	public UriDTO getColSubject() {
		return colSubject;
	}

	/**
	 * @param colSubject the colSubject to set
	 */
	public void setColSubject(UriDTO colSubject) {
		this.colSubject = colSubject;
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CollectionHistoryDTO)) {
			 return false;
		 }
		 CollectionHistoryDTO collectionHistoryDTO = (CollectionHistoryDTO) arg0;
		 return getId() != null && getId().equals(collectionHistoryDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
