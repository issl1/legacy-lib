package com.nokor.efinance.core.contract.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.EntityCreationException;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.model.Quotation;

/**
 * Activation Contract Service
 * @author bunlong.taing
 */
public interface ActivationContractService extends BaseEntityService {

	/**
	 * @param quotation
	 * @return
	 */
	Contract createContract(Quotation quotation) throws EntityCreationException;
			
	/**
	 * Complete contract
	 * @param contract
	 * @param isForced
	 * @return
	 */
	Contract complete(Contract contract, boolean isForced, boolean disburse) throws DealerPaymentException, JournalEntryException ;
	
	/**
	 * @param contract
	 */
	void unholdDealerPayment(Contract contract) throws DealerPaymentException, JournalEntryException ;
	
	/**
	 * 
	 * @param paymentIds
	 */
	void sendPaymentToAP(List<Long> paymentIds);
	
	/**
	 * 
	 * @param errors
	 * @param chassisNo
	 * @param engineNo
	 * @param contract
	 * @return
	 */
	List<String> validation(Contract contract, Date firstDueDate, String chassisNo, String engineNo, String taxInvoiceNumber);
	
	/**
	 * @param dealer
	 * @param assetModel
	 * @return
	 */
	double getInsurancePremium(Dealer dealer, AssetModel assetModel);
	
	/**
	 * 
	 */
	void updateContractData(Contract contract);
}
