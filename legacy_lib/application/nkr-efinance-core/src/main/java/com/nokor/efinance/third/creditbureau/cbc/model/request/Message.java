package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;


/**
 * @author ly.youhort
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 5784079618166828637L;
	
	private Enquiry enquiry;

	/**
	 * @return the enquiry
	 */
	public Enquiry getEnquiry() {
		return enquiry;
	}

	/**
	 * @param enquiry the enquiry to set
	 */
	public void setEnquiry(Enquiry enquiry) {
		this.enquiry = enquiry;
	}
	
}
