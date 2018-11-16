package com.nokor.ersys.messaging.share.organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author uhout.cheng
 */
public class OrganizationDTO extends BaseOrganizationDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -5560927379610468426L;

	private UriDTO parentOrganization;
	private UriDTO subTypeOrganization;
	private List<UriDTO> branches;
	private List<AddressDTO> addresses;

	/**
	 * @return the parentOrganization
	 */
	public UriDTO getParentOrganization() {
		return parentOrganization;
	}

	/**
	 * @param parentOrganization the parentOrganization to set
	 */
	public void setParentOrganization(UriDTO parentOrganization) {
		this.parentOrganization = parentOrganization;
	}

	/**
	 * @return the subTypeOrganization
	 */
	public UriDTO getSubTypeOrganization() {
		return subTypeOrganization;
	}

	/**
	 * @param subTypeOrganization the subTypeOrganization to set
	 */
	public void setSubTypeOrganization(UriDTO subTypeOrganization) {
		this.subTypeOrganization = subTypeOrganization;
	}

	
	/**
	 * @return the parentOrganizationId
	 */
	public Long getParentOrganizationId() {
		return parentOrganization != null ? parentOrganization.getId() : null;
	}

	/**
	 * @param parentOrganizationId the parentOrganizationId to set
	 */
	public void setParentOrganizationId(Long parentOrganizationId) {
		this.parentOrganization = new UriDTO(parentOrganizationId, null);
	}

	/**
	 * @return the subTypeOrganizationId
	 */
	public Long getSubTypeOrganizationId() {
		return subTypeOrganization != null ? subTypeOrganization.getId() : null;
	}

	/**
	 * @param subTypeOrganizationId the subTypeOrganizationId to set
	 */
	public void setSubTypeOrganizationId(Long subTypeOrganizationId) {
		this.subTypeOrganization = new UriDTO(subTypeOrganizationId, null);
	}

	
	
	/**
	 * @return the branches
	 */
	public List<UriDTO> getBranches() {
		if (branches != null) {
			branches = new ArrayList<UriDTO>();
		}
		return branches;
	}

	/**
	 * @param branches the branches to set
	 */
	public void setBranches(List<UriDTO> branches) {
		this.branches = branches;
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
		 if (arg0 == null || !(arg0 instanceof OrganizationDTO)) {
			 return false;
		 }
		 OrganizationDTO organizationDTO = (OrganizationDTO) arg0;
		 return getId() != null && getId().equals(organizationDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
