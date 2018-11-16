package com.nokor.efinance.core.applicant.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantRestriction;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.EmploymentArc;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualAddressArc;
import com.nokor.efinance.core.applicant.model.IndividualArc;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualContactInfoArc;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfoArc;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfoArc;
import com.nokor.efinance.core.applicant.model.IndividualSpouse;
import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.efinance.core.applicant.service.ApplicantSequenceImpl;
import com.nokor.efinance.core.applicant.service.IndividualService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.SequenceManager;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.shared.applicant.ApplicantEntityField;
import com.nokor.efinance.core.shared.exception.ValidationFields;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * IndividualServiceImpl service
 * @author ly.youhort
 *
 */
@Service("individualService")
public class IndividualServiceImpl extends BaseEntityServiceImpl implements IndividualService, ApplicantEntityField, FinServicesHelper {
	
	private static final long serialVersionUID = 1727783235457978721L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
		
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#identify(java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public List<Individual> identify(String lastName, String firstName, Date dateOfbirth) {		
		BaseRestrictions<Individual> restrictions = new BaseRestrictions<>(Individual.class);
		restrictions.addCriterion(Restrictions.ilike(LAST_NAME_EN, lastName, MatchMode.ANYWHERE));
		restrictions.addCriterion(Restrictions.ilike(FIRST_NAME_EN, firstName, MatchMode.ANYWHERE));
		restrictions.addCriterion(Restrictions.eq(BIRTH_DATE, dateOfbirth));
		restrictions.addOrder(Order.asc(LAST_NAME_EN));
		restrictions.addOrder(Order.asc(FIRST_NAME));
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateIndividual(com.nokor.efinance.core.applicant.model.Individual)
	 */
	@Override
	public Individual saveOrUpdateIndividual(Individual individual) {		
		LOG.debug("[>> saveOrUpdateIndividual]");		
		Assert.notNull(individual, "Individual could not be null.");	
		if (individual != null && StringUtils.isEmpty(individual.getReference())) {
			Long sequence = SequenceManager.getInstance().getSequenceApplicant();
			SequenceGenerator sequenceGenerator = new ApplicantSequenceImpl(sequence);
			individual.setReference(sequenceGenerator.generate());
		}
		saveOrUpdate(individual);		
		saveOrUpdateAddresses(individual);
		saveOrUpdateEmployments(individual);
		saveOrUpdateContactInfos(individual);
		saveOrUpdateReferenceInfos(individual);
		saveOrUpdateIndividualSpouse(individual);
		saveOrUpdate(individual);
		LOG.debug("[<< saveOrUpdateApplicant]");
		return individual;
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateIndividualReference(com.nokor.efinance.core.applicant.model.Individual)
	 */
	@Override
	public Individual saveOrUpdateIndividualReference(Individual individual) {
		LOG.debug("[>> saveOrUpdateApplicantReference]");
		
		Assert.notNull(individual, "Applicant could not be null.");
			
		saveOrUpdateReferenceInfos(individual);
		
		LOG.debug("[<< saveOrUpdateApplicantReference]");
		
		return individual;
	}
	
	/**	
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateIndividualArc(com.nokor.efinance.core.applicant.model.IndividualArc)
	 */
	@Override
	public IndividualArc saveOrUpdateIndividualArc(IndividualArc individual) {
		
		LOG.debug("[>> saveOrUpdateIndividualArc]");
		
		Assert.notNull(individual, "Individual could not be null.");
		
		LOG.debug("Update applicant = " + individual.getFirstNameEn() + " " + individual.getLastNameEn());
		
		saveOrUpdate(individual);
		saveOrUpdateAddressesArc(individual);
		saveOrUpdateEmploymentsArc(individual);
		saveOrUpdateContactInfosArc(individual);
		saveOrUpdateReferenceInfosArc(individual);
		LOG.debug("[<< saveOrUpdateIndividualArc]");
		
		return individual;
	}
	
	/**
	 * Delete an Individual
	 * @param individual
	 */
	public void deleteIndividual(Individual individual) {		
		List<Employment> employments = individual.getEmployments();
		
		if (employments != null) {
			for (Employment employment : employments) {
				deleteEmployment(employment);
			}
		}
		
		List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
		
		if (individualAddresses != null) {
			for (IndividualAddress individualAddress : individualAddresses) {
				delete(individualAddress);
				delete(individualAddress.getAddress());
			}
		}
		
		List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();
		if (individualContactInfos != null) {
			for (IndividualContactInfo individualContactInfo : individualContactInfos) {
				delete(individualContactInfo);
				delete(individualContactInfo.getContactInfo());
			}
		}
		
		List<IndividualReferenceInfo> individualReferenceInfos = individual.getIndividualReferenceInfos();
		if (individualReferenceInfos != null) {
			for (IndividualReferenceInfo reference : individualReferenceInfos) {
				deleteReference(reference);
			}
		}
		
		ApplicantRestriction restrictions = new ApplicantRestriction();
        restrictions.setIndividualId(individual.getId());
        List<Applicant> applicants = list(restrictions);
        if (applicants != null && !applicants.isEmpty()) {
            for (Applicant applicant : applicants) {
                APP_SRV.deleteApplicant(applicant);
            }
        }
		
		delete(individual);		
	}
	
	/**
	 * 
	 */
	public void deleteEmployment(Employment employment) {
		delete(employment);
		if (employment.getAddress() != null) {
			delete(employment.getAddress());
		}
	}
	
	/**
	 * Delete a reference
	 * @param reference
	 */
	public void deleteReference(IndividualReferenceInfo reference) {
		List<IndividualReferenceContactInfo> individualReferenceContactInfos = reference.getIndividualReferenceContactInfos();
		if (individualReferenceContactInfos != null) {
			for (IndividualReferenceContactInfo contactInfo : individualReferenceContactInfos) {
				delete(contactInfo);
				delete(contactInfo.getContactInfo());
			}
		}
		
		delete(reference);
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#deleteContactInfo(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteContactInfo(Long indId, Long conId) {
		Individual individual = getById(Individual.class, indId);
		if (individual == null) {
			String errMsg = "Individual [" + indId + "]";
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		} else {
			List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();
			if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
				for (IndividualContactInfo individualContactInfo : individualContactInfos) {
					if (individualContactInfo.getContactInfo().getId().equals(conId)) {
						delete(individualContactInfo);
						delete(individualContactInfo.getContactInfo());
						break;
					}
				}
			}
		}	
	}
	
