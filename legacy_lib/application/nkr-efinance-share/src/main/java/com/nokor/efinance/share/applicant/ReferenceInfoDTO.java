package com.nokor.efinance.share.applicant;

import java.io.Serializable;
import java.util.List;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author youhort.ly
 *
 */
public class ReferenceInfoDTO implements Serializable {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Long id;
	private RefDataDTO referenceType;
	private RefDataDTO relationship;
	private String lastNameEn;
	private String firstNameEn;
	private List<ContactInfoDTO> contactInfos;
	private String remark;
	
	public ReferenceInfoDTO() {
		
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
	 * @return the referenceType
	 */
	public RefDataDTO getReferenceType() {
		return referenceType;
	}

	/**
	 * @param referenceTypeId the referenceType to set
	 */
	public void setReferenceType(RefDataDTO referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * @return the relationship
	 */
	public RefDataDTO getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(RefDataDTO relationship) {
		this.relationship = relationship;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ReferenceInfoDTO)) {
			 return false;
		 }
		 ReferenceInfoDTO referenceInfoDTO = (ReferenceInfoDTO) arg0;
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
