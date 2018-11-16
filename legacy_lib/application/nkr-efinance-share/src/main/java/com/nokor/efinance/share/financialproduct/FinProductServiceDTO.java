package com.nokor.efinance.share.financialproduct;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class FinProductServiceDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -8490713717701272946L;

	private Long id;
	private Long finServiceId;	
	
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
	 * @return the finServiceId
	 */
	public Long getFinServiceId() {
		return finServiceId;
	}

	/**
	 * @param finServiceId the finServiceId to set
	 */
	public void setFinServiceId(Long finServiceId) {
		this.finServiceId = finServiceId;
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof FinProductServiceDTO)) {
			 return false;
		 }
		 FinProductServiceDTO finProductServiceDTO = (FinProductServiceDTO) arg0;
		 return getId() != null && getId().equals(finProductServiceDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
