package com.nokor.efinance.core.shared.accounting;

import java.io.Serializable;
import java.util.List;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.financial.model.FinService;

public class ServiceCalculation implements Serializable {
	
	private static final long serialVersionUID = -8970486430171391975L;
	
	private FinService service;
	private List<Cashflow> cashflows;
	private double serviceAmountUsd;
	
	/**
	 * @return the service
	 */
	public FinService getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(FinService service) {
		this.service = service;
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
	 * @return the serviceAmountUsd
	 */
	public double getServiceAmountUsd() {
		return serviceAmountUsd;
	}
	/**
	 * @param serviceAmountUsd the serviceAmountUsd to set
	 */
	public void setServiceAmountUsd(double serviceAmountUsd) {
		this.serviceAmountUsd = serviceAmountUsd;
	}	
}
