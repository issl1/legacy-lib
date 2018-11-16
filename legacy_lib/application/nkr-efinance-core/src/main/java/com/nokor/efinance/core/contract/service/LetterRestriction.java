package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.Letter;

/**
 * 
 * @author uhout.cheng
 */
public class LetterRestriction extends BaseRestrictions<Letter> {

	/** */
	private static final long serialVersionUID = -7613636218481528245L;

	private String contractNo;
	
	/**
	 * 
	 */
    public LetterRestriction() {
		super(Letter.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(contractNo)) {
    		addAssociation(Contract.class, "CON", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("CON" + DOT + Contract.REFERENCE, contractNo));
    	}
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
