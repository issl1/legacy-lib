/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Available implements Serializable {

	private static final long serialVersionUID = 4031754216210666965L;
	
	private Acnam acnam;
	private String acdob;
	private Acplb acplb;
	private String acgnd;
	private String acmar;
	private String acnat;
	private String aceml;
	/**
	 * @return the acnam
	 */
	public Acnam getAcnam() {
		return acnam;
	}
	/**
	 * @param acnam the acnam to set
	 */
	public void setAcnam(Acnam acnam) {
		this.acnam = acnam;
	}
	/**
	 * @return the acdob
	 */
	public String getAcdob() {
		return acdob;
	}
	/**
	 * @param acdob the acdob to set
	 */
	public void setAcdob(String acdob) {
		this.acdob = acdob;
	}
	/**
	 * @return the acplb
	 */
	public Acplb getAcplb() {
		return acplb;
	}
	/**
	 * @param acplb the acplb to set
	 */
	public void setAcplb(Acplb acplb) {
		this.acplb = acplb;
	}
	/**
	 * @return the acgnd
	 */
	public String getAcgnd() {
		return acgnd;
	}
	/**
	 * @param acgnd the acgnd to set
	 */
	public void setAcgnd(String acgnd) {
		this.acgnd = acgnd;
	}
	/**
	 * @return the acmar
	 */
	public String getAcmar() {
		return acmar;
	}
	/**
	 * @param acmar the acmar to set
	 */
	public void setAcmar(String acmar) {
		this.acmar = acmar;
	}
	/**
	 * @return the acnat
	 */
	public String getAcnat() {
		return acnat;
	}
	/**
	 * @param acnat the acnat to set
	 */
	public void setAcnat(String acnat) {
		this.acnat = acnat;
	}
	/**
	 * @return the aceml
	 */
	public String getAceml() {
		return aceml;
	}
	/**
	 * @param aceml the aceml to set
	 */
	public void setAceml(String aceml) {
		this.aceml = aceml;
	}
}
