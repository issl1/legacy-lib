package com.nokor.ersys.finance.accounting.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;

/**
 * 
 * @author bunlong.taing
 */
public class JournalEventAccountRestriction extends BaseRestrictions<JournalEventAccount> {
	/** */
	private static final long serialVersionUID = 1628350433333933420L;
	
	private Long journalId;
	private Long eventId;
	private Long accountId;
	private Boolean isDebit;
	/**
	 */
	public JournalEventAccountRestriction() {
		super(JournalEventAccount.class);
	}
	
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(JournalEventAccount.EVENT, JoinType.INNER_JOIN);
    	addAssociation(JournalEventAccount.ACCOUNT, JoinType.LEFT_OUTER_JOIN);
    }
    
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (journalId != null) {
	    	addAssociation(JournalEventAccount.EVENT + DOT + JournalEvent.JOURNAL, "jour", JoinType.INNER_JOIN);
			addCriterion("jour" + DOT + Journal.ID, journalId);
		}
		if (eventId != null) {
			addCriterion(Restrictions.eq(JournalEventAccount.EVENT + DOT + JournalEvent.ID, eventId));
		}
		if (accountId != null) {
			addCriterion(Restrictions.eq(JournalEventAccount.ACCOUNT + DOT + Account.ID, accountId));
		}
		
		if (isDebit != null) {
			addCriterion(Restrictions.eq(JournalEventAccount.ISDEBIT, accountId));
		}
	}

	/**
	 * @return the journalId
	 */
	public Long getJournalId() {
		return journalId;
	}


	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(Long journalId) {
		this.journalId = journalId;
	}


	/**
	 * @return the eventId
	 */
	public Long getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the isDebit
	 */
	public Boolean getIsDebit() {
		return isDebit;
	}

	/**
	 * @param isDebit the isDebit to set
	 */
	public void setIsDebit(Boolean isDebit) {
		this.isDebit = isDebit;
	}

	

}
