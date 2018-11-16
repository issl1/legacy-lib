package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class LossSimulateResponseDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -3048404778032732123L;

	private String contractID;
	private double totalPrincipal;
	private double totalInterest;
	private double totalOther;
	
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
	 * @return the totalPrincipal
	 */
	public double getTotalPrincipal() {
		return totalPrincipal;
	}
	/**
	 * @param totalPrincipal the totalPrincipal to set
	 */
	public void setTotalPrincipal(double totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}
	/**
	 * @return the totalInterest
	 */
	public double getTotalInterest() {
		return totalInterest;
	}
	/**
	 * @param totalInterest the totalInterest to set
	 */
	public void setTotalInterest(double totalInterest) {
		this.totalInterest = totalInterest;
	}
	/**
	 * @return the totalOther
	 */
	public double getTotalOther() {
		return totalOther;
	}
	/**
	 * @param totalOther the totalOther to set
	 */
	public void setTotalOther(double totalOther) {
		this.totalOther = totalOther;
	}	
}
