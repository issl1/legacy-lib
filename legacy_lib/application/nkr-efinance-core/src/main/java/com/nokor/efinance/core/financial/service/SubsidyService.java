package com.nokor.efinance.core.financial.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public interface SubsidyService extends BaseEntityService {
	
	/**
	 * saveOrUpdate subsidy
	 * @param assetRange
	 */
	void saveOrUpdateSubsidy(ManufacturerSubsidy manufacturerSubsidy);

}
