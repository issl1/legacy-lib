package com.nokor.ersys.messaging.share.accounting;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class AccountCategoryDTO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = 1234351231037307257L;
	

	private String name;
	private String nameEn;
	private Long parentId;
    private Long rootCategory;
	
    /**
     * 
     */
    public AccountCategoryDTO() {
    	
    }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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
	 * @return the rootCategory
	 */
	public Long getRootCategory() {
		return rootCategory;
	}

	/**
	 * @param rootCategory the rootCategory to set
	 */
	public void setRootCategory(Long rootCategory) {
		this.rootCategory = rootCategory;
	}


}
