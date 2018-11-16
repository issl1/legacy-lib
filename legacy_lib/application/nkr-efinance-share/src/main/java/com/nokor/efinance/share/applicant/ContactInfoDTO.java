package com.nokor.efinance.share.applicant;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author youhort.ly
 */
public class ContactInfoDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -3275002858593481822L;

	private Long id;
	private RefDataDTO typeInfo;
	private RefDataDTO typeAddress;
	private String value;
	private boolean primary;
		
	public ContactInfoDTO() {
		
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
	 * @return the typeInfo
	 */
	public RefDataDTO getTypeInfo() {
		return typeInfo;
	}

	/**
	 * @param typeInfo the typeInfo to set
	 */
	public void setTypeInfo(RefDataDTO typeInfo) {
		this.typeInfo = typeInfo;
	}

	/**
	 * @return the typeAddress
	 */
	public RefDataDTO getTypeAddress() {
		return typeAddress;
	}

	/**
	 * @param typeAddress the typeAddress to set
	 */
	public void setTypeAddress(RefDataDTO typeAddress) {
		this.typeAddress = typeAddress;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ContactInfoDTO)) {
			 return false;
		 }
		 ContactInfoDTO contactInfoDTO = (ContactInfoDTO) arg0;
		 return getId() != null && getId().equals(contactInfoDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
