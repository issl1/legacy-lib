/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class TotLiabilities implements Serializable {

	private static final long serialVersionUID = 8737759992520894898L;
	private String totliabcurr;
	private Double totliab;
	
	/**
	 * @return the totliabcurr
	 */
	public String getTotliabcurr() {
		return totliabcurr;
	}
	/**
	 * @param totliabcurr the totliabcurr to set
	 */
	public void setTotliabcurr(String totliabcurr) {
		this.totliabcurr = totliabcurr;
	}
	/**
	 * @return the totliab
	 */
	public Double getTotliab() {
		return totliab;
	}
	/**
	 * @param totliab the totliab to set
	 */
	public void setTotliab(Double totliab) {
		this.totliab = totliab;
	}
}
