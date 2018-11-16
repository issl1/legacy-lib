package com.nokor.efinance.core.collection.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Comment;

/**
 * 
 * @author uhout.cheng
 */
public class CommentRestriction extends BaseRestrictions<Comment> {

	/** */
	private static final long serialVersionUID = -2865202531635850497L;

	private String contractNo;
	
	/**
	 * 
	 */
    public CommentRestriction() {
		super(Comment.class);
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
