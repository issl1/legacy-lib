package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;

public class EarlySettlementSimulateResponse implements Serializable {
	
	private static final long serialVersionUID = 4146157822425914186L;
	
	private Long cotraId;
	private Date eventDate;
	
	private Amount balanceCapital;
	private Amount balanceInterest;
	private Amount balancePenalty;
	private Amount balanceFollowingFee;
	private Amount balanceRepossessionFee;
	private Amount balanceCollectionFee;
	private Amount balanceOperationFee;
	private Amount balancePressingFee;
	private Amount balanceTransferFee;
	
	private Amount adjustmentInterest;
	
	private List<Cashflow> cashflows;
	
	/**
	 * @return the cotraId
	 */
	public Long getCotraId() {
		return cotraId;
	}
	
	/**
	 * @param cotraId the cotraId to set
	 */
	public void setCotraId(Long cotraId) {
		this.cotraId = cotraId;
	}

	/**
	 * @return the eventDate
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * @return the balanceCapital
	 */
	public Amount getBalanceCapital() {
		return balanceCapital;
	}

	/**
	 * @param balanceCapital the balanceCapital to set
	 */
	public void setBalanceCapital(Amount balanceCapital) {
		this.balanceCapital = balanceCapital;
	}

	/**
	 * @return the balanceInterest
	 */
	public Amount getBalanceInterest() {
		return balanceInterest;
	}

	/**
	 * @param balanceInterest the balanceInterest to set
	 */
	public void setBalanceInterest(Amount balanceInterest) {
		this.balanceInterest = balanceInterest;
	}

	/**
	 * @return the balancePenalty
	 */
	public Amount getBalancePenalty() {
		return balancePenalty;
	}

	/**
	 * @param balancePenalty the balancePenalty to set
	 */
	public void setBalancePenalty(Amount balancePenalty) {
		this.balancePenalty = balancePenalty;
	}

	/**
	 * @return the balanceFollowingFee
	 */
	public Amount getBalanceFollowingFee() {
		return balanceFollowingFee;
	}

	/**
	 * @param balanceFollowingFee the balanceFollowingFee to set
	 */
	public void setBalanceFollowingFee(Amount balanceFollowingFee) {
		this.balanceFollowingFee = balanceFollowingFee;
	}

	/**
	 * @return the balanceRepossessionFee
	 */
	public Amount getBalanceRepossessionFee() {
		return balanceRepossessionFee;
	}

	/**
	 * @param balanceRepossessionFee the balanceRepossessionFee to set
	 */
	public void setBalanceRepossessionFee(Amount balanceRepossessionFee) {
		this.balanceRepossessionFee = balanceRepossessionFee;
	}

	/**
	 * @return the balanceCollectionFee
	 */
	public Amount getBalanceCollectionFee() {
		return balanceCollectionFee;
	}

	/**
	 * @param balanceCollectionFee the balanceCollectionFee to set
	 */
	public void setBalanceCollectionFee(Amount balanceCollectionFee) {
		this.balanceCollectionFee = balanceCollectionFee;
	}

	/**
	 * @return the balanceOperationFee
	 */
	public Amount getBalanceOperationFee() {
		return balanceOperationFee;
	}

	/**
	 * @param balanceOperationFee the balanceOperationFee to set
	 */
	public void setBalanceOperationFee(Amount balanceOperationFee) {
		this.balanceOperationFee = balanceOperationFee;
	}

	/**
	 * @return the balancePressingFee
	 */
	public Amount getBalancePressingFee() {
		return balancePressingFee;
	}

	/**
	 * @param balancePressingFee the balancePressingFee to set
	 */
	public void setBalancePressingFee(Amount balancePressingFee) {
		this.balancePressingFee = balancePressingFee;
	}

	/**
	 * @return the balanceTransferFee
	 */
	public Amount getBalanceTransferFee() {
		return balanceTransferFee;
	}

	/**
	 * @param balanceTransferFee the balanceTransferFee to set
	 */
	public void setBalanceTransferFee(Amount balanceTransferFee) {
		this.balanceTransferFee = balanceTransferFee;
	}

	/**
	 * @return the adjustmentInterest
	 */
	public Amount getAdjustmentInterest() {
		return adjustmentInterest;
	}

	/**
	 * @param adjustmentInterest the adjustmentInterest to set
	 */
	public void setAdjustmentInterest(Amount adjustmentInterest) {
		this.adjustmentInterest = adjustmentInterest;
	}

	/**
	 * @return the cashflows
	 */
	public List<Cashflow> getCashflows() {
		return cashflows;
	}

	/**
	 * @param cashflows the cashflows to set
	 */
	public void setCashflows(List<Cashflow> cashflows) {
		this.cashflows = cashflows;
	}

}
