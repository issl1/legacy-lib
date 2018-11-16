/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class PublicNotices implements Serializable {

	private static final long serialVersionUID = 4833283971686376920L;
	
	private PublicNotice publicNotice;

	/**
	 * @return the publicNotice
	 */
	public PublicNotice getPublicNotice() {
		return publicNotice;
	}

	/**
	 * @param publicNotice the publicNotice to set
	 */
	public void setPublicNotice(PublicNotice publicNotice) {
		this.publicNotice = publicNotice;
	}

}
