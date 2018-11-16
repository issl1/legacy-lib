package com.nokor.efinance.core.contract.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * 
 * @author buntha.chea
 *
 */
public class Transaction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6782007146649509643L;
	
	private Long id;
	private String transactionId;
	private Date transactionDate;
	private String description;
	private Double amount;
	private Double balance;
	private List<TransactionItem> items;
	private EWkfStatus wkfStatus;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	/**
	 * @return the items
	 */
	public List<TransactionItem> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<TransactionItem> items) {
		this.items = items;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
