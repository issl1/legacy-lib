package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionFlagRestriction extends BaseRestrictions<CollectionFlag> {
	
	/** */
	private static final long serialVersionUID = -4448418876013153540L;
	
	private Long conId;
	private EColType[] colTypes;
	private ERequestStatus[] requestStatuses;
	
	/**
	 * 
	 */
    public CollectionFlagRestriction() {
		super(CollectionFlag.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(CollectionFlag.CONTRACT + "." + Contract.ID, conId));
    	}
    	if (colTypes != null && colTypes.length > 0) {
			addCriterion(Restrictions.in(CollectionFlag.COLTYPE, colTypes));
		}
    	if (requestStatuses != null && requestStatuses.length > 0) {
			addCriterion(Restrictions.in(CollectionFlag.REQUESTSTATUS, requestStatuses));
		}
    	addOrder(Order.desc(CollectionFlag.ID));
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
