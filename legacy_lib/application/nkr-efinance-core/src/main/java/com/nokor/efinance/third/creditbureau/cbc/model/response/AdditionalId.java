/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class AdditionalId implements Serializable {

	private static final long serialVersionUID = -5899891041421803869L;
	
	private String aidcid1;
	private String aidcid2;
	private String aidcid3;
	private String aidloaddt;
	/**
	 * @return the aidcid1
	 */
	public String getAidcid1() {
		return aidcid1;
	}
	/**
	 * @param aidcid1 the aidcid1 to set
	 */
	public void setAidcid1(String aidcid1) {
		this.aidcid1 = aidcid1;
	}
	/**
	 * @return the aidcid2
	 */
	public String getAidcid2() {
		return aidcid2;
	}
	/**
	 * @param aidcid2 the aidcid2 to set
	 */
	public void setAidcid2(String aidcid2) {
		this.aidcid2 = aidcid2;
	}
	/**
	 * @return the aidcid3
	 */
	public String getAidcid3() {
		return aidcid3;
	}
	/**
	 * @param aidcid3 the aidcid3 to set
	 */
	public void setAidcid3(String aidcid3) {
		this.aidcid3 = aidcid3;
	}
	/**
	 * @return the aidloaddt
	 */
	public String getAidloaddt() {
		return aidloaddt;
	}
	/**
	 * @param aidloaddt the aidloaddt to set
	 */
	public void setAidloaddt(String aidloaddt) {
		this.aidloaddt = aidloaddt;
	}
}
