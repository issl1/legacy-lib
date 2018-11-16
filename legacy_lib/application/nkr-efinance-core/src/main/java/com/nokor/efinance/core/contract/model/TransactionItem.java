package com.nokor.efinance.core.contract.model;

import java.io.Serializable;

/**
 * 
 * @author buntha.chea
 *
 */
public class TransactionItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3976287886313275256L;
	
	
	private Long id;
	private String description;
	private Double amount;
	private Double balance;
	
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
}
