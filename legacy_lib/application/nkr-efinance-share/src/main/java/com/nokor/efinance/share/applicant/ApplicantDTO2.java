package com.nokor.efinance.share.applicant;

import java.util.List;

import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * @author ly.youhort
 *
 */
public class ApplicantDTO2 extends AbstractIndividualDTO {
	/** */
	private static final long serialVersionUID = 6949885917223461002L;
	
	private List<AddressDTO> addresses;
	private List<EmploymentDTO> employments;
	
	private List<ContactInfoDTO> contactInfos;
	private List<ReferenceInfoDTO> referenceInfos;
	
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ApplicantDTO2)) {
			 return false;
		 }
		 ApplicantDTO2 applicantDTO2 = (ApplicantDTO2) arg0;
		 return getId() != null && getId().equals(applicantDTO2.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
