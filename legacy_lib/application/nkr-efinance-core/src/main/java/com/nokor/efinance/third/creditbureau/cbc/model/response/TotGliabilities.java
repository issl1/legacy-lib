/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class TotGliabilities implements Serializable {

	private static final long serialVersionUID = 875946139361445123L;
	
	private String totgliabcurr;
	private Double totgliab;
	
	/**
	 * @return the totgliabcurr
	 */
	public String getTotgliabcurr() {
		return totgliabcurr;
	}
	/**
	 * @param totgliabcurr the totgliabcurr to set
	 */
	public void setTotgliabcurr(String totgliabcurr) {
		this.totgliabcurr = totgliabcurr;
	}
	/**
	 * @return the totgliab
	 */
	public Double getTotgliab() {
		return totgliab;
	}
	/**
	 * @param totgliab the totgliab to set
	 */
	public void setTotgliab(Double totgliab) {
		this.totgliab = totgliab;
	}
}
