package com.nokor.efinance.share.campaign;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;


/**
 * 
 * @author uhout.cheng
 */
public class CampaignSerieDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 8599126278536261076L;

	private Long id;
	private UriDTO brand;
	private UriDTO model;
	private UriDTO serie;
	private Double standardFinanceAmount;	
	
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
	 * @return the brand
	 */
	public UriDTO getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(UriDTO brand) {
		this.brand = brand;
	}

	/**
	 * @return the model
	 */
	public UriDTO getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(UriDTO model) {
		this.model = model;
	}

	/**
	 * @return the serie
	 */
	public UriDTO getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(UriDTO serie) {
		this.serie = serie;
	}

	/**
	 * @return the standardFinanceAmount
	 */
	public Double getStandardFinanceAmount() {
		return standardFinanceAmount;
	}

	/**
	 * @param standardFinanceAmount the standardFinanceAmount to set
	 */
	public void setStandardFinanceAmount(Double standardFinanceAmount) {
		this.standardFinanceAmount = standardFinanceAmount;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CampaignSerieDTO)) {
			 return false;
		 }
		 CampaignSerieDTO campaignSerieDTO = (CampaignSerieDTO) arg0;
		 return getId() != null && getId().equals(campaignSerieDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
