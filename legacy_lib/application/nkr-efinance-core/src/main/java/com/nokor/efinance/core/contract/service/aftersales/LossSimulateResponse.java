package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;

public class LossSimulateResponse implements Serializable {
	
	private static final long serialVersionUID = 4146157822425914186L;
	
	private Long cotraId;
	
	private Amount totalPrincipal;
	private Amount totalInterest;
	private Amount totalOther;
	private Amount insuranceFee;
	private Amount servicingFee;
	private Date eventDate;
	
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
	 * @return
	 */
	public Amount getTotalAmount() {
		Amount totalAmount = new Amount();
		totalAmount.plus(totalPrincipal);
		totalAmount.plus(totalInterest);
		totalAmount.plus(insuranceFee);
		totalAmount.plus(servicingFee);
		totalAmount.plus(totalOther);
		return totalAmount;
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
