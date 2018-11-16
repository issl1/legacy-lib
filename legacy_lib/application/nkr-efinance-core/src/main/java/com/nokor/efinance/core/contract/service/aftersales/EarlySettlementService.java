package com.nokor.efinance.core.contract.service.aftersales;

import org.seuksa.frmk.service.BaseEntityService;

public interface EarlySettlementService extends BaseEntityService {

	/**
	 * @param request
	 * @return
	 */
	EarlySettlementSimulateResponse simulate(EarlySettlementSimulateRequest request);
	
	/**
	 * @param request
	 * @return
	 */
	EarlySettlementSaveResponse save(EarlySettlementSaveRequest request);
	
	/**
	 * @param request
	 * @return
	 */
	EarlySettlementValidateResponse validate(EarlySettlementValidateRequest request);
	
}
