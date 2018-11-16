package com.nokor.efinance.core.applicant.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;



/**
 * 
 * @author uhout.cheng
 */
public class AppContactTypeVO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 5962382564109297794L;
	
	private String code;
	private String type;
	private String value;
	
	/**
	 * 
	 * @param code
	 * @param type
	 * @param value
	 */
	public AppContactTypeVO(String code, String type, String value) {
		this.code = code;
		this.type = type;
		this.value = value;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String typeCnt = StringUtils.EMPTY;
		if (ETypeContactInfo.LANDLINE.getCode().equals(type)) {
			typeCnt = "L";
		} else if (ETypeContactInfo.MOBILE.getCode().equals(type)) {
			typeCnt = "M";
		} else if (ETypeContactInfo.EMAIL.getCode().equals(type)) {
			typeCnt = "E";
		} 
		return this.code + " - " + typeCnt + " - " + this.value; 
	}
}