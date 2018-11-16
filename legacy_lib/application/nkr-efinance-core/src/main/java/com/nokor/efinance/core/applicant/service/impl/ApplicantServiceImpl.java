package com.nokor.efinance.core.applicant.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.dao.ApplicantDao;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantArc;
import com.nokor.efinance.core.applicant.model.ApplicantRestriction;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.applicant.service.IndividualService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.service.ContractApplicantRestriction;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.ContractSimulationApplicantRestriction;
import com.nokor.efinance.core.contract.service.ContractSimulationRestriction;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.applicant.ApplicantEntityField;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * Applicant service
 * @author ly.youhort
 *
 */
@Service("applicantService")
public class ApplicantServiceImpl extends BaseEntityServiceImpl implements ApplicantService, ApplicantEntityField {
	
	private static final long serialVersionUID = 1727783235457978721L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private ApplicantDao dao;
	
	@Autowired
	private IndividualService individualService;
	
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public ApplicantDao getDao() {
		return dao;
	}
	
	@Override
	public List<Applicant> identify(String lastName, String firstName, Date dateOfbirth) {		
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addCriterion(Restrictions.ilike(LAST_NAME_EN, lastName, MatchMode.ANYWHERE));
		restrictions.addCriterion(Restrictions.ilike(FIRST_NAME_EN, firstName, MatchMode.ANYWHERE));
		restrictions.addCriterion(Restrictions.eq(BIRTH_DATE, dateOfbirth));
		restrictions.addOrder(Order.asc(LAST_NAME_EN));
		restrictions.addOrder(Order.asc(FIRST_NAME));
		return list(restrictions);
	}
	
	@Override
	public Applicant saveOrUpdateApplicant(Applicant applicant) {
		
		LOG.debug("[>> saveOrUpdateApplicant]");
		
		Assert.notNull(applicant, "Applicant could not be null.");
		
		if (applicant.getApplicantCategory().equals(EApplicantCategory.INDIVIDUAL)) {
			Individual individual = individualService.saveOrUpdateIndividual(applicant.getIndividual());
			applicant.setIndividual(individual);
		} else {
			saveOrUpdate(applicant.getCompany());
		}
				
		saveOrUpdate(applicant);
		LOG.debug("[<< saveOrUpdateApplicant]");
		
		return applicant;
	}
	
	@Override
	public ApplicantArc saveOrUpdateApplicantArc(ApplicantArc applicant) {
		
		LOG.debug("[>> saveOrUpdateApplicantArc]");
		
		Assert.notNull(applicant, "Applicant could not be null.");
		
		if (applicant.getApplicantCategory().equals(EApplicantCategory.INDIVIDUAL)) {
			individualService.saveOrUpdateIndividualArc(applicant.getIndividual());
		} else {
			saveOrUpdate(applicant.getCompany());
		}

		saveOrUpdate(applicant);
		LOG.debug("[<< saveOrUpdateApplicantArc]");
		
		return applicant;
	}
	
	/**
	 * Check Guarantor
	 * @param guarantor
	 * @throws ValidationFieldsException
	 */
	public void checkGuarantor(Applicant guarantor) throws ValidationFieldsException {
		individualService.checkIndividual(guarantor.getIndividual(), DomainType.GUA);
	}
	
	/**
	 * Check Customer
	 * @param customer
	 * @throws ValidationFieldsException
	 */
	public void checkCustomer(Applicant customer) throws ValidationFieldsException {
		individualService.checkIndividual(customer.getIndividual(), DomainType.CUS);
	}
		
		
	/**
	 * @param appliId
	 * @return
	 */
	public boolean isMaxQuotationAuthorisedReach(Long appliId) {
		int maxLeaseApplicant = 3;
		SettingConfig settingConfig = getByCode(SettingConfig.class, "max.lease.app");
		if (settingConfig != null) {
			maxLeaseApplicant = Integer.parseInt(settingConfig.getValue());
		}
		int numberOfQuotationApplicant = getNumberOfQuotationApplicant(EApplicantType.C, appliId);
		return numberOfQuotationApplicant >= maxLeaseApplicant;
	}
	
