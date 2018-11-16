package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.FMEntityField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class FinServiceRestriction extends BaseRestrictions<FinService> implements FMEntityField {

	/**
	 */
	private static final long serialVersionUID = -8278020460329938423L;
	private EServiceType serviceType;
	
	public FinServiceRestriction() {
		super(FinService.class);
	}
	
	@Override
	public void preBuildSpecificCriteria() {
		if (serviceType != null) {
			addCriterion(Restrictions.eq("serviceType", serviceType));
		}
	}

	/**
	 * @return the serviceType
	 */
	public EServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(EServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
}
