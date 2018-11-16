package com.nokor.efinance.share.contract.withdraw;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class WithdrawContractResponseDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 3349401080642860633L;
	
	private String contractID;
	private String status;
	
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}		
	
}
