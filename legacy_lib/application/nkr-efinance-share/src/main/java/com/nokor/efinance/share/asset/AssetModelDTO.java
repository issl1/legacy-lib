package com.nokor.efinance.share.asset;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author youhort.ly
 */
public class AssetModelDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -8131418354065805942L;

	private Long id;
	private String assetId;
	private String desc;
	private String descEn;
	
	private String serie;
	private String characteristic;
	private Integer year;
	private double assetPrice;
	private Double averageMarketPrice;
	private RefDataDTO assetType;
	private AssetCategoryDTO assetCategory;
	private AssetBrandDTO assetBrand;
	private AssetRangeDTO assetRange;	
	private RefDataDTO engine;
	
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
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}
	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	 * @return the assetPrice
	 */
	public double getAssetPrice() {
		return assetPrice;
	}
	/**
	 * @param assetPrice the assetPrice to set
	 */
	public void setAssetPrice(double assetPrice) {
		this.assetPrice = assetPrice;
	}
	
	/**
	 * @return the averageMarketPrice
	 */
	public Double getAverageMarketPrice() {
		return averageMarketPrice;
	}
	/**
	 * @param averageMarketPrice the averageMarketPrice to set
	 */
	public void setAverageMarketPrice(Double averageMarketPrice) {
		this.averageMarketPrice = averageMarketPrice;
	}	
	
	/**
	 * @return the assetType
	 */
	public RefDataDTO getAssetType() {
		return assetType;
	}
	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(RefDataDTO assetType) {
		this.assetType = assetType;
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
	 * @return the assetRange
	 */
	public AssetRangeDTO getAssetRange() {
		return assetRange;
	}
	/**
	 * @param assetRange the assetRange to set
	 */
	public void setAssetRange(AssetRangeDTO assetRange) {
		this.assetRange = assetRange;
	}	
	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}
	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}
	/**
	 * @return the characteristic
	 */
	public String getCharacteristic() {
		return characteristic;
	}
	/**
	 * @param characteristic the characteristic to set
	 */
	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	
	/**
	 * @return the assetCategory
	 */
	public AssetCategoryDTO getAssetCategory() {
		return assetCategory;
	}
	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategoryDTO assetCategory) {
		this.assetCategory = assetCategory;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AssetModelDTO)) {
			 return false;
		 }
		 AssetModelDTO assetModelDTO = (AssetModelDTO) arg0;
		 return getId() != null && getId().equals(assetModelDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
	
	/**
	 * @return the engine
	 */
	public RefDataDTO getEngine() {
		return engine;
	}
	/**
	 * @param engine the engine to set
	 */
	public void setEngine(RefDataDTO engine) {
		this.engine = engine;
	}
	
}