	/**
	 * @param applicantType
	 * @param applicantId
	 * @return
	 */
	private int getNumberOfQuotationApplicant(EApplicantType applicantType, Long appliId) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);;
		restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addCriterion("quoapp.applicantType", applicantType);
		restrictions.addCriterion(Restrictions.eq("app.id", appliId));
		EWkfStatus[] quotationStatus = new EWkfStatus[] {QuotationWkfStatus.DEC, QuotationWkfStatus.REJ, QuotationWkfStatus.REU, QuotationWkfStatus.CAN};
		restrictions.addCriterion(Restrictions.not(Restrictions.in("quotationStatus", quotationStatus)));
		
		List<Quotation> applicantList = list(restrictions);
		return applicantList == null ? 0 : applicantList.size();
	}
	
	/**
	 * @return
	 */
	public List<Contract> getContractsByApplicant(Applicant applicant) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		if (applicant.getApplicantCategory().equals(EApplicantCategory.INDIVIDUAL)) {
			restrictions.addAssociation("app.individual", "ind", JoinType.INNER_JOIN);	
			restrictions.addCriterion(Restrictions.eq("ind.id", applicant.getIndividual().getId()));
		} else {
			restrictions.addAssociation("app.company", "com", JoinType.INNER_JOIN);	
			restrictions.addCriterion(Restrictions.eq("com.id", applicant.getCompany().getId()));
		}
		return list(restrictions);
	}

	@Override
	public List<Contract> getContractsGuarantorByApplicant(Applicant applicant) {		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("contractApplicants", "conapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("conapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("conapp.applicantType", EApplicantType.G));		
		if (applicant.getApplicantCategory().equals(EApplicantCategory.INDIVIDUAL)) {
			restrictions.addAssociation("app.individual", "ind", JoinType.INNER_JOIN);	
			restrictions.addCriterion(Restrictions.eq("ind.id", applicant.getIndividual().getId()));
		} else {
			restrictions.addAssociation("app.company", "com", JoinType.INNER_JOIN);	
			restrictions.addCriterion(Restrictions.eq("com.id", applicant.getCompany().getId()));
		}
		return list(restrictions);
	}

	/**
	 * @see com.nokor.efinance.core.applicant.service.ApplicantService#getApplicantCategory(com.nokor.efinance.core.applicant.model.EApplicantCategory)
	 */
	@Override
	public Applicant getApplicantCategory(EApplicantCategory applicantCategory, Long id) {
		ApplicantRestriction restrictions = new ApplicantRestriction();
		restrictions.setApplicantCategory(applicantCategory);
		if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory)) {
			restrictions.setIndividualId(id);
		} else if (EApplicantCategory.COMPANY.equals(applicantCategory)) {
			restrictions.setCompanyId(id);
		}
		List<Applicant> applicants = list(restrictions);
		if (applicants != null && !applicants.isEmpty()) {
			return applicants.get(0);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.ApplicantService#createNewGuarantor(com.nokor.efinance.core.applicant.model.Applicant, com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void createNewGuarantor(Applicant applicant, Contract contract) {
		create(applicant.getIndividual());
		create(applicant);
		ContractApplicant newGuarantor = ContractApplicant.createInstance(EApplicantType.G);
		newGuarantor.setContract(contract);
		newGuarantor.setApplicant(applicant);
		create(newGuarantor);
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.ApplicantService#deleteApplicant(com.nokor.efinance.core.applicant.model.Applicant)
	 */
	@Override
	public void deleteApplicant(Applicant applicant) {
		if (!checkAppContract(applicant)) {
			List<ContractApplicant> conApps = getContractApplicants(applicant);
			if (conApps != null && !conApps.isEmpty()) {
				for (ContractApplicant conApp : conApps) {
					delete(conApp);
				}
			}
			
			List<ContractSimulationApplicant> simulationApplicants = getContractSimulationApplicant(applicant);
			if (simulationApplicants != null && !simulationApplicants.isEmpty()) {
				for (ContractSimulationApplicant conApp : simulationApplicants) {
					delete(conApp);
				}
			}
		}
		delete(applicant);
	}
	
	/**
	 * 
	 * @param applicant
	 * @return
	 */
	private List<ContractApplicant> getContractApplicants(Applicant applicant) {
		ContractApplicantRestriction restrictions = new ContractApplicantRestriction();
		restrictions.setApplicant(applicant);
		return list(restrictions);
	}
	
	/**
	 * 
	 * @param applicant
	 * @return
	 */
	private List<ContractSimulationApplicant> getContractSimulationApplicant(Applicant applicant) {
		ContractSimulationApplicantRestriction restrictions = new ContractSimulationApplicantRestriction();
		restrictions.setApplicant(applicant);
		return list(restrictions);
	}
	
	/**
	 * 
	 * @param applicant
	 * @return
	 */
	private boolean checkAppContract(Applicant applicant) {
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addCriterion(Restrictions.eq(APPLICANT + "." + ID, applicant.getId()));
		
		ContractSimulationRestriction simulationRestrictions = new ContractSimulationRestriction();
		simulationRestrictions.addCriterion(Restrictions.eq(APPLICANT + "." + ID, applicant.getId()));
		
		if ((list(restrictions) != null && !list(restrictions).isEmpty())
				|| (list(simulationRestrictions) != null && !list(simulationRestrictions).isEmpty())) {
			return true;
		}
		return false;
	}
	
}
