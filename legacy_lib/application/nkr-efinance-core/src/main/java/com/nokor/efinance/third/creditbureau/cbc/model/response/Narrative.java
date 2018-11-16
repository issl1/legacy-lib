/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Narrative implements Serializable {

	private static final long serialVersionUID = 4173493450423658039L;
	
	private String naloaddt;
	private String naloadedby;
	private String natype;
	private String natext;
	private String natexta;
	/**
	 * @return the naloaddt
	 */
	public String getNaloaddt() {
		return naloaddt;
	}
	/**
	 * @param naloaddt the naloaddt to set
	 */
	public void setNaloaddt(String naloaddt) {
		this.naloaddt = naloaddt;
	}
	/**
	 * @return the naloadedby
	 */
	public String getNaloadedby() {
		return naloadedby;
	}
	/**
	 * @param naloadedby the naloadedby to set
	 */
	public void setNaloadedby(String naloadedby) {
		this.naloadedby = naloadedby;
	}
	/**
	 * @return the natype
	 */
	public String getNatype() {
		return natype;
	}
	/**
	 * @param natype the natype to set
	 */
	public void setNatype(String natype) {
		this.natype = natype;
	}
	/**
	 * @return the natext
	 */
	public String getNatext() {
		return natext;
	}
	/**
	 * @param natext the natext to set
	 */
	public void setNatext(String natext) {
		this.natext = natext;
	}
	/**
	 * @return the natexta
	 */
	public String getNatexta() {
		return natexta;
	}
	/**
	 * @param natexta the natexta to set
	 */
	public void setNatexta(String natexta) {
		this.natexta = natexta;
	}
}
