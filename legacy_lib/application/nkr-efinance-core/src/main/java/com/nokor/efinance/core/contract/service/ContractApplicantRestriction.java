package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.ContractApplicant;

/**
 * Contract Applicant Restriction
 * @author bunlong.taing
 */
public class ContractApplicantRestriction extends BaseRestrictions<ContractApplicant> {
	/** */
	private static final long serialVersionUID = -4853171243065172488L;
	
	private Long conId;
	private Applicant applicant;
	private EApplicantType applicantType;

	/**
	 */
	public ContractApplicantRestriction() {
		super(ContractApplicant.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (conId != null) {
    		addCriterion(Restrictions.eq(ContractApplicant.CONTRACT + DOT + ContractApplicant.ID, conId));
    	}	
		if (applicant != null) {
			addCriterion(Restrictions.eq(ContractApplicant.APPLICANT + DOT + Applicant.ID, applicant.getId()));
		}
		if (applicantType != null) {
			addCriterion(Restrictions.eq(ContractApplicant.APPLICANTTYPE, applicantType));
		}
	}

	/**
	 * @return the conId
	 */
	public Long getConId() {
		return conId;
	}

	/**
	 * @param conId the conId to set
	 */
	public void setConId(Long conId) {
		this.conId = conId;
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
