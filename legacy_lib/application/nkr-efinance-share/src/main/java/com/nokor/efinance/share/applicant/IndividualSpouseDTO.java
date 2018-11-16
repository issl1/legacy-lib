package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class IndividualSpouseDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 503480566382360525L;

	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String nickName;
	private Date birthDate;
	private RefDataDTO prefix;
	private RefDataDTO title;
	private RefDataDTO gender;
	
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the prefix
	 */
	public RefDataDTO getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(RefDataDTO prefix) {
		this.prefix = prefix;
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
	 * @return the title
	 */
	public RefDataDTO getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(RefDataDTO title) {
		this.title = title;
	}

	/**
	 * @return the gender
	 */
	public RefDataDTO getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(RefDataDTO gender) {
		this.gender = gender;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof IndividualSpouseDTO)) {
			 return false;
		 }
		 IndividualSpouseDTO referenceInfoDTO = (IndividualSpouseDTO) arg0;
		 return getId() != null && getId().equals(referenceInfoDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
