package com.nokor.efinance.share.term;

import java.io.Serializable;

/**
 * Term DTO
 * @author bunlong.taing
 */
public class TermDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -989548364393737009L;
	
	private Long id;
	private Integer value;
	private String desc;
	private String descEn;
	
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
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
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
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}
	
	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

}
