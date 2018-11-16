package com.nokor.efinance.share.insurancecampaign;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author uhout.cheng
 */
public class InsuranceCampaignDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -4762394925399566209L;

	private Long id;
	private String code;
	private String descEn;
	private String desc;
	private Date startDate;
	private Date endDate;
	private Integer nbCoverageInYears;
	private Double insuranceFee;
	
	private Long insuranceCompanyId;
	
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
	 * @return the nbCoverageInYears
	 */
	public Integer getNbCoverageInYears() {
		return nbCoverageInYears;
	}
	
	/**
	 * @param nbCoverageInYears the nbCoverageInYears to set
	 */
	public void setNbCoverageInYears(Integer nbCoverageInYears) {
		this.nbCoverageInYears = nbCoverageInYears;
	}
	
	/**
	 * @return the insuranceFee
	 */
	public Double getInsuranceFee() {
		return insuranceFee;
	}
	
	/**
	 * @param insuranceFee the insuranceFee to set
	 */
	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	/**
	 * @return the insuranceCompanyId
	 */
	public Long getInsuranceCompanyId() {
		return insuranceCompanyId;
	}
	
	/**
	 * @param insuranceCompanyId the insuranceCompanyId to set
	 */
	public void setInsuranceCompanyId(Long insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof InsuranceCampaignDTO)) {
			 return false;
		 }
		 InsuranceCampaignDTO insuranceCampaignDTO = (InsuranceCampaignDTO) arg0;
		 return getId() != null && getId().equals(insuranceCampaignDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
