package com.nokor.efinance.core.applicant.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.Driver;

/**
 * 
 * @author uhout.cheng
 */
public interface DriverInformationService extends BaseEntityService {

	/**
	 * 
	 * @param driver
	 */
	void saveOrUpdateDriverAddress(Driver driver);
}
