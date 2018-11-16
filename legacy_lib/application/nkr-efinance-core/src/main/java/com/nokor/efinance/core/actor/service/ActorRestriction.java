package com.nokor.efinance.core.actor.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.actor.model.Actor;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * 
 * @author prasnar
 *
 */
public class ActorRestriction extends BaseRestrictions<Actor> {

	/**
	 */
	private static final long serialVersionUID = -6851384305450773484L;
	
	private Organization financialCompany;
	private Dealer dealer;
	private Applicant applicant;
		
	/**
	 */
    public ActorRestriction() {
		super(Actor.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (financialCompany != null) {
    		addCriterion(Restrictions.eq("financialCompany.id", financialCompany.getId()));
    	}    		
    	if (dealer != null) {
    		addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
    	}
    	if (applicant != null) {
    		addCriterion(Restrictions.eq("applicant.id", applicant.getId()));
    	}
	}

	/**
	 * @return the financialCompany
	 */
	public Organization getFinancialCompany() {
		return financialCompany;
	}

	/**
	 * @param financialCompany the financialCompany to set
	 */
	public void setFinancialCompany(Organization financialCompany) {
		this.financialCompany = financialCompany;
	}

	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
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
}
