package com.nokor.efinance.share.asset;

import java.io.Serializable;

/**
 * @author youhort.ly
 */
public class AssetRangeDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 4875104804265326111L;

	private Long id;
	private String code;
	private String desc;
	private String descEn;
	
	private AssetBrandDTO assetBrand;
	
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
	 * @return the assetBrand
	 */
	public AssetBrandDTO getAssetBrand() {
		return assetBrand;
	}
	/**
	 * @param assetBrand the assetBrand to set
	 */
	public void setAssetBrand(AssetBrandDTO assetBrand) {
		this.assetBrand = assetBrand;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AssetRangeDTO)) {
			 return false;
		 }
		 AssetRangeDTO assetRangeDTO = (AssetRangeDTO) arg0;
		 return getId() != null && getId().equals(assetRangeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
