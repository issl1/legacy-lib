package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.List;

import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class ApplicantDataDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 2826886551670476271L;
	
	private Long id;
	private String name;
	private String nameEn;
	private String phoneNumber;
	private List<EmploymentDTO> employments;
	private List<AddressDTO> addresses;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
	
	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}
	
	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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
	 * @return the employments
	 */
	public List<EmploymentDTO> getEmployments() {
		return employments;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<EmploymentDTO> employments) {
		this.employments = employments;
	}

	/**
	 * @return the addresses
	 */
	public List<AddressDTO> getAddresses() {
		return addresses;
	}
	
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ApplicantDataDTO)) {
			 return false;
		 }
		 ApplicantDataDTO applicantDataDTO = (ApplicantDataDTO) arg0;
		 return getId() != null && getId().equals(applicantDataDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
