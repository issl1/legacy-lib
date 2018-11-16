package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.CampaignAssetMake;

/**
 * Campaign Asset Make Restriction
 * @author bunlong.taing
 */
public class CampaignAssetMakeRestriction extends BaseRestrictions<CampaignAssetMake> {
	/** */
	private static final long serialVersionUID = 8064115316728543474L;
	
	private Long campaignId;

	/**
	 */
	public CampaignAssetMakeRestriction() {
		super(CampaignAssetMake.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (campaignId != null) {
			addCriterion(Restrictions.eq(CampaignAssetMake.CAMPAIGN + DOT + CampaignAssetMake.ID, campaignId));
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
