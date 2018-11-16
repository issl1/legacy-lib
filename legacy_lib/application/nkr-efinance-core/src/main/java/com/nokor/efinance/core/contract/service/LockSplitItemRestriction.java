package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.MLockSplit;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitItemRestriction extends BaseRestrictions<LockSplitItem> implements MLockSplit {
	
	/** */
	private static final long serialVersionUID = -4452009108403253395L;
	
	private Long lockSplitId;
	
	/**
	 * 
	 */
    public LockSplitItemRestriction() {
		super(LockSplitItem.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (lockSplitId != null) {
    		addCriterion(Restrictions.eq(LOCKSPLIT + DOT + ID, lockSplitId));
    	}
	}

	/**
	 * @return the lockSplitId
	 */
	public Long getLockSplitId() {
		return lockSplitId;
	}

	/**
	 * @param lockSplitId the lockSplitId to set
	 */
	public void setLockSplitId(Long lockSplitId) {
		this.lockSplitId = lockSplitId;
	}
}
