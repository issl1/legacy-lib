package com.nokor.efinance.core.dealer.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;

/**
 * Dealer account holder restriction
 * @author uhout.cheng
 */
public class DealerAccountHolderRestriction extends BaseRestrictions<DealerAccountHolder> {
	
	/** */
	private static final long serialVersionUID = 7346938002494032001L;
	
	private Dealer dealer;
	private Long accountHolderId;

	/**
	 */
	public DealerAccountHolderRestriction() {
		super(DealerAccountHolder.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (dealer != null) {
			addCriterion(Restrictions.eq(DealerAccountHolder.DEALER, dealer));
		}
		if (accountHolderId != null) {
			addCriterion(Restrictions.eq(DealerAccountHolder.ACCOUNTHOLDERID, accountHolderId));
		}
	}

	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the accountHolderId
	 */
	public Long getAccountHolderId() {
		return accountHolderId;
	}

	/**
	 * @param accountHolderId the accountHolderId to set
	 */
	public void setAccountHolderId(Long accountHolderId) {
		this.accountHolderId = accountHolderId;
	}
	
}
