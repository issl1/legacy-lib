package com.nokor.efinance.share.applicant;

import java.util.List;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.ersys.messaging.share.organization.BaseOrganizationDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class CompanyDTO2 extends BaseOrganizationDTO {
	/** */
	private static final long serialVersionUID = -2424621254404544082L;

	private List<UriDTO> addresses;
	
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CompanyDTO2)) {
			 return false;
		 }
		 CompanyDTO2 companyDTO2 = (CompanyDTO2) arg0;
		 return getId() != null && getId().equals(companyDTO2.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
