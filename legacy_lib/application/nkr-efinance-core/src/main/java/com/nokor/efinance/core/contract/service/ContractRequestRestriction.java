package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRequest;

/**
 * 
 * @author prasnar
 *
 */
public class ContractRequestRestriction extends BaseRestrictions<ContractRequest> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private Long usrId;
	private Long conId;
	private String contractNo;
	
	/**
	 * 
	 */
    public ContractRequestRestriction() {
		super(ContractRequest.class);
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
    	
    	if (StringUtils.isNotEmpty(contractNo)) {
    		addAssociation(Contract.class, "CON", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("CON" + DOT + Contract.REFERENCE, contractNo));
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
	
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
}
