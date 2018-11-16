package com.nokor.efinance.share.campaign;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class CreditControlItemDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 5483529521172775671L;
	
	private Long id;
	private String code;
	private String value1;
	private String value2;
	private String value3;
		
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
		
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
	 * @return the value1
	 */
	public String getValue1() {
		return value1;
	}

	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	/**
	 * @return the value3
	 */
	public String getValue3() {
		return value3;
	}

	/**
	 * @param value3 the value3 to set
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CreditControlItemDTO)) {
			 return false;
		 }
		 CreditControlItemDTO campaignTermDTO = (CreditControlItemDTO) arg0;
		 return getId() != null && getId().equals(campaignTermDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
