package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.CampaignAssetModel;

/**
 * CampaignAssetModelRestriction
 * @author bunlong.taing
 */
public class CampaignAssetModelRestriction extends BaseRestrictions<CampaignAssetModel> {
	/** */
	private static final long serialVersionUID = -4026411414296823197L;
	
	private Long campaignId;
	private Long serieId;

	/**
	 */
	public CampaignAssetModelRestriction() {
		super(CampaignAssetModel.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (campaignId != null) {
			addCriterion(Restrictions.eq(CampaignAssetModel.CAMPAIGN + DOT + CampaignAssetModel.ID, campaignId));
		}
		if (serieId != null) {
			addCriterion(Restrictions.eq(CampaignAssetModel.ASSETMODEL + DOT + CampaignAssetModel.ID, serieId));
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

	/**
	 * @return the serieId
	 */
	public Long getSerieId() {
		return serieId;
	}

	/**
	 * @param serieId the serieId to set
	 */
	public void setSerieId(Long serieId) {
		this.serieId = serieId;
	}

}
