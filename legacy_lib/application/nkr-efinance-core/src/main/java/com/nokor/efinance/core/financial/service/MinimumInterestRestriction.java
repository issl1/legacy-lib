package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.model.Term;

/**
 * Minimum Interest Restriction
 * @author bunlong.taing
 */
public class MinimumInterestRestriction extends BaseRestrictions<MinimumInterest> {
	/** */
	private static final long serialVersionUID = -3710498720176681942L;
	
	private AssetCategory assetCategory;
	private Term term;
	
	private Long assetCategoryId;
	private Long termId;

	/**
	 */
	public MinimumInterestRestriction() {
		super(MinimumInterest.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (assetCategory != null) {
			addCriterion(Restrictions.eq(MinimumInterest.ASSETCATEGORY, assetCategory));
		} else if (assetCategoryId != null) {
			addCriterion(Restrictions.eq(MinimumInterest.ASSETCATEGORY + DOT + MinimumInterest.ID, assetCategoryId));
		}
		
		if (term != null) {
			addCriterion(Restrictions.eq(MinimumInterest.TERM, term));
		} else if (termId != null) {
			addCriterion(Restrictions.eq(MinimumInterest.TERM + DOT + MinimumInterest.ID, termId));
		}
	}

	/**
	 * @return the assetCategory
	 */
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
	 * @return the assetCategoryId
	 */
	public Long getAssetCategoryId() {
		return assetCategoryId;
	}

	/**
	 * @param assetCategoryId the assetCategoryId to set
	 */
	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	/**
	 * @return the termId
	 */
	public Long getTermId() {
		return termId;
	}

	/**
	 * @param termId the termId to set
	 */
	public void setTermId(Long termId) {
		this.termId = termId;
	}

}
