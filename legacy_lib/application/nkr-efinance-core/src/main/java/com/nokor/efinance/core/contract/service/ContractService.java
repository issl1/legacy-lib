package com.nokor.efinance.core.contract.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.contract.model.Transaction;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * Contract service interface
 * @author mao.heng
 *
 */
public interface ContractService extends BaseEntityService {

	/**
	 * @param critieria
	 * @return
	 */
	List<Contract> getListContract(BaseRestrictions<Contract> critieria);
	
	/**
	 * Get contract object by it's reference
	 * @param reference
	 * @return
	 */
	Contract getByReference(String reference);
	
	/**
	 * Get contract object by external reference
	 * @param externalReference
	 * @return
	 */
	Contract getByExternalReference(String externalReference);
	
	/**
	 * Get contract object by FO reference
	 * @param quotaId
	 * @return
	 */
	Contract getByFoReference(Long quotaId);
	
	/**
	 * Get real outstanding of contract
	 * @param calculDate
	 * @param cotraId
	 * @return
	 */
	Amount getRealOutstanding(Date calculDate, Long cotraId);

	/**
	 * Get real outstanding of contract
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getRealOutstanding(Date calculDate, List<Cashflow> cashflows);
	
	/**
	 * Get real outstanding of contract
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getTheoricalOutstanding(Date calculDate, List<Cashflow> cashflows);	
		
	/**
	 * Get interest balance
	 * @param calculDate
	 * @param cotraId
	 * @return
	 */
	Amount getRealInterestBalance(Date calculDate, Long cotraId);
	
	/**
	 * Get interest balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getRealInterestBalance(Date calculDate, List<Cashflow> cashflows);
	
	/**
	 * Get interest balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	Amount getInterestBalance(Date calculDate, List<Cashflow> cashflows);
	
	/**
	 * Get total interest
	 * @param cotraId
	 * @return
	 */
	Amount getTotalInterest(Long cotraId);
	
	/**
	 * Get total interest
	 * @param cashflows
	 * @return
	 */
	Amount getTotalInterest(List<Cashflow> cashflows);
	
	/**
	 * Calculate penalty
	 * @param contract
	 * @param installmentDate
	 * @param paymentDate
	 * @param installmentAmountUsd
	 */
	PenaltyVO calculatePenalty(Contract contract, Date installmentDate, Date paymentDate, double installmentAmountUsd);
	
	/**
	 * @param contract
	 * @param penaltyAmountUsd
	 * @param installmentAmountUsd
	 * @return
	 */
	int calculateOverdueDays(Contract contract, double penaltyAmountUsd, double installmentAmountUsd);
	
	/**
	 * @param cotraId
	 * @return
	 */
	List<Cashflow> getCashflowsNoCancel(Long cotraId);
	
	/**
	 * @param cotraId
	 * @return
	 */
	List<Cashflow> getCashflows(Long cotraId);
	
	/**
	 * @param cotraId
	 * @param simulateDate
	 * @return
	 */
	List<Cashflow> getCashflowsEarlySettlement(Long cotraId, Date simulateDate);
	
	/**
	 * @param cotraId
	 * @return
	 */
	boolean isOneInstallmentAlreadyPaid(Long cotraId);
	
	/**
	 * 
	 * @param contract
	 */
	public void updateContractAndAsset(Contract contract);
	
	/**
	 * @param cotraId
	 * @param firstInstallmentDate
	 */
	void updateInstallmentDate(Long cotraId, Date firstInstallmentDate);
	
	/**
	 * @param cotraId
	 * @param startContractDate
	 */
	void updateOfficialPaymentDate(Quotation quotation, Date startContractDate);
	
	/**
	 * @param contract
	 * @param status
	 */
	void changeContractStatus(Contract contract, EWkfStatus status);
	/**
	 * 
	 * @param contract
	 * @param status
	 * @param date
	 */
	void changeContractStatus(Contract contract, EWkfStatus status, Date date);
	
	/**
	 * @param cotraId
	 * @return
	 */
	Payment getLastPayment(Long cotraId);
	
	/**
	 * @param cotraId
	 * @return
	 */
	Payment getNextPayment(Long cotraId);
	
	/**
	 * @param cotraId
	 * @param installmentDate
	 * @return
	 */
	List<Cashflow> getCashflowsToPaid(Long cotraId, Date installmentDate);
	
	/**
	 * @param cotraId
	 * @return
	 */	
	boolean isPrintedPurchaseOrder(Long cotraId);
	
	/**
	 * @param contract
	 * @return
	 */
	void closeContract(Contract contract);
	
	/**
	 * @param contract
	 * @return
	 */
	void reverseContract(Contract contract);
	
	/**
	 * 
	 * @param contract
	 */
	void withdrawContract(Contract contract);
	
	/**
	 * 
	 * @param calDate
	 * @param cashflows
	 * @return
	 */
	int getNbOverdueInDays(Date calDate, List<Cashflow> cashflows);
	
	/**
	 * 
	 * @param calDate
	 * @param cotraId
	 * @return
	 */
	int getNbOverdueInDays(Date calDate, Long cotraId);
	
	/**
	 * 
	 * @param calDate
	 * @param cashflows
	 * @return
	 */
	int getDebtLevel(Date calDate, List<Cashflow> cashflows);
	
	/**
	 * Unbook/ Unlock a contract
	 * @param user
	 * @param conId
	 */
	void unbookContract(SecUser user, Long conId);
	
	/**
	 * Receive contracts
	 * @param conIds
	 */
	void receiveContracts(List<Long> conIds);
	
	/**
	 * Receive a contract
	 * @param contraId
	 */
	void receiveContract(Long conId);

	/**
	 * Book contracts
	 * @param conIds
	 */
	void bookContracts(List<Long> conIds);
	
	/**
	 * Book a contract
	 * @param contraId
	 */
	void bookContract(Long conId);
	
	/**
	 * Cancel a contract
	 * @param contraId
	 */
	Contract cancelContract(Long conId);
	
	/**
	 * Print a contract
	 * @param contraId
	 */
	void printContract(Long conId);
	
	/**
	 * Transfer contract to another user
	 * @param contraId
	 * @param profile
	 */
	void transfer(Long contraId, SecProfile profile);
	
	/**
	 * Reject a contract
	 * @param contractId
	 */
	void reject(Long contraId);
	
	/**
	 * Hold a contract
	 * @param contractId
	 */
	void hold(Long contraId);
	/**
	 * Cancel Received Contracts
	 * @param conIds
	 */
	void cancelReceivedContracts(List<Long> conIds);
	
	/**
	 * Cancel Received Contract
	 * @param conId
	 */
	void cancelReceivedContract(Long conId);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	List<ContractWkfHistoryItem> getListContractHistories(Contract contract);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	List<Transaction> getTransaction(List<TransactionVO> transactionVOs);
	
	/**
	 * @return
	 */
	List<String> getRemainingOneInstallmentContracts();
	
	/**
	 * 
	 * @param assetId
	 * @return
	 */
	Contract getContractByAssetId(Long assetId);

	/**
	 * @param contract
	 * @return
	 */
	Summary getContractSummary(Contract contract);
}
