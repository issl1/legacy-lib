package com.nokor.efinance.share.common;

import java.io.Serializable;

import com.nokor.ersys.messaging.share.accounting.JournalEventDTO;

public class BalanceAmountDTO implements Serializable {
	/**
	 */
	private static final long serialVersionUID = -5438451546803187237L;
	
	private JournalEventDTO event;
	private Double amount;
	
	/**
	 * 
	 */
	public BalanceAmountDTO() {
		
	}
	
	/**
	 * @param event
	 * @param amount
	 */
	public BalanceAmountDTO(JournalEventDTO event, Double amount) {
		this.event = event;
		this.amount = amount;
	}
	
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
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
