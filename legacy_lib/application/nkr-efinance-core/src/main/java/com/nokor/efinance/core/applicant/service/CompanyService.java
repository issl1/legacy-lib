package com.nokor.efinance.core.applicant.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.CompanyContactInfo;

/**
 * Individual service interface
 * @author uhout.cheng
 */
public interface CompanyService extends BaseEntityService {

	/**
	 * Save or update company contact info
	 * @param companyContactInfo
	 */
	void saveOrUpdateCompanyContactInfo(CompanyContactInfo companyContactInfo);
	
}
