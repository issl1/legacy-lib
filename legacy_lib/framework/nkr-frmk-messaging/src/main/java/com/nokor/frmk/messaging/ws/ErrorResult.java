package com.nokor.frmk.messaging.ws;

import java.io.Serializable;

/**
 * 
 * @author prasnar
 *
 */
public class ErrorResult implements Serializable {
	/** */
	private static final long serialVersionUID = -7689269116889134778L;
	
	private Integer status;
	private String desc;
	private String longDesc;
	private String uri;
	
	/**
	 * 
	 */
	public ErrorResult() {
		
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the longDesc
	 */
	public String getLongDesc() {
		return longDesc;
	}

	/**
	 * @param longDesc the longDesc to set
	 */
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
}
