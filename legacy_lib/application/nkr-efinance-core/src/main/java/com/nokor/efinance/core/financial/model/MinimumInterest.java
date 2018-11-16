package com.nokor.efinance.core.financial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.asset.model.AssetCategory;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_minimum_interest")
public class MinimumInterest extends EntityA implements MMinimumInterest {
	/** */
	private static final long serialVersionUID = 8497491670986641708L;
	
	private AssetCategory assetCategory;
	private Term term;
	private Double minimumInterestAmount;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "min_int_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }			

    /**
	 * @return the assetCategory
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ass_cat_id")
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}
	
	/**
	 * @return the term
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ter_id")
	public Term getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Term term) {
		this.term = term;
	}	

	/**
	 * @return the minimumInterestAmount
	 */
	@Column(name = "min_int_amount", nullable = true)
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
