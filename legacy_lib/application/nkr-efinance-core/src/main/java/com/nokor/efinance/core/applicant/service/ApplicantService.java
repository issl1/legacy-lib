package com.nokor.efinance.core.applicant.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantArc;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;

/**
 * Applicant service interface
 * 
 * @author ly.youhort
 *
 */
public interface ApplicantService extends BaseEntityService {

	/**
	 * Identify customer to known if he/she exists already in customer database
	 * @param lastName
	 * @param firstName
	 * @param dateOfbirth
	 * @return
	 */
	List<Applicant> identify(String lastName, String firstName, Date dateOfbirth);
	
	/**
	 * @param customer
	 * @return
	 */
	Applicant saveOrUpdateApplicant(Applicant applicant);
	
	/**
	 * @param applicant
	 * @return
	 */
	ApplicantArc saveOrUpdateApplicantArc(ApplicantArc applicant);
		
	/**
	 * Check Guarantor
	 * @param guarantor
	 * @throws ValidationFieldsException
	 */
	void checkGuarantor(Applicant guarantor) throws ValidationFieldsException;
	
	/**
	 * Check Customer
	 * @param customer
	 * @throws ValidationFieldsException
	 */
	void checkCustomer(Applicant customer) throws ValidationFieldsException;
		
	/**
	 * @param appliId
	 * @return
	 */
	boolean isMaxQuotationAuthorisedReach(Long appliId);
	
	/**
	 * @param applicant
	 * @return
	 */
	List<Contract> getContractsByApplicant(Applicant applicant);
	
	/**
	 * @param applicant
	 * @return
	 */
	List<Contract> getContractsGuarantorByApplicant(Applicant applicant);
	
	/**
	 * Get an applicant by applicant category & individual/company id
	 * @param applicantCategory
	 * @param id
	 * @return
	 */
	Applicant getApplicantCategory(EApplicantCategory applicantCategory, Long id);
	
	/**
	 * Add new guarantor
	 * @param applicant
	 * @param contract
	 */
	void createNewGuarantor(Applicant applicant, Contract contract);
	
	/**
	 * 
	 * @param applicant
	 */
	void deleteApplicant(Applicant applicant);
}
