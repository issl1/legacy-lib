package com.nokor.ersys.messaging.share.accounting;

import java.math.BigDecimal;
import java.util.Date;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * 
 * @author bunlong.taing
 */
public class AccountLedgerDTO extends BaseEntityDTO {

	/** */
	private static final long serialVersionUID = 6133890055987935059L;
	
	private Long journalEntryId;
	private Long accountId;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal balance;
	private Date when;

	/**
     */
    public AccountLedgerDTO() {
    	
    }

	/**
	 * @return the journalEntryId
	 */
	public Long getJournalEntryId() {
		return journalEntryId;
	}

	/**
	 * @param journalEntryId the journalEntryId to set
	 */
	public void setJournalEntryId(Long journalEntryId) {
		this.journalEntryId = journalEntryId;
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
	 * @return the debit
	 */
	public BigDecimal getDebit() {
		return debit;
	}

	/**
	 * @param debit the debit to set
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	/**
	 * @return the credit
	 */
	public BigDecimal getCredit() {
		return credit;
	}

	/**
	 * @param credit the credit to set
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the when
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}
    
}
