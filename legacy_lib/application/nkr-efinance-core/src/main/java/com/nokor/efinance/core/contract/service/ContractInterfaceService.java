package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;

/**
 * Contract service interface
 * @author ly.youhort
 *
 */
public interface ContractInterfaceService extends BaseEntityService {

	/**
	 * @param quotaId
	 */
	Contract activateDownPaymentContract(Long quotaId);
	
	/**
	 * @param quotation
	 */
	Contract activateDownPaymentContract(Quotation quotation);
	
	
	/**
	 * @param quotation
	 */
	Contract activateContract(Long quotaId);
	
	/**
	 * @param quotation
	 */
	Contract activateContract(Quotation quotation);
	
	/**
	 * @param contractReference
	 * @param directCoseCode
	 * @param directCostAmount
	 */
	void addDirectCost(String contractReference, String directCostCode, Amount directCostAmount);
}
