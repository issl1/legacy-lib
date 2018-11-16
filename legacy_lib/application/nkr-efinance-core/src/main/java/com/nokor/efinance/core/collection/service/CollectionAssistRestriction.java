package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionAssistRestriction extends BaseRestrictions<CollectionAssist> {
	
	/** */
	private static final long serialVersionUID = -4430549139667502844L;
	
	private Long conId;
	private EColType[] colTypes;
	private ERequestStatus[] requestStatuses;
	
	/**
	 * 
	 */
    public CollectionAssistRestriction() {
		super(CollectionAssist.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(CollectionAssist.CONTRACT + "." + Contract.ID, conId));
    	}
    	if (colTypes != null && colTypes.length > 0) {
			addCriterion(Restrictions.in(CollectionAssist.COLTYPE, colTypes));
		}
    	if (requestStatuses != null && requestStatuses.length > 0) {
			addCriterion(Restrictions.in(CollectionAssist.REQUESTSTATUS, requestStatuses));
		}
    	addOrder(Order.desc(CollectionAssist.ID));
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
	 * @return the colTypes
	 */
	public EColType[] getColTypes() {
		return colTypes;
	}

	/**
	 * @param colTypes the colTypes to set
	 */
	public void setColTypes(EColType[] colTypes) {
		this.colTypes = colTypes;
	}

	/**
	 * @return the requestStatuses
	 */
	public ERequestStatus[] getRequestStatuses() {
		return requestStatuses;
	}

	/**
	 * @param requestStatuses the requestStatuses to set
	 */
	public void setRequestStatuses(ERequestStatus[] requestStatuses) {
		this.requestStatuses = requestStatuses;
	}

}
