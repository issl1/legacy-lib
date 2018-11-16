package com.nokor.efinance.core.shared.contract;

import java.io.Serializable;

import org.seuksa.frmk.tools.amount.Amount;

/**
 * @author ly.youhort
 */
public class PenaltyVO implements Serializable {
	
	private static final long serialVersionUID = -1707012181106240683L;
	
	private Integer numPenaltyDays = null;
	private Integer numOverdueDays = null;
	private Amount penaltyAmount = null;
	
	
	/**
	 * @return the numOverdueDays
	 */
	public Integer getNumOverdueDays() {
		return numOverdueDays;
	}
	/**
	 * @param numOverdueDays the numOverdueDays to set
	 */
	public void setNumOverdueDays(Integer numOverdueDays) {
		this.numOverdueDays = numOverdueDays;
	}
	/**
	 * @return the numPenaltyDays
	 */
	public Integer getNumPenaltyDays() {
		return numPenaltyDays;
	}
	/**
	 * @param numPenaltyDays the numPenaltyDays to set
	 */
	public void setNumPenaltyDays(Integer numPenaltyDays) {
		this.numPenaltyDays = numPenaltyDays;
	}
	/**
	 * @return the penaltyAmount
	 */
	public Amount getPenaltyAmount() {
		return penaltyAmount;
	}
	/**
	 * @param penaltyAmount the penaltyAmount to set
	 */
	public void setPenaltyAmount(Amount penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}
}
