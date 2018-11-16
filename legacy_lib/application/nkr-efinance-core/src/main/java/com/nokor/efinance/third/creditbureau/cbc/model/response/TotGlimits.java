/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class TotGlimits implements Serializable {

	private static final long serialVersionUID = -84096669590454261L;
	
	private String totglimcurr;
	private Double totglim;
	
	/**
	 * @return the totglimcurr
	 */
	public String getTotglimcurr() {
		return totglimcurr;
	}
	/**
	 * @param totglimcurr the totglimcurr to set
	 */
	public void setTotglimcurr(String totglimcurr) {
		this.totglimcurr = totglimcurr;
	}
	/**
	 * @return the totglim
	 */
	public Double getTotglim() {
		return totglim;
	}
	/**
	 * @param totglim the totglim to set
	 */
	public void setTotglim(Double totglim) {
		this.totglim = totglim;
	}	
}
