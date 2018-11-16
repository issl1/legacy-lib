package com.nokor.efinance.share.organization;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author uhout.cheng
 */
public class InsuranceAOMDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1103698498166091390L;

	private List<InsuranceSerieDTO> insuranceSeriesDTO;

	/**
	 * @return the insuranceSeriesDTO
	 */
	public List<InsuranceSerieDTO> getInsuranceSeriesDTO() {
		return insuranceSeriesDTO;
	}

	/**
	 * @param insuranceSeriesDTO the insuranceSeriesDTO to set
	 */
	public void setInsuranceSeriesDTO(List<InsuranceSerieDTO> insuranceSeriesDTO) {
		this.insuranceSeriesDTO = insuranceSeriesDTO;
	}
	
}
