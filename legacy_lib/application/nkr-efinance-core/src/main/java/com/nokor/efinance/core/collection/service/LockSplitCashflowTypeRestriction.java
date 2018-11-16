package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.ELockSplitCashflowType;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class LockSplitCashflowTypeRestriction extends BaseRestrictions<ELockSplitCashflowType> {

	/** */
	private static final long serialVersionUID = 2152149248033459925L;

	private ELockSplitType lockSplitType;
	private ECashflowType cashflowType;
	
	public LockSplitCashflowTypeRestriction() {
			super(ELockSplitCashflowType.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
    	if (lockSplitType != null) {
    		addCriterion(Restrictions.eq(ELockSplitCashflowType.LOCKSPLITTYPE, lockSplitType));
    	}
    	if (cashflowType != null) {
    		addCriterion(Restrictions.eq(ELockSplitCashflowType.CASHFLOWTYPE, cashflowType));
    	}
    }

	/**
	 * @return the lockSplitType
	 */
	public ELockSplitType getLockSplitType() {
		return lockSplitType;
	}

	/**
	 * @param lockSplitType the lockSplitType to set
	 */
	public void setLockSplitType(ELockSplitType lockSplitType) {
		this.lockSplitType = lockSplitType;
	}

	/**
	 * @return the cashflowType
	 */
	public ECashflowType getCashflowType() {
		return cashflowType;
	}

	/**
	 * @param cashflowType the cashflowType to set
	 */
	public void setCashflowType(ECashflowType cashflowType) {
		this.cashflowType = cashflowType;
	}
	
}
