package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;

/**
 * 
 * @author prasnar
 *
 */
public class ContractUserSimulInboxRestriction extends BaseRestrictions<ContractUserSimulInbox> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private Long usrId;
	private Long conId;
	private EColType colType;
	private Integer[] debtLevels;
	private String profileCode;
	
	private boolean guaranteed;
	private boolean notGuaranteed;
	private boolean userNotNull;
	
	
	/**
	 * 
	 */
    public ContractUserSimulInboxRestriction() {
		super(ContractUserSimulInbox.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
 
    	if (usrId != null) {
    		addCriterion(Restrictions.eq("secUser.id", usrId));
    	}
    	
    	if (userNotNull) {
    		addCriterion(Restrictions.isNotNull("secUser"));
    	}
    	
    	if (conId != null) {
    		addCriterion(Restrictions.eq("contract.id", conId));
    	}
    	
    	if (colType != null) {
    		addCriterion(Restrictions.eq("colType", colType));
    	}
    	
    	if (debtLevels != null) {
    		addCriterion(Restrictions.in("debtLevel", debtLevels));
    	}
    	
    	if (StringUtils.isNotEmpty(profileCode)) {
    		addAssociation("profile", "pro", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("pro.code", profileCode));
    	}
    	
    	if (!(guaranteed && notGuaranteed)) {
    		if (guaranteed) {
    			addAssociation(ContractUserSimulInbox.CONTRACT, "con", JoinType.INNER_JOIN);
    			addCriterion(Restrictions.ge("con." + Contract.NUMBERGUARANTORS, 1));
    		}
    		if (notGuaranteed) {
    			addAssociation(ContractUserSimulInbox.CONTRACT, "con", JoinType.INNER_JOIN);
    			addCriterion(Restrictions.eq("con." + Contract.NUMBERGUARANTORS, 0));
    		}
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
	 * @return the colType
	 */
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}

	/**
	 * @return the debtLevels
	 */
	public Integer[] getDebtLevels() {
		return debtLevels;
	}

	/**
	 * @param debtLevels the debtLevels to set
	 */
	public void setDebtLevels(Integer[] debtLevels) {
		this.debtLevels = debtLevels;
	}

	/**
	 * @return the guaranteed
	 */
	public boolean isGuaranteed() {
		return guaranteed;
	}

	/**
	 * @param guaranteed the guaranteed to set
	 */
	public void setGuaranteed(boolean guaranteed) {
		this.guaranteed = guaranteed;
	}	

	/**
	 * @return the notGuaranteed
	 */
	public boolean isNotGuaranteed() {
		return notGuaranteed;
	}

	/**
	 * @param notGuaranteed the notGuaranteed to set
	 */
	public void setNotGuaranteed(boolean notGuaranteed) {
		this.notGuaranteed = notGuaranteed;
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
	 * @return the userNotNull
	 */
	public boolean isUserNotNull() {
		return userNotNull;
	}

	/**
	 * @param userNotNull the userNotNull to set
	 */
	public void setUserNotNull(boolean userNotNull) {
		this.userNotNull = userNotNull;
	}
}
