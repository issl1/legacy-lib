package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class ContractOperationRestriction extends BaseRestrictions<ContractOperation> {
	
	/** */
	private static final long serialVersionUID = 8353881687505215863L;
	
	private Long conId;
	
	/**
	 * 
	 */
    public ContractOperationRestriction() {
		super(ContractOperation.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(ContractOperation.CONTRACT + DOT + Contract.ID, conId));
    	}
    	addOrder(Order.desc(ContractOperation.ID));
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
