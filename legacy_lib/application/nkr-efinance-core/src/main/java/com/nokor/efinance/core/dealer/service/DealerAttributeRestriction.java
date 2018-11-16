package com.nokor.efinance.core.dealer.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.dealer.model.DealerAttribute;

/**
 * Dealer Attribute Restriction
 * @author bunlong.taing
 */
public class DealerAttributeRestriction extends BaseRestrictions<DealerAttribute> {
	/** */
	private static final long serialVersionUID = -13692609696601280L;

	private Long dealerId;
	private AssetMake assetMake;
	private AssetCategory assetCategory;
	
	/**
	 * DealerAttributeRestriction Constructor
	 */
	public DealerAttributeRestriction() {
		super(DealerAttribute.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (dealerId != null) {
			addCriterion(Restrictions.eq(DealerAttribute.DEALER + DOT + DealerAttribute.ID, dealerId));
		}
		if (assetMake != null) {
			addCriterion(Restrictions.eq(DealerAttribute.ASSETMAKE, assetMake));
		}
		if (assetCategory != null) {
			addCriterion(Restrictions.eq(DealerAttribute.ASSETCATEGORY, assetCategory));
		}
	}

	/**
	 * @return the dealerId
	 */
	public Long getDealerId() {
		return dealerId;
	}

	/**
	 * @param dealerId the dealerId to set
	 */
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	/**
	 * @return the assetMake
	 */
	public AssetMake getAssetMake() {
		return assetMake;
	}

	/**
	 * @param assetMake the assetMake to set
	 */
	public void setAssetMake(AssetMake assetMake) {
		this.assetMake = assetMake;
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

	
	
}
