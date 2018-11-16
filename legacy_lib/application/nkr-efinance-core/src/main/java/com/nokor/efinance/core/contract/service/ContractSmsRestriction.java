package com.nokor.efinance.core.contract.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractSms;

/**
 * 
 * @author prasnar
 *
 */
public class ContractSmsRestriction extends BaseRestrictions<ContractSms> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private Long usrId;
	private Long conId;
	
	/**
	 * 
	 */
    public ContractSmsRestriction() {
		super(ContractSms.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
 
    	if (usrId != null) {
    		addCriterion(Restrictions.eq("user.id", usrId));
    	}
    	
    	if (conId != null) {
    		addCriterion(Restrictions.eq("contract.id", conId));
    	}		
	}


	/**
	 * @return the usrId
	 */
	public Long getUsrId() {
		return usrId;
	}


	/**
	 * @param usrId the usrId to set
	 */
	public void setUsrId(Long usrId) {
		this.usrId = usrId;
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
}
