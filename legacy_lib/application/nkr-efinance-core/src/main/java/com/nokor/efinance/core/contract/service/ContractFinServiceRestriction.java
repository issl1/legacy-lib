package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractFinService;

/**
 * 
 * @author uhout.cheng
 */
public class ContractFinServiceRestriction extends BaseRestrictions<ContractFinService> {
	
	/** */
	private static final long serialVersionUID = 2582866675612151742L;
	
	private Long contractId;
	private Long serviceId;
	
	/**
	 * 
	 */
	public ContractFinServiceRestriction() {
		super(ContractFinService.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		addOrder(Order.desc(ContractFinService.ID));
		if (contractId != null) {
			addCriterion(Restrictions.eq(ContractFinService.CONTRACT + DOT + ContractFinService.ID, contractId));
		}
		if (serviceId != null) {
			addCriterion(Restrictions.eq(ContractFinService.SERVICE + DOT + ContractFinService.ID, serviceId));
		}
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
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

}
