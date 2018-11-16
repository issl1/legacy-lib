package com.nokor.efinance.share.applicant;

import java.util.List;

import com.nokor.common.messaging.share.UriDTO;

/**
 * @author ly.youhort
 *
 */
public class IndividualDTO3 extends AbstractIndividualDTO {
	/** */
	private static final long serialVersionUID = 6949885917223461002L;
	
	private List<UriDTO> addresses;
	private List<UriDTO> employments;
	
	private List<ContactInfoDTO> contactInfos;
	private List<UriDTO> referenceInfos;
	
	/**
	 * @return the addresses
	 */
	public List<UriDTO> getAddresses() {
		return addresses;
	}
	
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<UriDTO> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * @return the employments
	 */
	public List<UriDTO> getEmployments() {
		return employments;
	}
	
	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<UriDTO> employments) {
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
	public List<UriDTO> getReferenceInfos() {
		return referenceInfos;
	}

	/**
	 * @param referenceInfos the referenceInfos to set
	 */
	public void setReferenceInfos(List<UriDTO> referenceInfos) {
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
		 if (arg0 == null || !(arg0 instanceof IndividualDTO3)) {
			 return false;
		 }
		 IndividualDTO3 individualDTO3 = (IndividualDTO3) arg0;
		 return getId() != null && getId().equals(individualDTO3.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
