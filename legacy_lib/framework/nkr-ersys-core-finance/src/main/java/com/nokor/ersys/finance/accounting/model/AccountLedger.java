package com.nokor.ersys.finance.accounting.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_account_ledger")
public class AccountLedger extends EntityA implements MAccountLedger {
    /** */
	private static final long serialVersionUID = -5985416667504065391L;

	private JournalEntry journalEntry;
    private Account account;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;	// balance = last balance + (debit - credit) 
    private Date when;
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_led_ent_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the journalEntry
	 */
    @ManyToOne
    @JoinColumn(name="jou_en_id", nullable = false)
	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry the journalEntry to set
	 */
	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the account
	 */
    @ManyToOne
    @JoinColumn(name="acc_id", nullable = false)
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
	 * @return the debit
	 */
    @JoinColumn(name="acc_led_ent_debit", nullable = false)
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
    @JoinColumn(name="acc_led_ent_credit", nullable = false)
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
    @JoinColumn(name="acc_led_ent_balance", nullable = false)
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
    @Column(name="acc_led_ent_when", nullable = false)
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
