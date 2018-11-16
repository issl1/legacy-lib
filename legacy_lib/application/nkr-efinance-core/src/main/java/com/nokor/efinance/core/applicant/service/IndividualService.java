package com.nokor.efinance.core.applicant.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualArc;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * Individual service interface
 * 
 * @author ly.youhort
 *
 */
public interface IndividualService extends BaseEntityService {

	/**
	 * Identify customer to known if he/she exists already in customer database
	 * @param lastName
	 * @param firstName
	 * @param dateOfbirth
	 * @return
	 */
	List<Individual> identify(String lastName, String firstName, Date dateOfbirth);
	
	/**
	 * Save or Update a customer
	 * @param customer
	 * @return
	 */
	Individual saveOrUpdateIndividual(Individual individual);
	
	/**
	 * Save or Update a customer reference information
	 * @param individual
	 * @return
	 */
	Individual saveOrUpdateIndividualReference(Individual individual);
	
	/**
	 * @param individual
	 * @return
	 */
	IndividualArc saveOrUpdateIndividualArc(IndividualArc individual);
		
	/**
	 * Delete an individual
	 * @param individual
	 */
	void deleteIndividual(Individual individual);
	
	/**
	 * Delete an employment
	 * @param employment
	 */
	void deleteEmployment(Employment employment);
	
	/**
	 * Delete a reference
	 * @param reference
	 */
	void deleteReference(IndividualReferenceInfo reference);
	
	/**
	 * Delete a contact info by individual id
	 * @param indId
	 * @param conId
	 */
	void deleteContactInfo(Long indId, Long conId);
	
	/**
	 * 
	 * @param indId
	 * @param refId
	 * @param conId
	 */
	void deleteContactInfo(Long indId, Long refId, Long conId);
	
	/**
	 * Delete a contact address by individual id
	 * @param indId
	 * @param adrId
	 */
	void deleteContactAddress(Long indId, Long adrId);

	/**
	 * Check Individual
	 * @param individual
	 * @throws ValidationFieldsException
	 */
	void checkIndividual(Individual individual) throws ValidationFieldsException;
	
	/**
	 * Check Individual
	 * @param individual
	 * @param domainType
	 * @throws ValidationFieldsException
	 */
	void checkIndividual(Individual individual, DomainType domainType) throws ValidationFieldsException;
	
	/**
	 * @param birthDate
	 * @return
	 */
	boolean isIndividualOver18Years(Date birthDate);
	
	/**
	 * saveOrUpdate Employment 
	 * @param employment
	 */
	void saveOrUpdateEmployment(Employment employment);
	
	/**
	 * Save or Update a customer reference information
	 * @param individualReferenceInfo
	 * @return
	 */
	void saveOrUpdateReferenceInfo(IndividualReferenceInfo individualReferenceInfo);
	
	/**
	 * Save or Update a customer reference information
	 * @param individualReferenceContactInfo
	 * @return
	 */
	void saveOrUpdateReferenceContactInfo(IndividualReferenceContactInfo individualReferenceContactInfo);
	
	/**
	 * Save or update individual contact info
	 * @param individualContactInfo
	 */
	void saveOrUpdateIndividualContactInfo(IndividualContactInfo individualContactInfo);
	
	/**
	 * Save or update individual address
	 * @param individualAddress
	 */
	void saveOrUpdateIndividualAddress(IndividualAddress individualAddress);
	
	/**
	 * Get list individual address by individual id
	 * @param indId
	 * @return
	 */
	List<IndividualAddress> getIndividualAddresses(Long indId);
	
	/**
	 * Get list individual reference info by individual id
	 * @param indId
	 * @return
	 */
	List<IndividualReferenceInfo> getIndividualReferenceInfos(Long indId);
	
	/**
	 * Get list individual contact info by individual id
	 * @param indId
	 * @return
	 */
	List<IndividualContactInfo> getIndividualContactInfos(Long indId);
	
	/**
	 * Get list contracts by individual id
	 * @param indId
	 * @return
	 */
	List<Contract> getContractsByIndividual(Long indId);
	
	/**
	 * Get list contracts guarantor by individual
	 * @param indId
	 * @return
	 */
	List<Contract> getContractsGuarantorByIndividual(Long indId);
	
	/**
	 * 
	 * @param indId
	 * @return
	 */
	IndividualContactInfo getIndividualContactInfoByAddressType(Long indId, ETypeAddress typeAddress);
	
}
