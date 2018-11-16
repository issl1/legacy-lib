package com.nokor.efinance.share.minimum.interest;

import java.io.Serializable;

import com.nokor.efinance.share.asset.AssetCategoryDTO;
import com.nokor.efinance.share.term.TermDTO;

/**
 * Minimum Interest DTO
 * @author bunlong.taing
 */
public class MinimumInterestDTO implements Serializable {
	/** */
	private static final long serialVersionUID = -807764934124057581L;
	
	private Long id;
	private AssetCategoryDTO assetCategory;
	private TermDTO term;
	private Double minimumInterestAmount;
	
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
	 * @return the term
	 */
	public TermDTO getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(TermDTO term) {
		this.term = term;
	}
	
	/**
	 * @return the minimumInterestAmount
	 */
	public Double getMinimumInterestAmount() {
		return minimumInterestAmount;
	}
	
	/**
	 * @param minimumInterestAmount the minimumInterestAmount to set
	 */
	public void setMinimumInterestAmount(Double minimumInterestAmount) {
		this.minimumInterestAmount = minimumInterestAmount;
	}

}
