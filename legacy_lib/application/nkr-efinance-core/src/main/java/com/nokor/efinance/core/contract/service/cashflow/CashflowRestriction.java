package com.nokor.efinance.core.contract.service.cashflow;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * Cashflow Restriction
 * @author bunlong.taing
 */
public class CashflowRestriction extends BaseRestrictions<Cashflow> {
	/** */
	private static final long serialVersionUID = 4752540957609138338L;
	
	private Long contractId;
	private Long journalEventId;
	private ECashflowType[] cashflowTypes;
	private ETreasuryType[] treasuryTypes;
	
	private boolean excludeCancel = false;
	private boolean excludePaid = false;
	
	/**
	 */
	public CashflowRestriction() {
		super(Cashflow.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (contractId != null) {
			addCriterion(Restrictions.eq(Cashflow.CONTRACT + DOT + Cashflow.ID, contractId));
		}
		if (journalEventId != null) {
			addCriterion(Restrictions.eq(Cashflow.JOURNALEVENT + DOT + JournalEvent.ID, journalEventId));
		}
		if (cashflowTypes != null && cashflowTypes.length > 0) {
			addCriterion(Restrictions.in(Cashflow.CASHFLOWTYPE, cashflowTypes));
		}
		if (treasuryTypes != null && treasuryTypes.length > 0) {
			addCriterion(Restrictions.in(Cashflow.TREASURYTYPE, treasuryTypes));
		}
		
		if (excludeCancel) {
			addCriterion(Restrictions.eq(Cashflow.CANCEL, false));
		}
		if (excludePaid) {
			addCriterion(Restrictions.eq(Cashflow.PAID, false));
		}
	}

	/**
	 * @return the contractId
	 */
	public Long getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the journalEventId
	 */
	public Long getJournalEventId() {
		return journalEventId;
	}

	/**
	 * @param journalEventId the journalEventId to set
	 */
	public void setJournalEventId(Long journalEventId) {
		this.journalEventId = journalEventId;
	}

	/**
	 * @return the cashflowTypes
	 */
	public ECashflowType[] getCashflowTypes() {
		return cashflowTypes;
	}

	/**
	 * @param cashflowTypes the cashflowTypes to set
	 */
	public void setCashflowTypes(ECashflowType[] cashflowTypes) {
		this.cashflowTypes = cashflowTypes;
	}
	
	/**
	 * @return the treasuryTypes
	 */
	public ETreasuryType[] getTreasuryTypes() {
		return treasuryTypes;
	}

	/**
	 * @param treasuryTypes the treasuryTypes to set
	 */
	public void setTreasuryTypes(ETreasuryType[] treasuryTypes) {
		this.treasuryTypes = treasuryTypes;
	}

	/**
	 * @return the excludeCancel
	 */
	public boolean isExcludeCancel() {
		return excludeCancel;
	}

	/**
	 * @param excludeCancel the excludeCancel to set
	 */
	public void setExcludeCancel(boolean excludeCancel) {
		this.excludeCancel = excludeCancel;
	}

	/**
	 * @return the excludePaid
	 */
	public boolean isExcludePaid() {
		return excludePaid;
	}

	/**
	 * @param excludePaid the excludePaid to set
	 */
	public void setExcludePaid(boolean excludePaid) {
		this.excludePaid = excludePaid;
	}

}
