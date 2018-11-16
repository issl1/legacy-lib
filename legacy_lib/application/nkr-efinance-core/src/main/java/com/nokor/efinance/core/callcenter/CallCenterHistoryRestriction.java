package com.nokor.efinance.core.callcenter;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;

/**
 * 
 * @author uhout.cheng
 */
public class CallCenterHistoryRestriction extends BaseRestrictions<CallCenterHistory> {
	
	/** */
	private static final long serialVersionUID = 9195801472564281056L;
	
	private Long contractId;
	private ECallCenterResult callCenterResult;
	
	/**
	 * 
	 */
    public CallCenterHistoryRestriction() {
		super(CallCenterHistory.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (contractId != null) {
    		addCriterion(Restrictions.eq(CallCenterHistory.CONTRACT + "." + CallCenterHistory.ID, contractId));
    	}
    	if (callCenterResult != null) {
    		addCriterion(Restrictions.eq(CallCenterHistory.RESULT, callCenterResult));
    	}
    	addOrder(Order.desc(CallCenterHistory.ID));
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
	 * @return the callCenterResult
	 */
	public ECallCenterResult getCallCenterResult() {
		return callCenterResult;
	}

	/**
	 * @param callCenterResult the callCenterResult to set
	 */
	public void setCallCenterResult(ECallCenterResult callCenterResult) {
		this.callCenterResult = callCenterResult;
	}

}
