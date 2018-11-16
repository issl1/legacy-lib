package com.nokor.efinance.third.creditbureau.cbc.model;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Cid implements Serializable {

	private static final long serialVersionUID = 2618350075428751314L;
	
	private String cid1;
	private String cid2;
	private Cid3 cid3;
	/**
	 * @return the cid1
	 */
	public String getCid1() {
		return cid1;
	}
	/**
	 * @param cid1 the cid1 to set
	 */
	public void setCid1(String cid1) {
		this.cid1 = cid1;
	}
	/**
	 * @return the cid2
	 */
	public String getCid2() {
		return cid2;
	}
	/**
	 * @param cid2 the cid2 to set
	 */
	public void setCid2(String cid2) {
		this.cid2 = cid2;
	}
	/**
	 * @return the cid3
	 */
	public Cid3 getCid3() {
		return cid3;
	}
	/**
	 * @param cid3 the cid3 to set
	 */
	public void setCid3(Cid3 cid3) {
		this.cid3 = cid3;
	}
}