	/**(non-Javadoc)
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#deleteContactInfo(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteContactInfo(Long indId, Long refId, Long conId) {
		Individual individual = getById(Individual.class, indId);
		if (individual == null) {
			String errMsg = "Individual [" + indId + "]";
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		} else {
			List<IndividualReferenceInfo> referenceInfos = individual.getIndividualReferenceInfos();
			IndividualReferenceInfo indRefInfo = null;
			if (referenceInfos != null && !referenceInfos.isEmpty()) {
				for (IndividualReferenceInfo referenceInfo : referenceInfos) {
					if (refId != null && refId.equals(referenceInfo.getId())) {
						indRefInfo = referenceInfo;
						break;
					}
				}
			}
			if (indRefInfo == null) {
				String errMsg = "Individual-ReferenceInfo [" + refId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				List<IndividualReferenceContactInfo> indRefContactInfos = indRefInfo.getIndividualReferenceContactInfos();
				if (indRefContactInfos != null && !indRefContactInfos.isEmpty()) {
					for (IndividualReferenceContactInfo indRefContactInfo : indRefContactInfos) {
						if (indRefContactInfo.getContactInfo().getId().equals(conId)) {
							delete(indRefContactInfo);
							delete(indRefContactInfo.getContactInfo());
							break;
						}
					}
				}
			}
		}	
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#deleteContactAddress(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteContactAddress(Long indId, Long adrId) {
		Individual individual = getById(Individual.class, indId);
		if (individual == null) {
			String errMsg = "Individual [" + indId + "]";
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		} else {
			List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
			if (individualAddresses != null && !individualAddresses.isEmpty()) {
				for (IndividualAddress individualAddress : individualAddresses) {
					if (individualAddress.getAddress().getId().equals(adrId)) {
						delete(individualAddress);
						delete(individualAddress.getAddress());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Save or Update address
	 * @param applicant
	 */
	private void saveOrUpdateAddressesArc(IndividualArc individual) {
		LOG.debug("[>> saveOrUpdateAddressesArc]");
		List<IndividualAddressArc> individualAddresses = individual.getIndividualAddresses();		
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			for (IndividualAddressArc applicantAddress : individualAddresses) {
				saveOrUpdate(applicantAddress.getAddress());
				applicantAddress.setIndividual(individual);
				saveOrUpdate(applicantAddress);
			}
		}		
		LOG.debug("[<< saveOrUpdateAddressesArc]");
	}
	
	/**
	 * Save or Update contact info
	 * @param applicant
	 */
	private void saveOrUpdateContactInfosArc(IndividualArc individual) {
		LOG.debug("[>> saveOrUpdateContactInfosArc]");
		List<IndividualContactInfoArc> individualContactInfos = individual.getIndividualContactInfos();	
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfoArc applicantContactInfo : individualContactInfos) {
				saveOrUpdate(applicantContactInfo.getContactInfo());
				applicantContactInfo.setIndividual(individual);
				saveOrUpdate(applicantContactInfo);
			}
		}		
		LOG.debug("[<< saveOrUpdateContactInfosArc]");
	}
	
	/**
	 * Save or Update contact info
	 * @param individual
	 */
	private void saveOrUpdateReferenceInfosArc(IndividualArc individual) {
		LOG.debug("[>> saveOrUpdateReferenceInfosArc]");
		List<IndividualReferenceInfoArc> individualReferenceInfos = individual.getIndividualReferenceInfos();		
		if (individualReferenceInfos != null && !individualReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfoArc individualReferenceInfo : individualReferenceInfos) {
				List<IndividualReferenceContactInfoArc> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();				
				individualReferenceInfo.setIndividual(individual);
				saveOrUpdate(individualReferenceInfo);
				if (individualReferenceContactInfos != null && !individualReferenceContactInfos.isEmpty()) {
					for (IndividualReferenceContactInfoArc individualReferenceContactInfo : individualReferenceContactInfos) {
						saveOrUpdate(individualReferenceContactInfo.getContactInfo());
						individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
						saveOrUpdate(individualReferenceContactInfo);
					}				
				}
			}
		}
		LOG.debug("[<< saveOrUpdateReferenceInfosArc]");
	}
	
	/**
	 * Save or Update address
	 * @param applicant
	 */
	private void saveOrUpdateAddresses(Individual individual) {
		LOG.debug("[>> saveOrUpdateAddresses]");
		List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();		
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			for (IndividualAddress individualAddress : individualAddresses) {
				if (CrudAction.DELETE.equals(individualAddress.getCrudAction())) {
					delete(individualAddress);
					if (CrudAction.DELETE.equals(individualAddress.getAddress())) {
						delete(individualAddress.getAddress());
					}
				} else {
					saveOrUpdate(individualAddress.getAddress());
					individualAddress.setIndividual(individual);
					saveOrUpdate(individualAddress);
				}
			}
		}
		
		LOG.debug("[>> saveOrUpdateAddresses]");
	}
	
	/**
	 * Save or Update employments
	 * @param applicant
	 */
	private void saveOrUpdateEmployments(Individual individual) {
		LOG.debug("[>> saveOrUpdateEmployments]");
		List<Employment> employments = individual.getEmployments();				
		if (employments != null && !employments.isEmpty()) {
			for (Employment employment : employments) {
				if (CrudAction.DELETE.equals(employment.getCrudAction())) {
					delete(employment);
					if (employment.getAddress() != null) {
						delete(employment.getAddress());
					}
				} else {
					Address address = employment.getAddress();
					if (address != null) {
						if (employment.isSameApplicantAddress()) {
							Address applicantAddress = individual.getMainAddress();
							if (applicantAddress != null) {
								address.setStreet(applicantAddress.getStreet());
								address.setProvince(applicantAddress.getProvince());
								address.setDistrict(applicantAddress.getDistrict());
								address.setCommune(applicantAddress.getCommune());
								address.setVillage(applicantAddress.getVillage());
							}
						}
						saveOrUpdate(address);
					}
					employment.setIndividual(individual);
					saveOrUpdate(employment);
				}
			}
		}
		LOG.debug("[<< saveOrUpdateEmployments]");
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateEmployment(com.nokor.efinance.core.applicant.model.Employment)
	 */
	@Override
	public void saveOrUpdateEmployment(Employment employment) {
		if (employment.getAddress() != null) {
			saveOrUpdate(employment.getAddress());
		}
		saveOrUpdate(employment);
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateIndividualContactInfo(com.nokor.efinance.core.applicant.model.IndividualContactInfo)
	 */
	@Override
	public void saveOrUpdateIndividualContactInfo(IndividualContactInfo individualContactInfo) {
		if (individualContactInfo != null) {
			saveOrUpdate(individualContactInfo.getContactInfo());
			saveOrUpdate(individualContactInfo);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateReferenceContactInfo(com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo)
	 */
	@Override
	public void saveOrUpdateReferenceContactInfo(IndividualReferenceContactInfo individualReferenceContactInfo) {
		if (individualReferenceContactInfo != null) {
			saveOrUpdate(individualReferenceContactInfo.getContactInfo());
			saveOrUpdate(individualReferenceContactInfo);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateIndividualAddress(com.nokor.efinance.core.applicant.model.IndividualAddress)
	 */
	@Override
	public void saveOrUpdateIndividualAddress(IndividualAddress individualAddress) {
		if (individualAddress != null) {
			saveOrUpdate(individualAddress.getAddress());
			saveOrUpdate(individualAddress);
		} 
	}
	
	/**
	 * Save or Update contact info
	 * @param applicant
	 */
	private void saveOrUpdateContactInfos(Individual individual) {
		LOG.debug("[>> saveOrUpdateContactInfos]");
		List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();		
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo individualContactInfo : individualContactInfos) {
				if (CrudAction.DELETE.equals(individualContactInfo.getCrudAction())) {
					delete(individualContactInfo);
					delete(individualContactInfo.getContactInfo());
				} else {
					saveOrUpdate(individualContactInfo.getContactInfo());
					individualContactInfo.setIndividual(individual);
					saveOrUpdate(individualContactInfo);
				}
			}
		}
		
		LOG.debug("[>> saveOrUpdateContactInfos]");
	}
	
	/**
	 * Save or Update contact info
	 * @param applicant
	 */
	private void saveOrUpdateReferenceInfos(Individual individual) {
		LOG.debug("[>> saveOrUpdateReferenceInfos]");
		List<IndividualReferenceInfo> individualReferenceInfos = individual.getIndividualReferenceInfos();		
		if (individualReferenceInfos != null && !individualReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
				List<IndividualReferenceContactInfo> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();
				if (CrudAction.DELETE.equals(individualReferenceInfo.getCrudAction())) {
					delete(individualReferenceInfo);
					for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
						delete(individualReferenceContactInfo);
						delete(individualReferenceContactInfo.getContactInfo());
					}
				} else {
					for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
						saveOrUpdate(individualReferenceContactInfo.getContactInfo());
						individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
						saveOrUpdate(individualReferenceContactInfo);
					}
					individualReferenceInfo.setIndividual(individual);
					saveOrUpdate(individualReferenceInfo);
				}
			}
		}
		
		LOG.debug("[>> saveOrUpdateReferenceInfos]");
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#saveOrUpdateReferenceInfo(com.nokor.efinance.core.applicant.model.IndividualReferenceInfo)
	 */
	@Override
	public void saveOrUpdateReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
		saveOrUpdate(individualReferenceInfo);
		for (IndividualReferenceContactInfo indRefContactInfo : individualReferenceInfo.getIndividualReferenceContactInfos()) {
			indRefContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
			saveOrUpdate(indRefContactInfo.getContactInfo());
			if (!checkExistedIndRefContactInfo(individualReferenceInfo.getId(), indRefContactInfo.getContactInfo().getId())) {
				saveOrUpdate(indRefContactInfo);
			}
		}
	}
	
	/**
	 * 
	 * @param indRefInfoId
	 * @param contactInfoId
	 * @return
	 */
	private boolean checkExistedIndRefContactInfo(Long indRefInfoId, Long contactInfoId) {
		BaseRestrictions<IndividualReferenceContactInfo> restrictions = new BaseRestrictions<>(IndividualReferenceContactInfo.class);
		restrictions.addCriterion(Restrictions.eq("individualReferenceInfo.id", indRefInfoId));
		restrictions.addCriterion(Restrictions.eq("contactInfo.id", contactInfoId));
		if (!list(restrictions).isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param individual
	 */
	private void saveOrUpdateIndividualSpouse(Individual individual) {
		LOG.debug("[>> saveOrUpdateIndividualSpouse]");
		List<IndividualSpouse> spouses = individual.getIndividualSpouses();
		if (spouses != null) {
			for (IndividualSpouse spouse : spouses) {
				if (CrudAction.DELETE.equals(spouse.getCrudAction())) {
					delete(spouse);
				} else {
					spouse.setIndividual(individual);
					saveOrUpdate(spouse);
				}
			}
		}
		LOG.debug("[<< saveOrUpdateIndividualSpouse]");
	}
	
	/**
	 * Save or Update employments
	 * @param individual
	 */
	private void saveOrUpdateEmploymentsArc(IndividualArc individual) {
		LOG.debug("[>> saveOrUpdateEmployments]");
		List<EmploymentArc> employments = individual.getEmployments();				
		if (employments != null && !employments.isEmpty()) {
			for (EmploymentArc employment : employments) {
				if (CrudAction.DELETE.equals(employment.getCrudAction())) {
					delete(employment);
					if (employment.getAddress() != null) {
						delete(employment.getAddress());
					}
				} else {
					if (employment.getAddress() != null) {
						saveOrUpdate(employment.getAddress());
					}
					employment.setIndividual(individual);
					saveOrUpdate(employment);
				}
			}
		}
		LOG.debug("[<< saveOrUpdateEmployments]");
	}
	
	/**
	 * Check Customer
	 * @param customer
	 * @throws ValidationFieldsException
	 */
	public void checkIndividual(Individual individual) throws ValidationFieldsException {
		checkIndividual(individual, DomainType.CUS);
	}
	
	/**
	 * Check Applicant
	 * @param individual
	 * @throws ValidationFieldsException
	 */
	public void checkIndividual(Individual individual, DomainType domainType) throws ValidationFieldsException {
				
		String fieldRequired1 = "field.required.1";
		
		ValidationFields validationFields = new ValidationFields();
		
		validationFields.addRequired(StringUtils.isEmpty(individual.getFirstName()), domainType, fieldRequired1, I18N.message("firstname"));
		validationFields.addRequired(StringUtils.isEmpty(individual.getFirstNameEn()), domainType, fieldRequired1, I18N.message("firstname.en"));
		validationFields.addRequired(StringUtils.isEmpty(individual.getLastName()), domainType, fieldRequired1, I18N.message("lastname"));
		validationFields.addRequired(StringUtils.isEmpty(individual.getLastNameEn()), domainType, fieldRequired1, I18N.message("lastname.en"));
		validationFields.addRequired(individual.getBirthDate() == null, domainType, fieldRequired1, I18N.message("dateofbirth"));
		validationFields.addRequired(individual.getCivility() == null, domainType, fieldRequired1, I18N.message("civility"));
		validationFields.addRequired(individual.getGender() == null, domainType, fieldRequired1, I18N.message("gender"));
		validationFields.addRequired(individual.getMaritalStatus() == null, domainType, fieldRequired1, I18N.message("marital.status"));
		validationFields.addRequired(individual.getNationality() == null, domainType, fieldRequired1, I18N.message("nationality"));
		// TODO YLY
		// validationFields.addRequired(StringUtils.isEmpty(applicant.getMobilePhone()), domainType, fieldRequired1, I18N.message("mobile.phone"));
		validationFields.addRequired(StringUtils.isEmpty(individual.getConvenientVisitTime()), domainType, fieldRequired1, I18N.message("convenient.time.for.visit"));
		
		Address customerAddress = individual.getMainAddress();
		if (customerAddress != null) {
			// validationFields.addRequired(StringUtils.isEmpty(customerAddress.getHouseNo()), domainType, fieldRequired1, I18N.message("house.no"));
			// validationFields.addRequired(StringUtils.isEmpty(customerAddress.getStreet()), domainType, fieldRequired1, I18N.message("street"));
			validationFields.addRequired(customerAddress.getTimeAtAddressInYear() == null, domainType, fieldRequired1, I18N.message("time.at.address"));
			validationFields.addRequired(customerAddress.getTimeAtAddressInMonth() == null, domainType, fieldRequired1, I18N.message("time.at.address"));
			validationFields.addRequired(customerAddress.getProperty() == null, domainType, fieldRequired1, I18N.message("housing"));
			validationFields.addRequired(customerAddress.getCountry() == null, domainType, fieldRequired1, I18N.message("country"));
			validationFields.addRequired(customerAddress.getProvince() == null, domainType, fieldRequired1, I18N.message("province"));
			validationFields.addRequired(customerAddress.getDistrict() == null, domainType, fieldRequired1, I18N.message("district"));
			validationFields.addRequired(customerAddress.getCommune() == null, domainType, fieldRequired1, I18N.message("commune"));
			validationFields.addRequired(customerAddress.getVillage() == null, domainType, fieldRequired1, I18N.message("village"));
		} else {
			validationFields.addRequired(true, domainType, fieldRequired1, I18N.message("address"));
		}
		
		Employment employment = individual.getCurrentEmployment();
		if (employment != null) {
			validationFields.addRequired(StringUtils.isEmpty(employment.getPosition()), domainType, fieldRequired1, I18N.message("position"));
			validationFields.addRequired(employment.getEmploymentStatus() == null, domainType, fieldRequired1, I18N.message("employment.status"));
			validationFields.addRequired(employment.getEmploymentIndustry() == null, domainType, fieldRequired1, I18N.message("employment.industry"));
			validationFields.addRequired(MyNumberUtils.getDouble(employment.getRevenue()) < 0, domainType, fieldRequired1, I18N.message("revenue"));
			validationFields.addRequired(employment.getTimeWithEmployerInYear() == null, domainType, fieldRequired1, I18N.message("time.with.employer"));
			validationFields.addRequired(employment.getTimeWithEmployerInMonth() == null, domainType, fieldRequired1, I18N.message("time.with.employer"));
			// validationFields.addRequired(MyNumberUtils.getDouble(employment.getBusinessExpense()) < 0, domainType, fieldRequired1, I18N.message("business.expense"));
			validationFields.addRequired(StringUtils.isEmpty(employment.getEmployerName()), domainType, fieldRequired1, I18N.message("employer"));
			
			Address employmentAddress = employment.getAddress();
			String prefix = I18N.message("employment") + " ";
			if (customerAddress != null) {
				// validationFields.addRequired(StringUtils.isEmpty(employmentAddress.getHouseNo()), domainType, fieldRequired1, prefix + I18N.message("house.no"));
				// validationFields.addRequired(StringUtils.isEmpty(employmentAddress.getStreet()), domainType, fieldRequired1, prefix + I18N.message("street"));
				validationFields.addRequired(employmentAddress.getCountry() == null, domainType, fieldRequired1, prefix + I18N.message("country"));
				validationFields.addRequired(employmentAddress.getProvince() == null, domainType, fieldRequired1, prefix + I18N.message("province"));
				validationFields.addRequired(employmentAddress.getDistrict() == null, domainType, fieldRequired1, prefix + I18N.message("district"));
				validationFields.addRequired(employmentAddress.getCommune() == null, domainType, fieldRequired1, prefix + I18N.message("commune"));
				validationFields.addRequired(employmentAddress.getVillage() == null, domainType, fieldRequired1, prefix + I18N.message("village"));
			} else {
				validationFields.addRequired(true, domainType, fieldRequired1, prefix + I18N.message("address"));
			}
		} else {
			validationFields.addRequired(true, domainType, fieldRequired1, I18N.message("current.employment"));
		}
		
		if (!validationFields.getErrorMessages().isEmpty()) {
			throw new ValidationFieldsException(validationFields.getErrorMessages());
		}
	}

	
	/**
	 * @param birthDate
	 * @return
	 */
	public boolean isIndividualOver18Years(Date birthDate) {
		int individualAge = 0;
		if (birthDate != null) {
			individualAge = DateUtils.getAge(birthDate);
		}
		return individualAge >= 18 ;
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#getIndividualAddresses(java.lang.Long)
	 */
	@Override
	public List<IndividualAddress> getIndividualAddresses(Long indId) {
		BaseRestrictions<IndividualAddress> restrictions = new BaseRestrictions<>(IndividualAddress.class);
		restrictions.addCriterion(Restrictions.eq(MApplicant.INDIVIDUAL + "." + ID, indId));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#getIndividualReferenceInfos(java.lang.Long)
	 */
	@Override
	public List<IndividualReferenceInfo> getIndividualReferenceInfos(Long indId) {
		BaseRestrictions<IndividualReferenceInfo> restrictions = new BaseRestrictions<>(IndividualReferenceInfo.class);
		restrictions.addCriterion(Restrictions.eq(MApplicant.INDIVIDUAL + "." + ID, indId));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#getIndividualContactInfos(java.lang.Long)
	 */
	@Override
	public List<IndividualContactInfo> getIndividualContactInfos(Long indId) {
		BaseRestrictions<IndividualContactInfo> restrictions = new BaseRestrictions<>(IndividualContactInfo.class);
		restrictions.addCriterion(Restrictions.eq(MApplicant.INDIVIDUAL + "." + ID, indId));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#getContractsByIndividual(java.lang.Long)
	 */
	@Override
	public List<Contract> getContractsByIndividual(Long indId) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("app.individual", "ind", JoinType.INNER_JOIN);	
		restrictions.addCriterion(Restrictions.eq("ind.id", indId));
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.IndividualService#getContractsGuarantorByIndividual(java.lang.Long)
	 */
	@Override
	public List<Contract> getContractsGuarantorByIndividual(Long indId) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("contractApplicants", "conapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("conapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("conapp.applicantType", EApplicantType.G));
		restrictions.addAssociation("app.individual", "ind", JoinType.INNER_JOIN);	
		restrictions.addCriterion(Restrictions.eq("ind.id", indId));
		return list(restrictions);
	}
	
	/**
	 * 
	 */
	@Override
	public IndividualContactInfo getIndividualContactInfoByAddressType(Long indId, ETypeAddress typeAddress) {
		BaseRestrictions<IndividualContactInfo> restrictions = new BaseRestrictions<>(IndividualContactInfo.class);
		restrictions.addAssociation("contactInfo", "conIn", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq(MApplicant.INDIVIDUAL + "." + ID, indId));
		restrictions.addCriterion(Restrictions.eq("conIn.typeAddress", typeAddress));
		List<IndividualContactInfo> individualContactInfos = list(restrictions);
		if (!individualContactInfos.isEmpty()) {
			return individualContactInfos.get(0);
		}
		
		return null;
	}

}
