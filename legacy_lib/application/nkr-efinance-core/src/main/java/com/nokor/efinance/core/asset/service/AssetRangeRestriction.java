package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;

public class AssetRangeRestriction extends BaseRestrictions<AssetRange> {
	/** */
	private static final long serialVersionUID = -8314627398979986370L;
	
	private String searchText;
	private AssetMake assetMake;
	
	private Long brandId;
	
	/**
	 */
	public AssetRangeRestriction() {
		super(AssetRange.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (StringUtils.isNotEmpty(searchText)) {
			addCriterion(Restrictions.or(Restrictions.ilike(AssetRange.DESC, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(AssetRange.DESCEN, searchText, MatchMode.ANYWHERE)));
		}
		if (assetMake != null) {
			addCriterion(Restrictions.eq(AssetRange.ASSETMAKE, assetMake));
		} else if (brandId != null) {
			addCriterion(Restrictions.eq(AssetRange.ASSETMAKE + DOT + AssetMake.ID, brandId));
		}
	}

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
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
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

}
