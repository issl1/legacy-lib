package com.nokor.ersys.finance.accounting.model;

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
 * A source event can triggers 1 or n account entries 
 *  
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_journal_event_account")
public class JournalEventAccount extends EntityA implements MJournalEventAccount {
	/** */
	private static final long serialVersionUID = -8055972983483023461L;

	private JournalEvent event;
	private Account account;
	private Boolean isDebit;
	private Integer sortIndex;
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jou_eve_acc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the event
	 */
    @ManyToOne
    @JoinColumn(name="jou_eve_id", nullable = false)
	public JournalEvent getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(JournalEvent event) {
		this.event = event;
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
	 * @return the isDebit
	 */
    @Column(name="jou_eve_acc_is_debit", nullable = false)
	public Boolean getIsDebit() {
		return isDebit;
	}

	/**
	 * @param isDebit the isDebit to set
	 */
	public void setIsDebit(Boolean isDebit) {
		this.isDebit = isDebit;
	}

	/**
	 * @return the sortIndex
	 */
	@Column(name="jou_eve_acc_sort_index", nullable = true)
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}
}
