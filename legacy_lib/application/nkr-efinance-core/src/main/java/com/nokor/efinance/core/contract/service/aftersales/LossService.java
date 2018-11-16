package com.nokor.efinance.core.contract.service.aftersales;

import org.seuksa.frmk.service.BaseEntityService;

public interface LossService extends BaseEntityService {

	/**
	 * @param request
	 * @return
	 */
	LossSimulateResponse simulate(LossSimulateRequest request);
	
	/**
	 * @param request
	 * @return
	 */
	LossSaveResponse save(LossSaveRequest request);
	
	/**
	 * @param request
	 * @return
	 */
	LossValidateResponse validate(LossValidateRequest request);
	
}
