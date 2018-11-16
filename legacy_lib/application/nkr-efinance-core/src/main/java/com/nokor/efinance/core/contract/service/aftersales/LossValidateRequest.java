package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;

public class LossValidateRequest implements Serializable {

	private static final long serialVersionUID = 8136176355032037369L;

	private Long cotraId;
	
	private List<Cashflow> cashflows;
	
	private Amount totalPrincipal;
	private Amount totalInterest;
	private Amount totalOther;
	private Amount totalPenalty;
	private Amount insuranceFee;
	private Amount servicingFee;
	private Dealer dealer;
	private Date eventDate;
	
	private EWkfStatus contractStatus;

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

	/**
	 * @return the totalPrincipal
	 */
	public Amount getTotalPrincipal() {
		return totalPrincipal;
	}

	/**
	 * @param totalPrincipal the totalPrincipal to set
	 */
	public void setTotalPrincipal(Amount totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	/**
	 * @return the totalInterest
	 */
	public Amount getTotalInterest() {
		return totalInterest;
	}

	/**
	 * @param totalInterest the totalInterest to set
	 */
	public void setTotalInterest(Amount totalInterest) {
		this.totalInterest = totalInterest;
	}

	/**
	 * @return the totalOther
	 */
	public Amount getTotalOther() {
		return totalOther;
	}

	/**
	 * @param totalOther the totalOther to set
	 */
	public void setTotalOther(Amount totalOther) {
		this.totalOther = totalOther;
	}

	/**
	 * @return the totalPenalty
	 */
	public Amount getTotalPenalty() {
		return totalPenalty;
	}

	/**
	 * @param totalPenalty the totalPenalty to set
	 */
	public void setTotalPenalty(Amount totalPenalty) {
		this.totalPenalty = totalPenalty;
	}

	/**
	 * @return the insuranceFee
	 */
	public Amount getInsuranceFee() {
		return insuranceFee;
	}

	/**
	 * @param insuranceFee the insuranceFee to set
	 */
	public void setInsuranceFee(Amount insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	/**
	 * @return the servicingFee
	 */
	public Amount getServicingFee() {
		return servicingFee;
	}

	/**
	 * @param servicingFee the servicingFee to set
	 */
	public void setServicingFee(Amount servicingFee) {
		this.servicingFee = servicingFee;
	}

	/**
	 * 
	 * @return dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}
	/**
	 * 
	 * @param dealer
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the contractStatus
	 */
	public EWkfStatus getWkfStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setWkfStatus(EWkfStatus contractStatus) {
		this.contractStatus = contractStatus;
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
}
