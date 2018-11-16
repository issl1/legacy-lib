package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Provided implements Serializable {

	private static final long serialVersionUID = 3964539439081721986L;
	
	private Pcnam pcnam;
	private String pcdob;
	private Pcplb pcplb;
	private String pcgnd;
	private String pcmar;
	private String pcnat;
	private String pceml;
	/**
	 * @return the pcnam
	 */
	public Pcnam getPcnam() {
		return pcnam;
	}
	/**
	 * @param pcnam the pcnam to set
	 */
	public void setPcnam(Pcnam pcnam) {
		this.pcnam = pcnam;
	}
	/**
	 * @return the pcdob
	 */
	public String getPcdob() {
		return pcdob;
	}
	/**
	 * @param pcdob the pcdob to set
	 */
	public void setPcdob(String pcdob) {
		this.pcdob = pcdob;
	}
	/**
	 * @return the pcplb
	 */
	public Pcplb getPcplb() {
		return pcplb;
	}
	/**
	 * @param pcplb the pcplb to set
	 */
	public void setPcplb(Pcplb pcplb) {
		this.pcplb = pcplb;
	}
	/**
	 * @return the pcgnd
	 */
	public String getPcgnd() {
		return pcgnd;
	}
	/**
	 * @param pcgnd the pcgnd to set
	 */
	public void setPcgnd(String pcgnd) {
		this.pcgnd = pcgnd;
	}
	/**
	 * @return the pcmar
	 */
	public String getPcmar() {
		return pcmar;
	}
	/**
	 * @param pcmar the pcmar to set
	 */
	public void setPcmar(String pcmar) {
		this.pcmar = pcmar;
	}
	/**
	 * @return the pcnat
	 */
	public String getPcnat() {
		return pcnat;
	}
	/**
	 * @param pcnat the pcnat to set
	 */
	public void setPcnat(String pcnat) {
		this.pcnat = pcnat;
	}
	/**
	 * @return the pceml
	 */
	public String getPceml() {
		return pceml;
	}
	/**
	 * @param pceml the pceml to set
	 */
	public void setPceml(String pceml) {
		this.pceml = pceml;
	}
	
}
