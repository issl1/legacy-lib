package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;
import java.util.List;

import com.nokor.efinance.third.creditbureau.cbc.model.Cid;

/**
 * @author ly.youhort
 */
public class Consumer implements Serializable {

	private static final long serialVersionUID = -187029544013732816L;

	private String capl;
	private List<Cid> cids;
	private Cdob cdob;
	private Cplb cplb;
	private String cgnd;
	private String cmar;
	private String cnat;
	private Cnam cnam;
	private String ceml;
	private Cadr cadr;
	private Ccnt ccnt;
	private Cemp cemp;
	/**
	 * @return the capl
	 */
	public String getCapl() {
		return capl;
	}
	/**
	 * @param capl the capl to set
	 */
	public void setCapl(String capl) {
		this.capl = capl;
	}
	/**
	 * @return the cids
	 */
	public List<Cid> getCids() {
		return cids;
	}
	/**
	 * @param cids the cids to set
	 */
	public void setCids(List<Cid> cids) {
		this.cids = cids;
	}
	/**
	 * @return the cdob
	 */
	public Cdob getCdob() {
		return cdob;
	}
	/**
	 * @param cdob the cdob to set
	 */
	public void setCdob(Cdob cdob) {
		this.cdob = cdob;
	}
	/**
	 * @return the cplb
	 */
	public Cplb getCplb() {
		return cplb;
	}
	/**
	 * @param cplb the cplb to set
	 */
	public void setCplb(Cplb cplb) {
		this.cplb = cplb;
	}
	/**
	 * @return the cgnd
	 */
	public String getCgnd() {
		return cgnd;
	}
	/**
	 * @param cgnd the cgnd to set
	 */
	public void setCgnd(String cgnd) {
		this.cgnd = cgnd;
	}
	/**
	 * @return the cmar
	 */
	public String getCmar() {
		return cmar;
	}
	/**
	 * @param cmar the cmar to set
	 */
	public void setCmar(String cmar) {
		this.cmar = cmar;
	}
	/**
	 * @return the cnat
	 */
	public String getCnat() {
		return cnat;
	}
	/**
	 * @param cnat the cnat to set
	 */
	public void setCnat(String cnat) {
		this.cnat = cnat;
	}
	/**
	 * @return the cnam
	 */
	public Cnam getCnam() {
		return cnam;
	}
	/**
	 * @param cnam the cnam to set
	 */
	public void setCnam(Cnam cnam) {
		this.cnam = cnam;
	}
	/**
	 * @return the ceml
	 */
	public String getCeml() {
		return ceml;
	}
	/**
	 * @param ceml the ceml to set
	 */
	public void setCeml(String ceml) {
		this.ceml = ceml;
	}
	/**
	 * @return the cadr
	 */
	public Cadr getCadr() {
		return cadr;
	}
	/**
	 * @param cadr the cadr to set
	 */
	public void setCadr(Cadr cadr) {
		this.cadr = cadr;
	}
	/**
	 * @return the ccnt
	 */
	public Ccnt getCcnt() {
		return ccnt;
	}
	/**
	 * @param ccnt the ccnt to set
	 */
	public void setCcnt(Ccnt ccnt) {
		this.ccnt = ccnt;
	}
	/**
	 * @return the cemp
	 */
	public Cemp getCemp() {
		return cemp;
	}
	/**
	 * @param cemp the cemp to set
	 */
	public void setCemp(Cemp cemp) {
		this.cemp = cemp;
	}
}
