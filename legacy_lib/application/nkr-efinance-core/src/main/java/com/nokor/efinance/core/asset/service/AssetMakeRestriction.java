package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetMake;

/**
 * Asset Make Restriction
 * @author bunlong.taing
 */
public class AssetMakeRestriction extends BaseRestrictions<AssetMake> {
	/** */
	private static final long serialVersionUID = 8230547245050968778L;
	
	private String searchText;

	/**
	 */
	public AssetMakeRestriction() {
		super(AssetMake.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (StringUtils.isNotEmpty(searchText)) {
			addCriterion(Restrictions.or(Restrictions.ilike(AssetMake.DESC, searchText, MatchMode.ANYWHERE),
					Restrictions.ilike(AssetMake.DESCEN, searchText, MatchMode.ANYWHERE)));
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

}
