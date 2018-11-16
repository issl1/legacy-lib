/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Parser implements Serializable {

	private static final long serialVersionUID = 3488206432541028995L;
	
	private String rspmsg;
	private String reqdata;

	
	/**
	 * @return the rspmsg
	 */
	public String getRspmsg() {
		return rspmsg;
	}

	/**
	 * @param rspmsg the rspmsg to set
	 */
	public void setRspmsg(String rspmsg) {
		this.rspmsg = rspmsg;
	}

	/**
	 * @return the reqdata
	 */
	public String getReqdata() {
		return reqdata;
	}

	/**
	 * @param reqdata the reqdata to set
	 */
	public void setReqdata(String reqdata) {
		this.reqdata = reqdata;
	}
	
}
