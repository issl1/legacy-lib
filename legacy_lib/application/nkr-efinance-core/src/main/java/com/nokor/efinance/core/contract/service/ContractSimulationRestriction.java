package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.contract.model.ContractSimulation;

/**
 * 
 * @author uhout.cheng
 */
public class ContractSimulationRestriction extends BaseRestrictions<ContractSimulation> {
	
	/** */
	private static final long serialVersionUID = 5837346768270738009L;
	
	private Long conId;
	private String applicationId;
	private EAfterSaleEventType afterSaleEventType;
	
	/**
	 * 
	 */
    public ContractSimulationRestriction() {
		super(ContractSimulation.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (conId != null) {
    		addCriterion(Restrictions.eq(ContractSimulation.CONTRACT + DOT + ContractSimulation.ID, conId));
    	}		
    	if (StringUtils.isNotEmpty(applicationId)) {
    		addCriterion(Restrictions.eq(ContractSimulation.EXTERNALREFERENCE, applicationId));
    	}
    	if (afterSaleEventType != null) {
    		addCriterion(Restrictions.eq(ContractSimulation.AFTERSALEEVENTTYPE, afterSaleEventType));
    	}
    	addOrder(Order.desc(ContractSimulation.ID));
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
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the afterSaleEventType
	 */
	public EAfterSaleEventType getAfterSaleEventType() {
		return afterSaleEventType;
	}

	/**
	 * @param afterSaleEventType the afterSaleEventType to set
	 */
	public void setAfterSaleEventType(EAfterSaleEventType afterSaleEventType) {
		this.afterSaleEventType = afterSaleEventType;
	}

}
