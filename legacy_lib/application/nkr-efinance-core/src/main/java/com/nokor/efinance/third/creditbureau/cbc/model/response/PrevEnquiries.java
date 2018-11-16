/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class PrevEnquiries implements Serializable {

	private static final long serialVersionUID = 640609393084745777L;
	
	private PrevEnquiry prevEnquiry;

	/**
	 * @return the prevEnquiry
	 */
	public PrevEnquiry getPrevEnquiry() {
		return prevEnquiry;
	}

	/**
	 * @param prevEnquiry the prevEnquiry to set
	 */
	public void setPrevEnquiry(PrevEnquiry prevEnquiry) {
		this.prevEnquiry = prevEnquiry;
	}

	
}
