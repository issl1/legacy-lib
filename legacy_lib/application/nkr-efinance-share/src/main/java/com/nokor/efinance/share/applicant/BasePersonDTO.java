package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author ly.youhort
 */
public abstract class BasePersonDTO implements Serializable {
	/** */
	private static final long serialVersionUID = 3469386575982741317L;

	private Long id;
	private String lastName;
	private String lastNameEn;
	private String firstName;
	private String firstNameEn;
	private String nickName;
	private RefDataDTO typeIdNumber;
	private String idNumber;
	private Date issuingIdNumberDate;
	private Date expiringIdNumberDate;
	
	private RefDataDTO prefix;
	private RefDataDTO title;
	private Date birthDate;
	private RefDataDTO gender;
	private RefDataDTO maritalStatus;
	private RefDataDTO nationality;	
	
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
	 * @return the lastNameEn
	 */
	public String getLastNameEn() {
		return lastNameEn;
	}
	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
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
	 * @return the firstNameEn
	 */
	public String getFirstNameEn() {
		return firstNameEn;
	}
	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
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
	 * @return the issuingIdNumberDate
	 */
	public Date getIssuingIdNumberDate() {
		return issuingIdNumberDate;
	}
	/**
	 * @param issuingIdNumberDate the issuingIdNumberDate to set
	 */
	public void setIssuingIdNumberDate(Date issuingIdNumberDate) {
		this.issuingIdNumberDate = issuingIdNumberDate;
	}
	/**
	 * @return the expiringIdNumberDate
	 */
	public Date getExpiringIdNumberDate() {
		return expiringIdNumberDate;
	}
	/**
	 * @param expiringIdNumberDate the expiringIdNumberDate to set
	 */
	public void setExpiringIdNumberDate(Date expiringIdNumberDate) {
		this.expiringIdNumberDate = expiringIdNumberDate;
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
	 * @return the typeIdNumber
	 */
	public RefDataDTO getTypeIdNumber() {
		return typeIdNumber;
	}
	/**
	 * @param typeIdNumber the typeIdNumber to set
	 */
	public void setTypeIdNumber(RefDataDTO typeIdNumber) {
		this.typeIdNumber = typeIdNumber;
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
	 * @return the maritalStatus
	 */
	public RefDataDTO getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(RefDataDTO maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	/**
	 * @return the nationality
	 */
	public RefDataDTO getNationality() {
		return nationality;
	}
	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(RefDataDTO nationality) {
		this.nationality = nationality;
	}	
}
