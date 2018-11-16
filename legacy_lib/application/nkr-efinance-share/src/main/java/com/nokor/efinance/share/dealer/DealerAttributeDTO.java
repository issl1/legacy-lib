package com.nokor.efinance.share.dealer;

import java.io.Serializable;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.share.asset.AssetBrandDTO;
import com.nokor.efinance.share.asset.AssetCategoryDTO;

/**
 * @author youhort.ly
 */
public class DealerAttributeDTO implements Serializable {
	/**
	 */
	private static final long serialVersionUID = 8005008019439167301L;
	private Long id;
	private AssetBrandDTO brand;
	private AssetCategoryDTO assetCategory;
	private Integer insuranceCoverageDuration;
	private Double tiContractFeeAmount;
	private Double vatContractFeeAmount;
	private Double teContractFeeAmount;
	private RefDataDTO contractFeeChargePoint;
	
	private Double teCommission1Amount;
	private Double vatCommission1Amount;
	private Double tiCommission1Amount;
	private double vatValue;
	
	private RefDataDTO commission1ChargePoint;
		
	
	private boolean commission2Enabled;
	private RefDataDTO ladderOption;
	private LadderTypeDTO ladderType;
	
	private boolean insuranceFeeEnabled;
	private RefDataDTO insuranceFeeChargePoint;
	private UriDTO insuranceCompanies;

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
	public AssetBrandDTO getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(AssetBrandDTO brand) {
		this.brand = brand;
	}

