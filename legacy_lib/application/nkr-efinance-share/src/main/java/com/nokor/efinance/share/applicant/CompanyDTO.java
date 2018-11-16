package com.nokor.efinance.share.applicant;

import java.util.List;

import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.ersys.messaging.share.organization.BaseOrganizationDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class CompanyDTO extends BaseOrganizationDTO {
	/** */
	private static final long serialVersionUID = -2424621254404544082L;

	private List<CompanyEmployeeDTO> companyEmployees;
	private List<AddressDTO> addresses;
	
	/**
	 * @return the companyEmployees
	 */
	public List<CompanyEmployeeDTO> getCompanyEmployees() {
		return companyEmployees;
	}

	/**
	 * @param companyEmployees the companyEmployees to set
	 */
	public void setCompanyEmployees(List<CompanyEmployeeDTO> companyEmployees) {
		this.companyEmployees = companyEmployees;
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
		 if (arg0 == null || !(arg0 instanceof CompanyDTO)) {
			 return false;
		 }
		 CompanyDTO companyDTO = (CompanyDTO) arg0;
		 return getId() != null && getId().equals(companyDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
