/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author sun.vanndy
 *
 */
public class Employer implements Serializable {

	private static final long serialVersionUID = -8246257204093640606L;
	
	private String edld;
	private String etyp;
	private String eslf;
	private String eoca;
	private String eoce;
	private Integer elen;
	private Double etms;
	private String ecurr;
	private String enma;
	private String enme;
	private List<Eadr> eadrs;
	
	/**
	 * @return the edld
	 */
	public String getEdld() {
		return edld;
	}
	/**
	 * @param edld the edld to set
	 */
	public void setEdld(String edld) {
		this.edld = edld;
	}
	/**
	 * @return the etyp
	 */
	public String getEtyp() {
		return etyp;
	}
	/**
	 * @param etyp the etyp to set
	 */
	public void setEtyp(String etyp) {
		this.etyp = etyp;
	}
	/**
	 * @return the eslf
	 */
	public String getEslf() {
		return eslf;
	}
	/**
	 * @param eslf the eslf to set
	 */
	public void setEslf(String eslf) {
		this.eslf = eslf;
	}
	/**
	 * @return the eoca
	 */
	public String getEoca() {
		return eoca;
	}
	/**
	 * @param eoca the eoca to set
	 */
	public void setEoca(String eoca) {
		this.eoca = eoca;
	}
	/**
	 * @return the eoce
	 */
	public String getEoce() {
		return eoce;
	}
	/**
	 * @param eoce the eoce to set
	 */
	public void setEoce(String eoce) {
		this.eoce = eoce;
	}
	/**
	 * @return the elen
	 */
	public Integer getElen() {
		return elen;
	}
	/**
	 * @param elen the elen to set
	 */
	public void setElen(Integer elen) {
		this.elen = elen;
	}
	/**
	 * @return the etms
	 */
	public Double getEtms() {
		return etms;
	}
	/**
	 * @param etms the etms to set
	 */
	public void setEtms(Double etms) {
		this.etms = etms;
	}
	/**
	 * @return the ecurr
	 */
	public String getEcurr() {
		return ecurr;
	}
	/**
	 * @param ecurr the ecurr to set
	 */
	public void setEcurr(String ecurr) {
		this.ecurr = ecurr;
	}
	/**
	 * @return the enma
	 */
	public String getEnma() {
		return enma;
	}
	/**
	 * @param enma the enma to set
	 */
	public void setEnma(String enma) {
		this.enma = enma;
	}
	/**
	 * @return the enme
	 */
	public String getEnme() {
		return enme;
	}
	/**
	 * @param enme the enme to set
	 */
	public void setEnme(String enme) {
		this.enme = enme;
	}
	/**
	 * @return the eadrs
	 */
	public List<Eadr> getEadrs() {
		return eadrs;
	}
	/**
	 * @param eadrs the eadrs to set
	 */
	public void setEadrs(List<Eadr> eadrs) {
		this.eadrs = eadrs;
	}
	
}
