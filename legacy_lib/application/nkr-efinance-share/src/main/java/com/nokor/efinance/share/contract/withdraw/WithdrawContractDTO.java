package com.nokor.efinance.share.contract.withdraw;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class WithdrawContractDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -5900983116132858218L;

	private String contractID;
	
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
}
