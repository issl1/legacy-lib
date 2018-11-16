package com.nokor.common.messaging.ws.resource.security.vo;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * @author prasnar
 *
 */
public class SecManagedProfileVO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = 7905970919691206698L;
	
	private String parentProCode;
	private String childProCode;


	/**
	 * 
	 */
	public SecManagedProfileVO() {
		
	}


	/**
	 * @return the parentProCode
	 */
	public String getParentProCode() {
		return parentProCode;
	}


	/**
	 * @param parentProCode the parentProCode to set
	 */
	public void setParentProCode(String parentProCode) {
		this.parentProCode = parentProCode;
	}


	/**
	 * @return the childProCode
	 */
	public String getChildProCode() {
		return childProCode;
	}


	/**
	 * @param childProCode the childProCode to set
	 */
	public void setChildProCode(String childProCode) {
		this.childProCode = childProCode;
	}
	
		
}
