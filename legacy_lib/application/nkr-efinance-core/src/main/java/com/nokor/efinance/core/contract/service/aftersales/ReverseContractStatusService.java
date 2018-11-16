package com.nokor.efinance.core.contract.service.aftersales;

import org.seuksa.frmk.service.BaseEntityService;

/**
 * Reverse the contract status to activated
 * 
 * @author meng.kim
 *
 */
public interface ReverseContractStatusService extends BaseEntityService {

	/**
	 * @param request
	 * @return
	 */
	ReverseContractStatusValidateResponse validate(ReverseContractStatusValidateRequest request);
	
}
