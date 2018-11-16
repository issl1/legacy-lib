/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class TotLimits implements Serializable {

	private static final long serialVersionUID = 94170540902180845L;
	
	private String totlimcurr;
	private Double totlim;
	/**
	 * @return the totlimcurr
	 */
	public String getTotlimcurr() {
		return totlimcurr;
	}
	/**
	 * @param totlimcurr the totlimcurr to set
	 */
	public void setTotlimcurr(String totlimcurr) {
		this.totlimcurr = totlimcurr;
	}
	/**
	 * @return the totlim
	 */
	public Double getTotlim() {
		return totlim;
	}
	/**
	 * @param totlim the totlim to set
	 */
	public void setTotlim(Double totlim) {
		this.totlim = totlim;
	}
}
