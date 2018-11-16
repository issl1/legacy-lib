package com.nokor.common.messaging.share;



/**
 * @author prasnar
 *
 */
public abstract class BaseEntityRefDTO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = 6418440485324409244L;

	protected String code;
	protected String desc;
	protected String descEn;
	protected Integer sortIndex;
	protected Boolean isActive;
	
	/**
	 * 
	 */
	public BaseEntityRefDTO() {
		
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

	/**
	 * @return the sortIndex
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	

}
