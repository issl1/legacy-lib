package com.nokor.ersys.core.hr.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * Dealer account holder restriction
 * @author uhout.cheng
 */
public class OrgAccountHolderRestriction extends BaseRestrictions<OrgAccountHolder> {
	
	/** */
	private static final long serialVersionUID = 4684354351823272352L;
	
	private Organization organization;
	private Long accountHolderId;

	/**
	 */
	public OrgAccountHolderRestriction() {
		super(OrgAccountHolder.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (organization != null) {
			addCriterion(Restrictions.eq(OrgAccountHolder.ORGANIZATION, organization));
		}
		if (accountHolderId != null) {
			addCriterion(Restrictions.eq(OrgAccountHolder.ACCOUNTHOLDERID, accountHolderId));
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
	 * @return the accountHolderId
	 */
	public Long getAccountHolderId() {
		return accountHolderId;
	}

	/**
	 * @param accountHolderId the accountHolderId to set
	 */
	public void setAccountHolderId(Long accountHolderId) {
		this.accountHolderId = accountHolderId;
	}
	
}
