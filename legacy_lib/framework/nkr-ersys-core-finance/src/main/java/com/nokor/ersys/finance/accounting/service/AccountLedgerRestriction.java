package com.nokor.ersys.finance.accounting.service;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountLedger;

/**
 * @author bunlong.taing
 */
public class AccountLedgerRestriction extends BaseRestrictions<AccountLedger> {
	/** */
	private static final long serialVersionUID = -8586505099775721482L;
	
	private Account account;
	private Date startDate;
	private Date endDate;

	/**
	 */
	public AccountLedgerRestriction() {
		super(AccountLedger.class);
	}
	
	/**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(AccountLedger.ACCOUNT, JoinType.INNER_JOIN);
    }
    
    
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (account != null) {
			addCriterion(Restrictions.eq(AccountLedger.ACCOUNT, account));
		}
		if (startDate != null) {
			addCriterion(Restrictions.ge(AccountLedger.WHEN, DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			addCriterion(Restrictions.le(AccountLedger.WHEN, DateUtils.getDateAtEndOfDay(endDate)));
		}
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
