package com.nokor.ersys.messaging.share.organization;

import java.util.Date;

import com.nokor.common.messaging.share.BaseEntityDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author uhout.cheng
 */
public class EmployeeDTO extends BaseEntityDTO {

	/** */
	private static final long serialVersionUID = 7779350228623168686L;
	

	private Long id;
	private String lastName;
	private String lastNameEn;
	private String firstName;
	private String firstNameEn;
	private Long jopPositionId;
	private String referance;
	private Long civilityId;
	private Long genderId;
	private Long nationalityId;
	private Date birthDate;
	private String email;
	private String mobile;
	private AddressDTO addressDTO;

	/**
	 * 
	 */
	public EmployeeDTO() {
		
	}

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
	 * @return the jopPositionId
	 */
	public Long getJopPositionId() {
		return jopPositionId;
	}

	/**
	 * @param jopPositionId the jopPositionId to set
	 */
	public void setJopPositionId(Long jopPositionId) {
		this.jopPositionId = jopPositionId;
	}

	/**
	 * @return the referance
	 */
	public String getReferance() {
		return referance;
	}

	/**
	 * @param referance the referance to set
	 */
	public void setReferance(String referance) {
		this.referance = referance;
	}

	/**
	 * @return the civilityId
	 */
	public Long getCivilityId() {
		return civilityId;
	}

	/**
	 * @param civilityId the civilityId to set
	 */
	public void setCivilityId(Long civilityId) {
		this.civilityId = civilityId;
	}

	/**
	 * @return the genderId
	 */
	public Long getGenderId() {
		return genderId;
	}

	/**
	 * @param genderId the genderId to set
	 */
	public void setGenderId(Long genderId) {
		this.genderId = genderId;
	}

	/**
	 * @return the nationalityId
	 */
	public Long getNationalityId() {
		return nationalityId;
	}

	/**
	 * @param nationalityId the nationalityId to set
	 */
	public void setNationalityId(Long nationalityId) {
		this.nationalityId = nationalityId;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the addressDTO
	 */
	public AddressDTO getAddressDTO() {
		return addressDTO;
	}

	/**
	 * @param addressDTO the addressDTO to set
	 */
	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}
}
