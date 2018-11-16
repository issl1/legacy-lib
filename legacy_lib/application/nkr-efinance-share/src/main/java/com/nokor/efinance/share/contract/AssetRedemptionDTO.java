package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetRedemptionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8540808539365613324L;
	
	private Long id;
	private Long assetId;
	private Date foreclosureDate;
	
	
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
	public Long getAssetId() {
		return assetId;
	}
	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	/**
	 * @return the foreclosureDate
	 */
	public Date getForeclosureDate() {
		return foreclosureDate;
	}
	/**
	 * @param foreclosureDate the foreclosureDate to set
	 */
	public void setForeclosureDate(Date foreclosureDate) {
		this.foreclosureDate = foreclosureDate;
	}

}
