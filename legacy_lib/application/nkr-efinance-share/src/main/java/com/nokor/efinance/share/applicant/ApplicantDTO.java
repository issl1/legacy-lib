package com.nokor.efinance.share.applicant;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author youhort.ly
 *
 */
public class ApplicantDTO implements Serializable {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Long id;
	private RefDataDTO applicantCategory;
	private ApplicantDataDTO data;
		
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
	 * @return the applicantCategory
	 */
	public RefDataDTO getApplicantCategory() {
		return applicantCategory;
	}
	/**
	 * @param applicantCategory the applicantCategory to set
	 */
	public void setApplicantCategory(RefDataDTO applicantCategory) {
		this.applicantCategory = applicantCategory;
	}
	/**
	 * @return the data
	 */
	public ApplicantDataDTO getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(ApplicantDataDTO data) {
		this.data = data;
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ApplicantDTO)) {
			 return false;
		 }
		 ApplicantDTO applicantDTO = (ApplicantDTO) arg0;
		 return getId() != null && getId().equals(applicantDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
