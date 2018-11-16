package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.CampaignAssetCategory;

/**
 * Campaign Asset Make Restriction
 * @author bunlong.taing
 */
public class CampaignAssetCategoryRestriction extends BaseRestrictions<CampaignAssetCategory> {
	
	/**
	 */
	private static final long serialVersionUID = 9169047363837163899L;
	
	private Long campaignId;

	/**
	 */
	public CampaignAssetCategoryRestriction() {
		super(CampaignAssetCategory.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (campaignId != null) {
			addCriterion(Restrictions.eq(CampaignAssetCategory.CAMPAIGN + DOT + CampaignAssetCategory.ID, campaignId));
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
