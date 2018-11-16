package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.CampaignAssetRange;

/**
 * Campaign Asset Range Restriction
 * @author bunlong.taing
 */
public class CampaignAssetRangeRestriction extends BaseRestrictions<CampaignAssetRange> {
	/** */
	private static final long serialVersionUID = -8374766513579627632L;
	
	private Long campaignId;

	/**
	 */
	public CampaignAssetRangeRestriction() {
		super(CampaignAssetRange.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (campaignId != null) {
			addCriterion(Restrictions.eq(CampaignAssetRange.CAMPAIGN + DOT + CampaignAssetRange.ID, campaignId));
		}
	}

	/**
	 * @return the campaignId
	 */
	public Long getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

}
