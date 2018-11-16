package com.nokor.efinance.core.collection.service;

import java.util.Date;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionActionRestriction extends BaseRestrictions<CollectionAction> {
	
	/** */
	private static final long serialVersionUID = 8550871190843952529L;
	
	private Long contractId;
	private EColAction colAction;
	private Date startDate;
	private Date endDate;
	private boolean unProcessed;
	
	/**
	 * 
	 */
    public CollectionActionRestriction() {
		super(CollectionAction.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (contractId != null) {
    		addCriterion(Restrictions.eq(CollectionAction.CONTRACT + "." + CollectionAction.ID, contractId));
    	}
    	if (colAction != null) {
    		addCriterion(Restrictions.eq(CollectionAction.COLACTION, colAction));
    	}
    	if (!unProcessed) {
    		if (startDate != null && endDate != null) {
    			addCriterion(Restrictions.ge(CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(startDate)));
    			addCriterion(Restrictions.le(CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtEndOfDay(endDate)));
        	} else if (startDate != null && endDate == null) {
        		addCriterion(Restrictions.ge(CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(startDate)));
        	} else if (startDate == null && endDate != null) {
        		addCriterion(Restrictions.ge(CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(endDate)));
    		}
    	} else {
    		addCriterion(Restrictions.isNull(CollectionAction.NEXTACTIONDATE));
    	}
    	addOrder(Order.desc(CollectionAction.ID));
	}

	/**
	 * @return the contractId
	 */
	public Long getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the colAction
	 */
	public EColAction getColAction() {
		return colAction;
	}

	/**
	 * @param colAction the colAction to set
	 */
	public void setColAction(EColAction colAction) {
		this.colAction = colAction;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the unProcessed
	 */
	public boolean isUnProcessed() {
		return unProcessed;
	}

	/**
	 * @param unProcessed the unProcessed to set
	 */
	public void setUnProcessed(boolean unProcessed) {
		this.unProcessed = unProcessed;
	}
	
}
