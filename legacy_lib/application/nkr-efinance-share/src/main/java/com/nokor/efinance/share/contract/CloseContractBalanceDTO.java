package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class CloseContractBalanceDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 7981285941548985931L;

	private String contractID;
	private Double intBalance;
	private Double outstandingBalance;
	private Double totalBalance;
	
	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}
	
	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the intBalance
	 */
	public Double getIntBalance() {
		return intBalance;
	}

	/**
	 * @param intBalance the intBalance to set
	 */
	public void setIntBalance(Double intBalance) {
		this.intBalance = intBalance;
	}

	/**
	 * @return the outstandingBalance
	 */
	public Double getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * @param outstandingBalance the outstandingBalance to set
	 */
	public void setOutstandingBalance(Double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	/**
	 * @return the totalBalance
	 */
	public Double getTotalBalance() {
		return totalBalance;
	}

	/**
	 * @param totalBalance the totalBalance to set
	 */
	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}	
	
}
