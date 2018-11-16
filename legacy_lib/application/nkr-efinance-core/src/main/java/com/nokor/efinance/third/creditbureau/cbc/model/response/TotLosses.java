/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class TotLosses implements Serializable {

	private static final long serialVersionUID = -2740017425303902685L;
	
	private String totlosscurr;
	private Double totloss;
	
	/**
	 * @return the totlosscurr
	 */
	public String getTotlosscurr() {
		return totlosscurr;
	}
	/**
	 * @param totlosscurr the totlosscurr to set
	 */
	public void setTotlosscurr(String totlosscurr) {
		this.totlosscurr = totlosscurr;
	}
	/**
	 * @return the totloss
	 */
	public Double getTotloss() {
		return totloss;
	}
	/**
	 * @param totloss the totloss to set
	 */
	public void setTotloss(Double totloss) {
		this.totloss = totloss;
	}
}
