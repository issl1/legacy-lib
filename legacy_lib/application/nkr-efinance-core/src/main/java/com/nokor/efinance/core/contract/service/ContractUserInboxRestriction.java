package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.ContractUserInbox;

/**
 * 
 * @author prasnar
 *
 */
public class ContractUserInboxRestriction extends BaseRestrictions<ContractUserInbox> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private Long usrId;
	private Long conId;
	private Boolean userIsNull;
	private Integer debtLevel;
	private String profileCode;
	private String[] profileCodes;
	
	/**
	 * 
	 */
    public ContractUserInboxRestriction() {
		super(ContractUserInbox.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
 
    	if (usrId != null) {
    		addCriterion(Restrictions.eq("secUser.id", usrId));
    	} else if (userIsNull != null) {
    		if (isUserIsNull()) {
        		addCriterion(Restrictions.isNull("secUser"));
    		} else {
    			addCriterion(Restrictions.isNotNull("secUser"));
    		}
    	}
    	if (conId != null) {
    		addCriterion(Restrictions.eq("contract.id", conId));
    	}
    	if (debtLevel != null) {
    		addAssociation(ContractUserInbox.CONTRACT, "con", JoinType.INNER_JOIN);
    		addAssociation("con.collections", "col", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("col" + DOT + Collection.DEBTLEVEL, debtLevel));
    	}
    	
    	if (StringUtils.isNotEmpty(profileCode)) {
    		addAssociation("secUser", "usr", JoinType.INNER_JOIN);
    		addAssociation("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("pro.code", profileCode));
    	} else if (profileCodes != null && profileCodes.length > 0) {
    		addAssociation("secUser", "usr", JoinType.INNER_JOIN);
    		addAssociation("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
			addCriterion(Restrictions.in("pro.code", profileCodes));
		}
    	addOrder(Order.asc("contract.id"));
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
	 * @return the userIsNull
	 */
	public boolean isUserIsNull() {
		return userIsNull;
	}

	/**
	 * @param userIsNull the userIsNull to set
	 */
	public void setUserIsNull(boolean userIsNull) {
		this.userIsNull = userIsNull;
	}

	/**
	 * @return the debtLevel
	 */
	public Integer getDebtLevel() {
		return debtLevel;
	}

	/**
	 * @param debtLevel the debtLevel to set
	 */
	public void setDebtLevel(Integer debtLevel) {
		this.debtLevel = debtLevel;
	}

	/**
	 * @return the profileCode
	 */
	public String getProfileCode() {
		return profileCode;
	}

	/**
	 * @param profileCode the profileCode to set
	 */
	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}

	/**
	 * @return the profileCodes
	 */
	public String[] getProfileCodes() {
		return profileCodes;
	}

	/**
	 * @param profileCodes the profileCodes to set
	 */
	public void setProfileCodes(String[] profileCodes) {
		this.profileCodes = profileCodes;
	}
	
}
