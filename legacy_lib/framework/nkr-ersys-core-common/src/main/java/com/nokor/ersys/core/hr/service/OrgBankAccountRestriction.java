package com.nokor.ersys.core.hr.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * Dealer bank account restriction
 * @author uhout.cheng
 */
public class OrgBankAccountRestriction extends BaseRestrictions<OrgBankAccount> {
	
	/** */
	private static final long serialVersionUID = -8418181325874483194L;
	
	private Organization organization;
	private Long bankAccountId;

	/**
	 */
	public OrgBankAccountRestriction() {
		super(OrgBankAccount.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (organization != null) {
			addCriterion(Restrictions.eq(OrgBankAccount.ORGANIZATION, organization));
		}
		if (bankAccountId != null) {
			addCriterion(Restrictions.eq(OrgBankAccount.BANKACCOUNTID, bankAccountId));
		}
	}

	/**
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the bankAccountId
	 */
	public Long getBankAccountId() {
		return bankAccountId;
	}

	/**
	 * @param bankAccountId the bankAccountId to set
	 */
	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	
}
