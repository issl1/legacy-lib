package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.InsuranceClaims;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceClaimsRestriction extends BaseRestrictions<InsuranceClaims> {
	
	/** */
	private static final long serialVersionUID = 5034814096003076681L;
	
	private Long insuranceId;

	/**
	 */
	public InsuranceClaimsRestriction() {
		super(InsuranceClaims.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		addOrder(Order.desc(InsuranceClaims.ID));
		if (insuranceId != null) {
			addCriterion(Restrictions.eq(InsuranceClaims.INSURANCE + DOT + InsuranceClaims.ID, insuranceId));
		}
	}

	/**
	 * @return the insuranceId
	 */
	public Long getInsuranceId() {
		return insuranceId;
	}

	/**
	 * @param insuranceId the insuranceId to set
	 */
	public void setInsuranceId(Long insuranceId) {
		this.insuranceId = insuranceId;
	}
	
}
