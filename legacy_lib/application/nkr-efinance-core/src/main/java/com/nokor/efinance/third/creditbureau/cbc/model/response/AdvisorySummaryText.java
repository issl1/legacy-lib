/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class AdvisorySummaryText implements Serializable {

	private static final long serialVersionUID = 92752875058763638L;
	
	private String advisorytext;

	/**
	 * @return the advisorytext
	 */
	public String getAdvisorytext() {
		return advisorytext;
	}

	/**
	 * @param advisorytext the advisorytext to set
	 */
	public void setAdvisorytext(String advisorytext) {
		this.advisorytext = advisorytext;
	}

}
