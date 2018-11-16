package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Cplb implements Serializable {
	
	private static final long serialVersionUID = 5097381233409218293L;
	
	private String cplbc;
	private String cplbp;
	private String cplbd;
	private String cplbcm;
	
	/**
	 * @return the cplbc
	 */
	public String getCplbc() {
		return cplbc;
	}
	/**
	 * @param cplbc the cplbc to set
	 */
	public void setCplbc(String cplbc) {
		this.cplbc = cplbc;
	}
	/**
	 * @return the cplbp
	 */
	public String getCplbp() {
		return cplbp;
	}
	/**
	 * @param cplbp the cplbp to set
	 */
	public void setCplbp(String cplbp) {
		this.cplbp = cplbp;
	}
	/**
	 * @return the cplbd
	 */
	public String getCplbd() {
		return cplbd;
	}
	/**
	 * @param cplbd the cplbd to set
	 */
	public void setCplbd(String cplbd) {
		this.cplbd = cplbd;
	}
	/**
	 * @return the cplbcm
	 */
	public String getCplbcm() {
		return cplbcm;
	}
	/**
	 * @param cplbcm the cplbcm to set
	 */
	public void setCplbcm(String cplbcm) {
		this.cplbcm = cplbcm;
	}
}
