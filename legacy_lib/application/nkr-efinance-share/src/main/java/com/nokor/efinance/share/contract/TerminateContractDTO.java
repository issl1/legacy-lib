package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class TerminateContractDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 7204396602460580679L;

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
