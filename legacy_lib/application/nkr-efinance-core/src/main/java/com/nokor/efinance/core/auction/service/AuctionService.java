package com.nokor.efinance.core.auction.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;

/**
 * Auction Service Interface
 * @author bunlong.taing
 */
public interface AuctionService extends BaseEntityService {
	
	/**
	 * Change the Auction status of the contract
	 * @param contract
	 * @param auctionStatus
	 */
	void changeAuctionStatus (Contract contract, EWkfStatus auctionStatus);
	
	/**
	 * Get insurance balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getRealInsuranceBalance (Date calculDate, List<Cashflow> cashflows);
	
	/**
	 * Get insurance balance
	 * @param calculDate
	 * @param cotraId
	 * @return
	 */
	Amount getRealInsuranceBalance (Date calculDate, Long cotraId);
	
	/**
	 * Get Service income balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getRealServiceIncomeBalance (Date calculDate, List<Cashflow> cashflows);
	
	/**
	 * Get Service income balance
	 * @param calculDate
	 * @param cotraId
	 * @return
	 */
	Amount getRealServiceIncomeBalance (Date calculDate, Long cotraId);
	
	/**
	 * Get the number of day from repossess
	 * @param contract
	 * @return
	 */
	Long getDayFromRepossess (Contract contract);
	
	/**
	 * Get the date of repossess
	 * @param contract
	 * @return
	 */
	Date getDateOfRepossess (Contract contract);
	
}
