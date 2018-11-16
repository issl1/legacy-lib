/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class PrevEnquiry implements Serializable {

	private static final long serialVersionUID = 2757422512183480021L;
	
	private String pedate;
	private String peenqr;
	private String petype;
	private String pemembref;
	private String peprd;
	private Double peamount;
	private String pecurr;
	private Pename pename;
	/**
	 * @return the pedate
	 */
	public String getPedate() {
		return pedate;
	}
	/**
	 * @param pedate the pedate to set
	 */
	public void setPedate(String pedate) {
		this.pedate = pedate;
	}
	/**
	 * @return the peenqr
	 */
	public String getPeenqr() {
		return peenqr;
	}
	/**
	 * @param peenqr the peenqr to set
	 */
	public void setPeenqr(String peenqr) {
		this.peenqr = peenqr;
	}
	/**
	 * @return the petype
	 */
	public String getPetype() {
		return petype;
	}
	/**
	 * @param petype the petype to set
	 */
	public void setPetype(String petype) {
		this.petype = petype;
	}
	/**
	 * @return the pemembref
	 */
	public String getPemembref() {
		return pemembref;
	}
	/**
	 * @param pemembref the pemembref to set
	 */
	public void setPemembref(String pemembref) {
		this.pemembref = pemembref;
	}
	/**
	 * @return the peprd
	 */
	public String getPeprd() {
		return peprd;
	}
	/**
	 * @param peprd the peprd to set
	 */
	public void setPeprd(String peprd) {
		this.peprd = peprd;
	}
	/**
	 * @return the peamount
	 */
	public Double getPeamount() {
		return peamount;
	}
	/**
	 * @param peamount the peamount to set
	 */
	public void setPeamount(Double peamount) {
		this.peamount = peamount;
	}
	/**
	 * @return the pecurr
	 */
	public String getPecurr() {
		return pecurr;
	}
	/**
	 * @param pecurr the pecurr to set
	 */
	public void setPecurr(String pecurr) {
		this.pecurr = pecurr;
	}
	/**
	 * @return the pename
	 */
	public Pename getPename() {
		return pename;
	}
	/**
	 * @param pename the pename to set
	 */
	public void setPename(Pename pename) {
		this.pename = pename;
	}
}
