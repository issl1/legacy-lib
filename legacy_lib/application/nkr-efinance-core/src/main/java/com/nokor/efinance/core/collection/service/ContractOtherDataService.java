package com.nokor.efinance.core.collection.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Contract;

/**
 * Contract Other data service interface
 * @author youhort.ly
 */
public interface ContractOtherDataService extends BaseEntityService {

	/**
	 * @param critieria
	 * @return
	 */
	void calculateOtherDataContracts();
	
	/**
	 * @param contract
	 */
	Contract calculateOtherDataContract(Contract contract);
	
}
