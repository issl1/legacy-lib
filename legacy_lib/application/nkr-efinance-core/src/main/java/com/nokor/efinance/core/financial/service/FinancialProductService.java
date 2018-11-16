package com.nokor.efinance.core.financial.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinService;

/**
 * Financial product service interface
 * @author uhout.cheng
 */
public interface FinancialProductService extends BaseEntityService {

	/**
	 * Create a new financial product
	 * @param finProduct
	 * @return
	 */
	FinProduct createFinProduct(FinProduct finProduct);
	
	/**
	 * Delete financial product
	 * @param finProduct
	 * @return
	 */
	void deleteFinProduct(FinProduct finProduct);
	
	/**
	 * @param serviceType
	 * @return
	 */
	FinService getFinServiceByType(EServiceType serviceType);
}
