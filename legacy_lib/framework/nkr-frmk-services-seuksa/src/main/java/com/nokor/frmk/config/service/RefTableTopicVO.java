package com.nokor.frmk.config.service;

import java.io.Serializable;

/**
 * 
 * @author phirun.kong
 *
 */
public class RefTableTopicVO implements Serializable {

	private Long id;
	private Long parentId;
	private Long refTableId;
	private String descEn;
	private String desc;
	
	public RefTableTopicVO() {
		
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the refTableId
	 */
	public Long getRefTableId() {
		return refTableId;
	}

	/**
	 * @param refTableId the refTableId to set
	 */
	public void setRefTableId(Long refTableId) {
		this.refTableId = refTableId;
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

}
