package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.EPromiseStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractPromise;

/**
 * 
 * @author uhout.cheng
 */
public class PromiseRestriction extends BaseRestrictions<ContractPromise> {

	/** */
	private static final long serialVersionUID = -4197099713735443206L;

	private Long contraId;
	private EPromiseStatus[] promiseStatuses;
	
	/**
	 * 
	 */
    public PromiseRestriction() {
		super(ContractPromise.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (contraId != null) {
    		addCriterion(Restrictions.eq(ContractPromise.CONTRACT + DOT + Contract.ID, contraId));
    	}
    	if (promiseStatuses != null && promiseStatuses.length > 0) {
			addCriterion(Restrictions.in(ContractPromise.PROMISESTATUS, promiseStatuses));
		}
    	addOrder(Order.desc(ContractPromise.ID));
    }

	/**
	 * @return the contraId
	 */
	public Long getContraId() {
		return contraId;
	}

	/**
	 * @param contraId the contraId to set
	 */
	public void setContraId(Long contraId) {
		this.contraId = contraId;
	}

	/**
	 * @return the promiseStatuses
	 */
	public EPromiseStatus[] getPromiseStatuses() {
		return promiseStatuses;
	}

	/**
	 * @param promiseStatuses the promiseStatuses to set
	 */
	public void setPromiseStatuses(EPromiseStatus[] promiseStatuses) {
		this.promiseStatuses = promiseStatuses;
	}

}
