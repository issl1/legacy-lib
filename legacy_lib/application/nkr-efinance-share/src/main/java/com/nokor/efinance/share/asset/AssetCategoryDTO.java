package com.nokor.efinance.share.asset;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class AssetCategoryDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 1259671532666180368L;
	
	private Long id;
	private String name;
	private String nameEn;
		
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AssetCategoryDTO)) {
			 return false;
		 }
		 AssetCategoryDTO assetBrandDTO = (AssetCategoryDTO) arg0;
		 return getId() != null && getId().equals(assetBrandDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
