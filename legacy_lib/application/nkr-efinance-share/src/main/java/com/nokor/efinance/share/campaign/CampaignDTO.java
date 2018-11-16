package com.nokor.efinance.share.campaign;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.common.messaging.share.UriDTO;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 6649628009949950126L;

	private Long id;
	//private String fullDesc;
	private String descEn;
	private String code;
	private Date startDate;
	private Date endDate;
	private Double flatRate;
	private Double maxFlatRate;
	private boolean validForAllDealers;
	private UriDTO area;
	
	private CreditControlDTO creditControl;
	private List<UriDTO> dealers;
	private List<CampaignSerieDTO> series;

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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return the flatRate
	 */
	public Double getFlatRate() {
		return flatRate;
	}
	
	/**
	 * @param flatRate the flatRate to set
	 */
	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}
	
	/**
	 * @return the maxFlatRate
	 */
	public Double getMaxFlatRate() {
		return maxFlatRate;
	}

	/**
	 * @param maxFlatRate the maxFlatRate to set
	 */
	public void setMaxFlatRate(Double maxFlatRate) {
		this.maxFlatRate = maxFlatRate;
	}

	/**
	 * @return the validForAllDealers
	 */
	public boolean isValidForAllDealers() {
		return validForAllDealers;
	}

	/**
	 * @param validForAllDealers the validForAllDealers to set
	 */
	public void setValidForAllDealers(boolean validForAllDealers) {
		this.validForAllDealers = validForAllDealers;
	}
	
	/**
	 * @return the area
	 */
	public UriDTO getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(UriDTO area) {
		this.area = area;
	}

	/**
	 * @return the creditControl
	 */
	public CreditControlDTO getCreditControl() {
		return creditControl;
	}

	/**
	 * @param creditControl the creditControl to set
	 */
	public void setCreditControl(CreditControlDTO creditControl) {
		this.creditControl = creditControl;
	}

	/**
	 * @return the dealers
	 */
	public List<UriDTO> getDealers() {
		return dealers;
	}

	/**
	 * @param dealers the dealers to set
	 */
	public void setDealers(List<UriDTO> dealers) {
		this.dealers = dealers;
	}

	/**
	 * @return the series
	 */
	public List<CampaignSerieDTO> getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(List<CampaignSerieDTO> series) {
		this.series = series;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CampaignDTO)) {
			 return false;
		 }
		 CampaignDTO campaignDTO = (CampaignDTO) arg0;
		 return getId() != null && getId().equals(campaignDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
