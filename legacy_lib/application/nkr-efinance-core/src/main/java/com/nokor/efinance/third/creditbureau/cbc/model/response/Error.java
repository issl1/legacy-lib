/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;

/**
 * @author sun.vanndy
 *
 */
public class Error implements Serializable {

	private static final long serialVersionUID = -473609200037265723L;
	private Integer consumerseq;
	private String field;
	private String rspmsg;
	private String data;
	/**
	 * @return the consumerseq
	 */
	public Integer getConsumerseq() {
		return consumerseq;
	}
	/**
	 * @param consumerseq the consumerseq to set
	 */
	public void setConsumerseq(Integer consumerseq) {
		this.consumerseq = consumerseq;
	}
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return the rspmsg
	 */
	public String getRspmsg() {
		return rspmsg;
	}
	/**
	 * @param rspmsg the rspmsg to set
	 */
	public void setRspmsg(String rspmsg) {
		this.rspmsg = rspmsg;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
}
