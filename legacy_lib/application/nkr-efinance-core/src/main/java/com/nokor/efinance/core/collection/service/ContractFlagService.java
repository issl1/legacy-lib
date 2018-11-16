package com.nokor.efinance.core.collection.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.collection.model.ContractFlag;

/**
 * Asset model service interface
 * @author uhout.cheng
 */
public interface ContractFlagService extends BaseEntityService {

	/**
	 * saveOrUpdate legal case info
	 * @param contractFlag
	 */
	void saveOrUpdateLegalCase(ContractFlag contractFlag);
	
	/**
	 * Withdraw / delete legal case info
	 * @param contractFlag
	 */
	void withdrawLegalCase(ContractFlag contractFlag);
	
}
