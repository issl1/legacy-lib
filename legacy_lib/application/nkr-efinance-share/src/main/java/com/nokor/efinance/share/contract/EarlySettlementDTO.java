package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

/**
 * @author youhort.ly
 *
 */
public class EarlySettlementDTO implements Serializable {

	/** 
	 */
	private static final long serialVersionUID = 4083921318639809401L;

	private String contractID;
	private Date earlySettlementDate;
	
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
	 * @return the earlySettlementDate
	 */
	public Date getEarlySettlementDate() {
		return earlySettlementDate;
	}
	/**
	 * @param earlySettlementDate the earlySettlementDate to set
	 */
	public void setEarlySettlementDate(Date earlySettlementDate) {
		this.earlySettlementDate = earlySettlementDate;
	}		
}
