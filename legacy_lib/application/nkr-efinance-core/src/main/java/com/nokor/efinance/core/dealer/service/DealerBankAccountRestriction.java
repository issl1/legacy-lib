package com.nokor.efinance.core.dealer.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;

/**
 * Dealer bank account restriction
 * @author uhout.cheng
 */
public class DealerBankAccountRestriction extends BaseRestrictions<DealerBankAccount> {
	
	/** */
	private static final long serialVersionUID = -8418181325874483194L;
	
	private Dealer dealer;
	private Long bankAccountId;

	/**
	 */
	public DealerBankAccountRestriction() {
		super(DealerBankAccount.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (dealer != null) {
			addCriterion(Restrictions.eq(DealerBankAccount.DEALER, dealer));
		}
		if (bankAccountId != null) {
			addCriterion(Restrictions.eq(DealerBankAccount.BANKACCOUNTID, bankAccountId));
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
	 * @return the bankAccountId
	 */
	public Long getBankAccountId() {
		return bankAccountId;
	}

	/**
	 * @param bankAccountId the bankAccountId to set
	 */
	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	
}
