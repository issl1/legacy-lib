package com.nokor.efinance.share.blacklist;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;


/**
 * 
 * @author uhout.cheng
 */
public class BlackListItemDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -2230923405035953559L;

	private Long id;
	private UriDTO typeIdNumber;
	private String idNumber;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private String phoneNumber;
	private Date issuingDate;
	private Date expiringDate;
	
	private UriDTO source;
	private UriDTO reason;
	
	private UriDTO civility;
	private UriDTO gender;
	private UriDTO nationality;
	private UriDTO maritalStatus;
	private UriDTO applicantCateogry;
	
	private String details;
	private String remarks;

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
	 * @return the typeIdNumber
	 */
	public UriDTO getTypeIdNumber() {
		return typeIdNumber;
	}

	/**
	 * @param typeIdNumber the typeIdNumber to set
	 */
	public void setTypeIdNumber(UriDTO typeIdNumber) {
		this.typeIdNumber = typeIdNumber;
	}

	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the source
	 */
	public UriDTO getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(UriDTO source) {
		this.source = source;
	}

	/**
	 * @return the reason
	 */
	public UriDTO getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(UriDTO reason) {
		this.reason = reason;
	}

	/**
	 * @return the civility
	 */
	public UriDTO getCivility() {
		return civility;
	}

	/**
	 * @param civility the civility to set
	 */
	public void setCivility(UriDTO civility) {
		this.civility = civility;
	}

	/**
	 * @return the gender
	 */
	public UriDTO getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(UriDTO gender) {
		this.gender = gender;
	}

	/**
	 * @return the issuingDate
	 */
	public Date getIssuingDate() {
		return issuingDate;
	}

	/**
	 * @param issuingDate the issuingDate to set
	 */
	public void setIssuingDate(Date issuingDate) {
		this.issuingDate = issuingDate;
	}

	/**
	 * @return the expiringDate
	 */
	public Date getExpiringDate() {
		return expiringDate;
	}

	/**
	 * @param expiringDate the expiringDate to set
	 */
	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	/**
	 * @return the nationality
	 */
	public UriDTO getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(UriDTO nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the maritalStatus
	 */
	public UriDTO getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(UriDTO maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the applicantCateogry
	 */
	public UriDTO getApplicantCateogry() {
		return applicantCateogry;
	}

	/**
	 * @param applicantCateogry the applicantCateogry to set
	 */
	public void setApplicantCateogry(UriDTO applicantCateogry) {
		this.applicantCateogry = applicantCateogry;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof BlackListItemDTO)) {
			 return false;
		 }
		 BlackListItemDTO blackListItemDTO = (BlackListItemDTO) arg0;
		 return getId() != null && getId().equals(blackListItemDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