	/**
	 * @return the assetCategory
	 */
	public AssetCategoryDTO getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategoryDTO assetCategory) {
		this.assetCategory = assetCategory;
	}

	/**
	 * @return the insuranceCoverageDuration
	 */
	public Integer getInsuranceCoverageDuration() {
		return insuranceCoverageDuration;
	}

	/**
	 * @param insuranceCoverageDuration the insuranceCoverageDuration to set
	 */
	public void setInsuranceCoverageDuration(Integer insuranceCoverageDuration) {
		this.insuranceCoverageDuration = insuranceCoverageDuration;
	}

	/**
	 * @return the tiContractFeeAmount
	 */
	public Double getTiContractFeeAmount() {
		return tiContractFeeAmount;
	}

	/**
	 * @param tiContractFeeAmount the tiContractFeeAmount to set
	 */
	public void setTiContractFeeAmount(Double tiContractFeeAmount) {
		this.tiContractFeeAmount = tiContractFeeAmount;
	}

	/**
	 * @return the vatContractFeeAmount
	 */
	public Double getVatContractFeeAmount() {
		return vatContractFeeAmount;
	}

	/**
	 * @param vatContractFeeAmount the vatContractFeeAmount to set
	 */
	public void setVatContractFeeAmount(Double vatContractFeeAmount) {
		this.vatContractFeeAmount = vatContractFeeAmount;
	}

	/**
	 * @return the teContractFeeAmount
	 */
	public Double getTeContractFeeAmount() {
		return teContractFeeAmount;
	}

	/**
	 * @param teContractFeeAmount the teContractFeeAmount to set
	 */
	public void setTeContractFeeAmount(Double teContractFeeAmount) {
		this.teContractFeeAmount = teContractFeeAmount;
	}

	/**
	 * @return the contractFeeChargePoint
	 */
	public RefDataDTO getContractFeeChargePoint() {
		return contractFeeChargePoint;
	}

	/**
	 * @param contractFeeChargePoint the contractFeeChargePoint to set
	 */
	public void setContractFeeChargePoint(RefDataDTO contractFeeChargePoint) {
		this.contractFeeChargePoint = contractFeeChargePoint;
	}

	/**
	 * @return the teCommission1Amount
	 */
	public Double getTeCommission1Amount() {
		return teCommission1Amount;
	}

	/**
	 * @param teCommission1Amount the teCommission1Amount to set
	 */
	public void setTeCommission1Amount(Double teCommission1Amount) {
		this.teCommission1Amount = teCommission1Amount;
	}

	/**
	 * @return the vatCommission1Amount
	 */
	public Double getVatCommission1Amount() {
		return vatCommission1Amount;
	}

	/**
	 * @param vatCommission1Amount the vatCommission1Amount to set
	 */
	public void setVatCommission1Amount(Double vatCommission1Amount) {
		this.vatCommission1Amount = vatCommission1Amount;
	}

	/**
	 * @return the tiCommission1Amount
	 */
	public Double getTiCommission1Amount() {
		return tiCommission1Amount;
	}

	/**
	 * @param tiCommission1Amount the tiCommission1Amount to set
	 */
	public void setTiCommission1Amount(Double tiCommission1Amount) {
		this.tiCommission1Amount = tiCommission1Amount;
	}

	/**
	 * @return the vatValue
	 */
	public double getVatValue() {
		return vatValue;
	}

	/**
	 * @param vatValue the vatValue to set
	 */
	public void setVatValue(double vatValue) {
		this.vatValue = vatValue;
	}

	/**
	 * @return the commission1ChargePoint
	 */
	public RefDataDTO getCommission1ChargePoint() {
		return commission1ChargePoint;
	}

	/**
	 * @param commission1ChargePoint the commission1ChargePoint to set
	 */
	public void setCommission1ChargePoint(RefDataDTO commission1ChargePoint) {
		this.commission1ChargePoint = commission1ChargePoint;
	}

	/**
	 * @return the commission2Enabled
	 */
	public boolean isCommission2Enabled() {
		return commission2Enabled;
	}

	/**
	 * @param commission2Enabled the commission2Enabled to set
	 */
	public void setCommission2Enabled(boolean commission2Enabled) {
		this.commission2Enabled = commission2Enabled;
	}

	/**
	 * @return the ladderOption
	 */
	public RefDataDTO getLadderOption() {
		return ladderOption;
	}

	/**
	 * @param ladderOption the ladderOption to set
	 */
	public void setLadderOption(RefDataDTO ladderOption) {
		this.ladderOption = ladderOption;
	}

	/**
	 * @return the ladderType
	 */
	public LadderTypeDTO getLadderType() {
		return ladderType;
	}

	/**
	 * @param ladderType the ladderType to set
	 */
	public void setLadderType(LadderTypeDTO ladderType) {
		this.ladderType = ladderType;
	}

	/**
	 * @return the insuranceFeeEnabled
	 */
	public boolean isInsuranceFeeEnabled() {
		return insuranceFeeEnabled;
	}

	/**
	 * @param insuranceFeeEnabled the insuranceFeeEnabled to set
	 */
	public void setInsuranceFeeEnabled(boolean insuranceFeeEnabled) {
		this.insuranceFeeEnabled = insuranceFeeEnabled;
	}

	/**
	 * @return the insuranceFeeChargePoint
	 */
	public RefDataDTO getInsuranceFeeChargePoint() {
		return insuranceFeeChargePoint;
	}

	/**
	 * @param insuranceFeeChargePoint the insuranceFeeChargePoint to set
	 */
	public void setInsuranceFeeChargePoint(RefDataDTO insuranceFeeChargePoint) {
		this.insuranceFeeChargePoint = insuranceFeeChargePoint;
	}

	/**
	 * @return the insuranceCompanies
	 */
	public UriDTO getInsuranceCompanies() {
		return insuranceCompanies;
	}

	/**
	 * @param insuranceCompanies the insuranceCompanies to set
	 */
	public void setInsuranceCompanies(UriDTO insuranceCompanies) {
		this.insuranceCompanies = insuranceCompanies;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof DealerAttributeDTO)) {
			 return false;
		 }
		 DealerAttributeDTO dealerAttributeDTO = (DealerAttributeDTO) arg0;
		 return getId() != null && getId().equals(dealerAttributeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
