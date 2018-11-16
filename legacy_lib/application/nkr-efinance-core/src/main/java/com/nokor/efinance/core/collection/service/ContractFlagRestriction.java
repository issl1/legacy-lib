package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class ContractFlagRestriction extends BaseRestrictions<ContractFlag> {
	
	/** */
	private static final long serialVersionUID = -9090150704070013881L;
	
	private Long conId;
	private EFlag[] flags;
	private boolean completed = false;
	
	/**
	 * 
	 */
    public ContractFlagRestriction() {
		super(ContractFlag.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(ContractFlag.CONTRACT + DOT + Contract.ID, conId));
    	}
    	if (flags != null && flags.length > 0) {
			addCriterion(Restrictions.in(ContractFlag.FLAG, flags));
		}
    	if (completed) {
    		addCriterion(Restrictions.eq(ContractFlag.COMMPLETED, Boolean.TRUE));
    	}
    	addOrder(Order.desc(ContractFlag.ID));
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
	 * @return the flags
	 */
	public EFlag[] getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(EFlag[] flags) {
		this.flags = flags;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
