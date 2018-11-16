package com.nokor.common.messaging.share.refdata;

import com.nokor.common.messaging.share.BaseEntityRefDTO;
import com.nokor.common.messaging.share.UriDTO;


/**
 * @author prasnar
 *
 */
public class RefDataDTO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = 1046870897584926178L;

	private UriDTO parentRefDataUri;
	
	/**
	 * 
	 */
	public RefDataDTO() {
	}

	/**
	 * @return the parentRefDataUri
	 */
	public UriDTO getParentRefDataUri() {
		return parentRefDataUri;
	}

	/**
	 * @param parentRefDataUri the parentRefDataUri to set
	 */
	public void setParentRefDataUri(UriDTO parentRefDataUri) {
		this.parentRefDataUri = parentRefDataUri;
	}
	

	
}
