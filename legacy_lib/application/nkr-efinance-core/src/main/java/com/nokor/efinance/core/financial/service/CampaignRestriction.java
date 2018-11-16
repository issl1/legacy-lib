package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignDealer;

/**
 * Campaign Restriction
 * @author uhout.cheng
 */
public class CampaignRestriction extends BaseRestrictions<Campaign> {

	/** */
	private static final long serialVersionUID = 7714373677910368734L;

	private static final String DEA = "dea";
	
	private Long dealerId;

	/**
	 */
	public CampaignRestriction() {
		super(Campaign.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (dealerId != null) {
			addAssociation(Campaign.DEALERS, DEA, JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.or(Restrictions.eq(DEA + DOT + CampaignDealer.DEALER + DOT + CampaignDealer.ID, dealerId), 
					Restrictions.eq(Campaign.VALIDFORALLDEALERS, Boolean.TRUE)));
			setDistinctRootEntity(true);
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

}
