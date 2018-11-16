package com.nokor.efinance.share.applicant;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class AppContactTypeDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 2313482044371736195L;

	private String code;
	private String type;
	private String value;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
