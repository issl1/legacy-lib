package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;


/**
 * @author ly.youhort
 */
public class Cemp implements Serializable {

	private static final long serialVersionUID = -2097546420142168746L;
	
	private String etyp;
	private String eslf;
	private String eoca;
	private String eoce;
	private int elen;
	private Double etms;
	private String ecurr;
	private String enma;
	private String enme;
	private Eadr eadr;
	
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
	public int getElen() {
		return elen;
	}
	/**
	 * @param elen the elen to set
	 */
	public void setElen(int elen) {
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
	 * @return the eadr
	 */
	public Eadr getEadr() {
		return eadr;
	}
	/**
	 * @param eadr the eadr to set
	 */
	public void setEadr(Eadr eadr) {
		this.eadr = eadr;
	}
}
