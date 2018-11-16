package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.CampaignDealer;

/**
 * Campaign Dealer Restriction
 * @author bunlong.taing
 */
public class CampaignDealerRestriction extends BaseRestrictions<CampaignDealer> {
	/** */
	private static final long serialVersionUID = 7623527465087082832L;
	
	private Long campaignId;

	/**
	 */
	public CampaignDealerRestriction() {
		super(CampaignDealer.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (campaignId != null) {
			addCriterion(Restrictions.eq(CampaignDealer.CAMPAIGN + DOT + CampaignDealer.ID, campaignId));
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
