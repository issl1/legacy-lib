package com.nokor.efinance.share.locksplit.balance;

import java.io.Serializable;

import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;

/**
 * 
 * @author uhout.cheng
 */
public class BalanceByJournalEventDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 8173366122089405725L;
	
	private JournalEventDTO event;
	private String desc;
	private double balance;

	/**
	 * @return the event
	 */
	public JournalEventDTO getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(JournalEventDTO event) {
		this.event = event;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

}