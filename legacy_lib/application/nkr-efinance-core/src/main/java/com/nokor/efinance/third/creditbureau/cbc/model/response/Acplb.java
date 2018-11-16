/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Acplb implements Serializable {

	private static final long serialVersionUID = -2999626171462703469L;
	
	private String acplbc;
	private String acplbp;
	private String acplbd;
	private String acplbcm;
	/**
	 * @return the acplbc
	 */
	public String getAcplbc() {
		return acplbc;
	}
	/**
	 * @param acplbc the acplbc to set
	 */
	public void setAcplbc(String acplbc) {
		this.acplbc = acplbc;
	}
	/**
	 * @return the acplbp
	 */
	public String getAcplbp() {
		return acplbp;
	}
	/**
	 * @param acplbp the acplbp to set
	 */
	public void setAcplbp(String acplbp) {
		this.acplbp = acplbp;
	}
	/**
	 * @return the acplbd
	 */
	public String getAcplbd() {
		return acplbd;
	}
	/**
	 * @param acplbd the acplbd to set
	 */
	public void setAcplbd(String acplbd) {
		this.acplbd = acplbd;
	}
	/**
	 * @return the acplbcm
	 */
	public String getAcplbcm() {
		return acplbcm;
	}
	/**
	 * @param acplbcm the acplbcm to set
	 */
	public void setAcplbcm(String acplbcm) {
		this.acplbcm = acplbcm;
	}
}
