package com.nokor.efinance.third.creditbureau.cbc.model;

import java.io.Serializable;

/**
 * @author ly.youhort
 */
public class Cid3 implements Serializable {

	private static final long serialVersionUID = 3932650190568863379L;
	private String cid3d;
	private String cid3m;
	private String cid3y;
	/**
	 * @return the cid3d
	 */
	public String getCid3d() {
		return cid3d;
	}
	/**
	 * @param cid3d the cid3d to set
	 */
	public void setCid3d(String cid3d) {
		this.cid3d = cid3d;
	}
	/**
	 * @return the cid3m
	 */
	public String getCid3m() {
		return cid3m;
	}
	/**
	 * @param cid3m the cid3m to set
	 */
	public void setCid3m(String cid3m) {
		this.cid3m = cid3m;
	}
	/**
	 * @return the cid3y
	 */
	public String getCid3y() {
		return cid3y;
	}
	/**
	 * @param cid3y the cid3y to set
	 */
	public void setCid3y(String cid3y) {
		this.cid3y = cid3y;
	}
}
