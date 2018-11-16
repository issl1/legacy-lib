package com.nokor.efinance.core.contract.service.cashflow;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.financial.model.EServiceType;

/**
 * Cashflow service interface
 * @author mao.heng
 *
 */
public interface CashflowService extends BaseEntityService {

	/**
	 * Get cash flow list from database
	 * @param criteria Criteria
	 * @return List of cash flow
	 */
	List<Cashflow> getListCashflow(BaseRestrictions<Cashflow> criteria);
	
	/**
	 * Save or Update a cashflow
	 * @param cashflow
	 * @return
	 */
	Cashflow saveOrUpdateCashflow(Cashflow cashflow);
	
	/**
	 * Delete an cashflow
	 * @param cashflow
	 */
	void deleteCashflow(Cashflow cashflow);
	
	/**
	 * @param cotraId
	 * @return
	 */
	List<Cashflow> getOfficialCashflowsToPaid(Long cotraId);

	/**
	 * @param cotraId
	 * @param installmentDate
	 * @return
	 */
	List<Cashflow> getCashflowsToPaid(Long cotraId, Date installmentDate);
		
	/**
	 * 
	 * @param cotraId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Cashflow> getCashflowsToPaid(Long cotraId, Date startDate, Date endDate);
	
	/**
	 * 
	 * @param cotraId
	 * @param today
	 * @return
	 */
	List<Cashflow> getCashflowsToPaidLessThanToday(Long cotraId, Date today);
	
	/**
	 * @param cotraId
	 * @return
	 */
	List<Cashflow> getCashflowsToPaid(Long cotraId);
	
	/**
	 * @param contraId
	 * @param numPrepaidTerm
	 * @return
	 */
	List<Cashflow> getCashflowsToPaid(Long contraId, Integer numPrepaidTerm);
	
	/**
	 * @param cotraId
	 * @return
	 */
	List<Cashflow> getNativeCashflowsNoCancel(Long cotraId);
	
	/**
	 * @param cotraId
	 * @param serviId
	 * @return
	 */
	List<Cashflow> getServiceCashflowsOfContract(Long cotraId, Long serviId);
	
	/**
	 * 
	 * @param contraId
	 * @param installmentDate
	 * @return
	 */
	List<TransactionVO> getDueTransactions(Long contraId, Date installmentDate);
	
	/**
	 * 
	 * @param contraId
	 * @param installmentDate
	 * @return
	 */
	List<TransactionVO> getFeePenaltyTransaction(Long contraId, Date installmentDate);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	List<Cashflow> getCashflowCollectionFee(Contract contract);
	
	/**
	 * 
	 * @param contract
	 * @param serviceType
	 * @return
	 */
	Cashflow getServiceTypeCashflowOfContract(Contract contract, EServiceType serviceType);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	Cashflow getLoanOrganization(Contract contract);
	
	/**
	 * @param cashflows
	 * @return
	 */
	Cashflow getTotalCommissionCashflow(List<Cashflow> cashflows);
	
	/**
	 * @param cashflows
	 * @return
	 */
	Cashflow getTotalFinancedCashflow(List<Cashflow> cashflows);
	
}
