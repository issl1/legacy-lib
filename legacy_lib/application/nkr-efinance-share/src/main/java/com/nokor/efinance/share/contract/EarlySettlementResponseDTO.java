package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * @author youhort.ly
 *
 */
public class EarlySettlementResponseDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 1364471317654472069L;

	private String contractID;
	private ContractBalanceDTO contractBalance;
		
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
	 * @return the contractBalance
	 */
	public ContractBalanceDTO getContractBalance() {
		return contractBalance;
	}
	/**
	 * @param contractBalance the contractBalance to set
	 */
	public void setContractBalance(ContractBalanceDTO contractBalance) {
		this.contractBalance = contractBalance;
	}		
}
