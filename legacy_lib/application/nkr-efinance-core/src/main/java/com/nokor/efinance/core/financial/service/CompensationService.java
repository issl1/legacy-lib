package com.nokor.efinance.core.financial.service;

import com.nokor.efinance.core.financial.model.ManufacturerCompensation;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public interface CompensationService {

	/**
	 * 
	 * @param manufacturerCompensation
	 */
	void saveOrUpdateCompensation(ManufacturerCompensation manufacturerCompensation);
}
