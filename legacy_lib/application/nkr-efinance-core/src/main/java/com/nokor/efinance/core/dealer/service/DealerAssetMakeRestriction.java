package com.nokor.efinance.core.dealer.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.dealer.model.DealerAssetMake;

/**
 * Dealer brand restriction
 * @author uhout.cheng
 */
public class DealerAssetMakeRestriction extends BaseRestrictions<DealerAssetMake> {
	
	/** */
	private static final long serialVersionUID = 8009967855713942844L;
	
	private Long dealerId;

	/**
	 */
	public DealerAssetMakeRestriction() {
		super(DealerAssetMake.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (dealerId != null) {
			addCriterion(Restrictions.eq(DealerAssetMake.DEALER + DOT + DealerAssetMake.ID, dealerId));
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
