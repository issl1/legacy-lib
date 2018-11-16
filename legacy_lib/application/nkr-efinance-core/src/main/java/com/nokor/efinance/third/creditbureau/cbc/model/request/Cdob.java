package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Cdob implements Serializable {

	private static final long serialVersionUID = 1874021916811133930L;
	
	private String cdbd;
	private String cdbm;
	private String cdby;
	
	/**
	 * @return the cdbd
	 */
	public String getCdbd() {
		return cdbd;
	}
	/**
	 * @param cdbd the cdbd to set
	 */
	public void setCdbd(String cdbd) {
		this.cdbd = cdbd;
	}
	/**
	 * @return the cdbm
	 */
	public String getCdbm() {
		return cdbm;
	}
	/**
	 * @param cdbm the cdbm to set
	 */
	public void setCdbm(String cdbm) {
		this.cdbm = cdbm;
	}
	/**
	 * @return the cdby
	 */
	public String getCdby() {
		return cdby;
	}
	/**
	 * @param cdby the cdby to set
	 */
	public void setCdby(String cdby) {
		this.cdby = cdby;
	}
}
