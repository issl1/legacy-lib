package com.nokor.efinance.core.collection.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;

/**
 * 
 * @author uhout.cheng
 */
public class CollectionHistoryRestriction extends BaseRestrictions<CollectionHistory> {
	
	/** */
	private static final long serialVersionUID = -4770163863177073027L;
	
	private Long contractId;
	private EColResult colResult;
	private EColType[] colTypes;
	private ECallType[] callTypes;
	
	/**
	 * 
	 */
    public CollectionHistoryRestriction() {
		super(CollectionHistory.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (contractId != null) {
    		addCriterion(Restrictions.eq(CollectionHistory.CONTRACT + "." + CollectionHistory.ID, contractId));
    	}
    	if (colResult != null) {
    		addCriterion(Restrictions.eq(CollectionHistory.RESULT, colResult));
    	}
    	if (colTypes != null && colTypes.length > 0) {
			addCriterion(Restrictions.in(CollectionHistory.ORIGIN, colTypes));
		}
    	if (callTypes != null && callTypes.length > 0) {
			addCriterion(Restrictions.in(CollectionHistory.CALLTYPE, callTypes));
		}
    	addOrder(Order.desc(CollectionHistory.ID));
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
	 * @return the colResult
	 */
	public EColResult getColResult() {
		return colResult;
	}

	/**
	 * @param colResult the colResult to set
	 */
	public void setColResult(EColResult colResult) {
		this.colResult = colResult;
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
	 * @return the callTypes
	 */
	public ECallType[] getCallTypes() {
		return callTypes;
	}

	/**
	 * @param callTypes the callTypes to set
	 */
	public void setCallTypes(ECallType[] callTypes) {
		this.callTypes = callTypes;
	}

}
