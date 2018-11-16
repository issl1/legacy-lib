package com.nokor.efinance.share.applicant;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class CompanyEmployeeDTO extends BasePersonDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -6330159799476801450L;
	
	private RefDataDTO typeContact;
	
	/**
	 * @return the typeContact
	 */
	public RefDataDTO getTypeContact() {
		return typeContact;
	}

	/**
	 * @param typeContact the typeContact to set
	 */
	public void setTypeContact(RefDataDTO typeContact) {
		this.typeContact = typeContact;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CompanyEmployeeDTO)) {
			 return false;
		 }
		 CompanyEmployeeDTO referenceInfoDTO = (CompanyEmployeeDTO) arg0;
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
