package com.nokor.common.app.workflow.service;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.workflow.model.EWkfFlow;

/**
 * @author prasnar
 * 
 */
public class WkfFlowRestriction extends BaseRestrictions<EWkfFlow> {
	/** */
	private static final long serialVersionUID = 706489846193494976L;

	private Long flowId;

    /**
     *
     */
    public WkfFlowRestriction() {
        super(EWkfFlow.class);
    }

    @Override
	public void preBuildAssociation() {
    	
	}

    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	
    }

	/**
	 * @return the flowId
	 */
	public Long getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	
    
}
