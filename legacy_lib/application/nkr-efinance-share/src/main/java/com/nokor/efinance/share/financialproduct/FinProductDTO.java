package com.nokor.efinance.share.financialproduct;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author uhout.cheng
 */
public class FinProductDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -3341138309357338731L;

	private Long id;
	private String code;
	private String desc;
	private String descEn;
	private Long productLineId;
	private Date startDate;
	private Date endDate;
	private Integer maxFirstPaymentDay;
	private Integer numberOfPrincipalGracePeriods;
	private Integer term;
	private Double periodicInterestRate;
	private Double advancePaymentPercentage;
	private Long frequencyId;
	private Long guarantorRequirementId;
	private Long collateralRequirementId;
	private Long referenceRequirementId;
	private Long vatId;
	private Long penaltyRuleId;
	private Long locksplitRuleId;
	private Double minAdvancePaymentPercentage;
	private List<FinProductServiceDTO> finProductServiceDTOs;
	private Long roundingFormatId;
	
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
	 * @return the productLineId
	 */
	public Long getProductLineId() {
		return productLineId;
	}

	/**
	 * @param productLineId the productLineId to set
	 */
	public void setProductLineId(Long productLineId) {
		this.productLineId = productLineId;
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
	 * @return the maxFirstPaymentDay
	 */
	public Integer getMaxFirstPaymentDay() {
		return maxFirstPaymentDay;
	}
	
	/**
	 * @param maxFirstPaymentDay the maxFirstPaymentDay to set
	 */
	public void setMaxFirstPaymentDay(Integer maxFirstPaymentDay) {
		this.maxFirstPaymentDay = maxFirstPaymentDay;
	}
	
	/**
	 * @return the numberOfPrincipalGracePeriods
	 */
	public Integer getNumberOfPrincipalGracePeriods() {
		return numberOfPrincipalGracePeriods;
	}
	
	/**
	 * @param numberOfPrincipalGracePeriods the numberOfPrincipalGracePeriods to set
	 */
	public void setNumberOfPrincipalGracePeriods(
			Integer numberOfPrincipalGracePeriods) {
		this.numberOfPrincipalGracePeriods = numberOfPrincipalGracePeriods;
	}
	
	/**
	 * @return the term
	 */
	public Integer getTerm() {
		return term;
	}
	
	/**
	 * @param term the term to set
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	
	/**
	 * @return the periodicInterestRate
	 */
	public Double getPeriodicInterestRate() {
		return periodicInterestRate;
	}
	
	/**
	 * @param periodicInterestRate the periodicInterestRate to set
	 */
	public void setPeriodicInterestRate(Double periodicInterestRate) {
		this.periodicInterestRate = periodicInterestRate;
	}
	
	/**
	 * @return the advancePaymentPercentage
	 */
	public Double getAdvancePaymentPercentage() {
		return advancePaymentPercentage;
	}
	
	/**
	 * @param advancePaymentPercentage the advancePaymentPercentage to set
	 */
	public void setAdvancePaymentPercentage(Double advancePaymentPercentage) {
		this.advancePaymentPercentage = advancePaymentPercentage;
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
	 * @return the frequencyId
	 */
	public Long getFrequencyId() {
		return frequencyId;
	}

	/**
	 * @param frequencyId the frequencyId to set
	 */
	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	/**
	 * @return the guarantorRequirementId
	 */
	public Long getGuarantorRequirementId() {
		return guarantorRequirementId;
	}

	/**
	 * @param guarantorRequirementId the guarantorRequirementId to set
	 */
	public void setGuarantorRequirementId(Long guarantorRequirementId) {
		this.guarantorRequirementId = guarantorRequirementId;
	}

	/**
	 * @return the collateralRequirementId
	 */
	public Long getCollateralRequirementId() {
		return collateralRequirementId;
	}

	/**
	 * @param collateralRequirementId the collateralRequirementId to set
	 */
	public void setCollateralRequirementId(Long collateralRequirementId) {
		this.collateralRequirementId = collateralRequirementId;
	}

	/**
	 * @return the referenceRequirementId
	 */
	public Long getReferenceRequirementId() {
		return referenceRequirementId;
	}

	/**
	 * @param referenceRequirementId the referenceRequirementId to set
	 */
	public void setReferenceRequirementId(Long referenceRequirementId) {
		this.referenceRequirementId = referenceRequirementId;
	}

	/**
	 * @return the vatId
	 */
	public Long getVatId() {
		return vatId;
	}
	
	/**
	 * @param vatId the vatId to set
	 */
	public void setVatId(Long vatId) {
		this.vatId = vatId;
	}
	
	/**
	 * @return the penaltyRuleId
	 */
	public Long getPenaltyRuleId() {
		return penaltyRuleId;
	}
	
	/**
	 * @param penaltyRuleId the penaltyRuleId to set
	 */
	public void setPenaltyRuleId(Long penaltyRuleId) {
		this.penaltyRuleId = penaltyRuleId;
	}
	
	/**
	 * @return the locksplitRuleId
	 */
	public Long getLocksplitRuleId() {
		return locksplitRuleId;
	}
	
	/**
	 * @param locksplitRuleId the locksplitRuleId to set
	 */
	public void setLocksplitRuleId(Long locksplitRuleId) {
		this.locksplitRuleId = locksplitRuleId;
	}
	
	/**
	 * @return the minAdvancePaymentPercentage
	 */
	public Double getMinAdvancePaymentPercentage() {
		return minAdvancePaymentPercentage;
	}
	
	/**
	 * @param minAdvancePaymentPercentage the minAdvancePaymentPercentage to set
	 */
	public void setMinAdvancePaymentPercentage(Double minAdvancePaymentPercentage) {
		this.minAdvancePaymentPercentage = minAdvancePaymentPercentage;
	}	
	
	/**
	 * @return the finProductServiceDTOs
	 */
	public List<FinProductServiceDTO> getFinProductServiceDTOs() {
		return finProductServiceDTOs;
	}
	
	/**
	 * @param finProductServiceDTOs the finProductServiceDTOs to set
	 */
	public void setFinProductServiceDTOs(
			List<FinProductServiceDTO> finProductServiceDTOs) {
		this.finProductServiceDTOs = finProductServiceDTOs;
	}

	/**
	 * @return the roundingFormatId
	 */
	public Long getRoundingFormatId() {
		return roundingFormatId;
	}

	/**
	 * @param roundingFormatId the roundingFormatId to set
	 */
	public void setRoundingFormatId(Long roundingFormatId) {
		this.roundingFormatId = roundingFormatId;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof FinProductDTO)) {
			 return false;
		 }
		 FinProductDTO finProductDTO = (FinProductDTO) arg0;
		 return getId() != null && getId().equals(finProductDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
