package com.nokor.efinance.share.subsidy;

import java.io.Serializable;
import java.util.Date;

import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class SubsidyDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 5799279703243658906L;
	
	private Long id;
	private Double subsidyAmount;
	private Integer monthFrom;
	private Integer monthTo;
	private Date startDate;
	private Date endDate;
	
	private AssetBrandDTO assetBrand;
	private AssetModelDTO assetModel;
	
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
	 * 
	 * @return the subsidyAmount
	 */
	public Double getSubsidyAmount() {
		return subsidyAmount;
	}
	
	/**
	 * @param subsidyAmount the subsidyAmount to set
	 */
	public void setSubsidyAmount(Double subsidyAmount) {
		this.subsidyAmount = subsidyAmount;
	}
	
	/**
	 * 
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * 
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the assetModel
	 */
	public AssetModelDTO getAssetModel() {
		return assetModel;
	}

	/**
	 * @param assetModel the assetModel to set
	 */
	public void setAssetModel(AssetModelDTO assetModel) {
		this.assetModel = assetModel;
	}

	/**
	 * @return the monthFrom
	 */
	public Integer getMonthFrom() {
		return monthFrom;
	}

	/**
	 * @param monthFrom the monthFrom to set
	 */
	public void setMonthFrom(Integer monthFrom) {
		this.monthFrom = monthFrom;
	}

	/**
	 * @return the monthTo
	 */
	public Integer getMonthTo() {
		return monthTo;
	}

	/**
	 * @param monthTo the monthTo to set
	 */
	public void setMonthTo(Integer monthTo) {
		this.monthTo = monthTo;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof SubsidyDTO)) {
			 return false;
		 }
		 SubsidyDTO subsidyDTO = (SubsidyDTO) arg0;
		 return getId() != null && getId().equals(subsidyDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
