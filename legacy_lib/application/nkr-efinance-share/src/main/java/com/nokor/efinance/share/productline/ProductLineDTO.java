package com.nokor.efinance.share.productline;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class ProductLineDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -8708171057228402208L;

	private Long id;
	private String descEn;
	private String desc;
	private Long productLineTypeId;
	private Long vatId;
	private Long penaltyRuleId;
	private Long locksplitRuleId;
	private Long guarantorRequirementId;
	private Long collateralRequirementId;
	private Long referenceRequirementId;
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
	 * @return the productLineTypeId
	 */
	public Long getProductLineTypeId() {
		return productLineTypeId;
	}
	
	/**
	 * @param productLineTypeId the productLineTypeId to set
	 */
	public void setProductLineTypeId(Long productLineTypeId) {
		this.productLineTypeId = productLineTypeId;
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
		 if (arg0 == null || !(arg0 instanceof ProductLineDTO)) {
			 return false;
		 }
		 ProductLineDTO productLineDTO = (ProductLineDTO) arg0;
		 return getId() != null && getId().equals(productLineDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
