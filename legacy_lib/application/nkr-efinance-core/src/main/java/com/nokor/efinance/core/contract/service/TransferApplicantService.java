package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.TransferApplicantSimulateRequest;

/**
 * Transfer Applicant Service
 * @author bunlong.taing
 */
public interface TransferApplicantService extends BaseEntityService {

	/**
	 * @param request
	 * @return
	 */
	Contract simulate(TransferApplicantSimulateRequest request);
	
	/**
	 * @param contract
	 * @param force
	 * @return
	 */
	public Contract validate(Contract contract, boolean forceActivated);
	
	/**
	 * @param contract
	 * @return
	 */
	public Contract cancel(Contract contract);
}
