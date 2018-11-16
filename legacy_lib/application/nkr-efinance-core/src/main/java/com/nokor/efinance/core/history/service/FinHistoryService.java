package com.nokor.efinance.core.history.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.history.FinHistory;
import com.nokor.efinance.core.history.FinHistoryType;


/**
 * Fin History service
 * @author uhout.cheng
 */
public interface FinHistoryService extends BaseEntityService {
	
	/**
	 * Create new fin history 
	 * @param contract
	 * @param historyType
	 * @param comment
	 */
	void addFinHistory(Contract contract, FinHistoryType historyType, String comment);
	
	/**
	 * Get fin histories by contract id & history type
	 * @param conId
	 * @param historyTypes
	 * @return
	 */
	List<FinHistory> getFinHistories(Long conId, FinHistoryType[] historyTypes);
}

