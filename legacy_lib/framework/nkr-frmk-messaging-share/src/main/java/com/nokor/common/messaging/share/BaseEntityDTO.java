package com.nokor.common.messaging.share;

import java.io.Serializable;


/**
 * @author prasnar
 *
 */
public abstract class BaseEntityDTO implements Serializable, Comparable<BaseEntityDTO> {
	/** */
	private static final long serialVersionUID = 4920856729792563846L;

	protected Long id;
	
	/**
	 * 
	 */
	public BaseEntityDTO() {
		
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof BaseEntityDTO)) {
			 return false;
		 }
		 BaseEntityDTO refDataDTO = (BaseEntityDTO) arg0;
		 return getId() != null && getId().equals(refDataDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}


	@Override
	public int compareTo(BaseEntityDTO arg0) {
		return getId() == null ? 0 : getId().compareTo(arg0.getId());
	}
}
