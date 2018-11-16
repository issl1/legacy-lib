package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;

/**
 * Contract Applicant Restriction
 * @author bunlong.taing
 */
public class ContractSimulationApplicantRestriction extends BaseRestrictions<ContractSimulationApplicant> {
	
	/** */
	private static final long serialVersionUID = -2760881575719412396L;

	private Long conSimulationId;
	private Applicant applicant;
	private EApplicantType applicantType;

	/**
	 */
	public ContractSimulationApplicantRestriction() {
		super(ContractSimulationApplicant.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (conSimulationId != null) {
    		addCriterion(Restrictions.eq(ContractSimulationApplicant.CONTRACTSIMULATION + DOT + Contract.ID, conSimulationId));
    	}	
		if (applicant != null) {
			addCriterion(Restrictions.eq(ContractSimulationApplicant.APPLICANT + DOT + Applicant.ID, applicant.getId()));
		}
		if (applicantType != null) {
			addCriterion(Restrictions.eq(ContractSimulationApplicant.APPLICANTTYPE, applicantType));
		}
	}

	/**
	 * @return the conSimulationId
	 */
	public Long getConSimulationId() {
		return conSimulationId;
	}

	/**
	 * @param conSimulationId the conSimulationId to set
	 */
	public void setConSimulationId(Long conSimulationId) {
		this.conSimulationId = conSimulationId;
	}

	/**
	 * @return the applicant
	 */
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the applicantType
	 */
	public EApplicantType getApplicantType() {
		return applicantType;
	}

	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(EApplicantType applicantType) {
		this.applicantType = applicantType;
	}

}
