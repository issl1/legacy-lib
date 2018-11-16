package com.nokor.efinance.share.applicant;

import java.util.List;

import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * @author ly.youhort
 *
 */
public class IndividualDTO extends AbstractIndividualDTO {
	/** */
	private static final long serialVersionUID = 6949885917223461002L;
	
	private List<AddressDTO> addresses;
	private List<EmploymentDTO> employments;
	
	private List<ContactInfoDTO> contactInfos;
	private List<ReferenceInfoDTO> referenceInfos;
	private List<IndividualSpouseDTO> spouseInfos;
	
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
	 * @return the contactInfos
	 */
	public List<ContactInfoDTO> getContactInfos() {
		return contactInfos;
	}
	
	/**
	 * @param contactInfos the contactInfos to set
	 */
	public void setContactInfos(List<ContactInfoDTO> contactInfos) {
		this.contactInfos = contactInfos;
	}
	
	/**
	 * @return the referenceInfos
	 */
	public List<ReferenceInfoDTO> getReferenceInfos() {
		return referenceInfos;
	}

	/**
	 * @param referenceInfos the referenceInfos to set
	 */
	public void setReferenceInfos(List<ReferenceInfoDTO> referenceInfos) {
		this.referenceInfos = referenceInfos;
	}

	/**
	 * @return the spouseInfos
	 */
	public List<IndividualSpouseDTO> getSpouseInfos() {
		return spouseInfos;
	}

	/**
	 * @param spouseInfos the spouseInfos to set
	 */
	public void setSpouseInfos(List<IndividualSpouseDTO> spouseInfos) {
		this.spouseInfos = spouseInfos;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof IndividualDTO)) {
			 return false;
		 }
		 IndividualDTO individualDTO = (IndividualDTO) arg0;
		 return getId() != -1 && getId().equals(individualDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
