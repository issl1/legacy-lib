package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Disclaimer implements Serializable {

	private static final long serialVersionUID = 8004880116867443562L;
	
	private String ditext;

	/**
	 * @return the ditext
	 */
	public String getDitext() {
		return ditext;
	}

	/**
	 * @param ditext the ditext to set
	 */
	public void setDitext(String ditext) {
		this.ditext = ditext;
	}
}
