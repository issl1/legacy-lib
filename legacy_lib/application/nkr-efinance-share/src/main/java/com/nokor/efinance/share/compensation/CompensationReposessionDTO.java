package com.nokor.efinance.share.compensation;

import java.io.Serializable;
import java.util.Date;

import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetModelDTO;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class CompensationReposessionDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -8574826789293144324L;
	
	private Long id;
	private Double refundPercentage;
	private Integer fromMonth;
	private Integer toMonth;

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
	 * @return the refundPercentage
	 */
	public Double getRefundPercentage() {
		return refundPercentage;
	}

	/**
	 * @param refundPercentage the refundPercentage to set
	 */
	public void setRefundPercentage(Double refundPercentage) {
		this.refundPercentage = refundPercentage;
	}
	
	/**
	 * @return the fromMonth
	 */
	public Integer getFromMonth() {
		return fromMonth;
	}

	/**
	 * @param fromMonth the fromMonth to set
	 */
	public void setFromMonth(Integer fromMonth) {
		this.fromMonth = fromMonth;
	}

	/**
	 * @return the toMonth
	 */
	public Integer getToMonth() {
		return toMonth;
	}

	/**
	 * @param toMonth the toMonth to set
	 */
	public void setToMonth(Integer toMonth) {
		this.toMonth = toMonth;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CompensationReposessionDTO)) {
			 return false;
		 }
		 CompensationReposessionDTO subsidyDTO = (CompensationReposessionDTO) arg0;
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
