package com.nokor.efinance.share.organization;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceLostDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 2350432023189685694L;
	
	private List<InsuranceSerieDTO> insuranceSeriesDTO;
	private List<InsuranceLostClaimDTO> insuranceLostClaimsDTO;
	
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

	/**
	 * @return the insuranceLostClaimsDTO
	 */
	public List<InsuranceLostClaimDTO> getInsuranceLostClaimsDTO() {
		return insuranceLostClaimsDTO;
	}

	/**
	 * @param insuranceLostClaimsDTO the insuranceLostClaimsDTO to set
	 */
	public void setInsuranceLostClaimsDTO(List<InsuranceLostClaimDTO> insuranceLostClaimsDTO) {
		this.insuranceLostClaimsDTO = insuranceLostClaimsDTO;
	}
	
}
