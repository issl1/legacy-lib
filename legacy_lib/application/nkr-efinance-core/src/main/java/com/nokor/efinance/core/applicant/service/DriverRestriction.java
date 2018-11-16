package com.nokor.efinance.core.applicant.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.applicant.model.Driver;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class DriverRestriction extends BaseRestrictions<Driver> {
	
	/** */
	private static final long serialVersionUID = -8015565049769634219L;
	
	private Long conId;
	
	/**
	 * 
	 */
    public DriverRestriction() {
		super(Driver.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(Driver.CONTRACT + DOT + Contract.ID, conId));
    	}
    	addOrder(Order.desc(Driver.ID));
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
    
}
