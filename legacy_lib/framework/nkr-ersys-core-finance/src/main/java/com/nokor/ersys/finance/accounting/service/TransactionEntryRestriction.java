package com.nokor.ersys.finance.accounting.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;

/**
 * 
 * @author prasnar
 *
 */
public class TransactionEntryRestriction extends BaseRestrictions<TransactionEntry> {
	/** */
	private static final long serialVersionUID = 7523043853502262131L;

	private EWkfStatus wkfStatus;
	
	/**
	 */
	public TransactionEntryRestriction() {
		super(TransactionEntry.class);
	}
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
   }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {  	
		if (wkfStatus != null) {
			addCriterion(Restrictions.eq(JournalEntry.WKFSTATUS, wkfStatus));
		}
    }

	/**
	 * @return the wkfStatus
	 */
	public EWkfStatus getWkfStatus() {
		return wkfStatus;
	}

	/**
	 * @param wkfStatus the wkfStatus to set
	 */
	public void setWkfStatus(EWkfStatus wkfStatus) {
		this.wkfStatus = wkfStatus;
	}
}
