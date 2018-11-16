/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class CurDelBalances implements Serializable{

	private static final long serialVersionUID = -3659327977022889102L;
	
	private String curdbcurr;
	private String curdb;
	
	/**
	 * @return the curdbcurr
	 */
	public String getCurdbcurr() {
		return curdbcurr;
	}
	/**
	 * @param curdbcurr the curdbcurr to set
	 */
	public void setCurdbcurr(String curdbcurr) {
		this.curdbcurr = curdbcurr;
	}
	/**
	 * @return the curdb
	 */
	public String getCurdb() {
		return curdb;
	}
	/**
	 * @param curdb the curdb to set
	 */
	public void setCurdb(String curdb) {
		this.curdb = curdb;
	}
}
