package com.nokor.efinance.core.asset.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

/**
 * 
 * @author uhout.cheng
 */
public class AssetRestriction extends BaseRestrictions<Asset> implements MAsset {
	
	/** */
	private static final long serialVersionUID = -1883514032714612737L;
	
	private String chassisNumber;
	private String engineNumber;
	private String registrationNumber;
	private String contractId;
	
	/**
	 * 
	 */
    public AssetRestriction() {
		super(Asset.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(chassisNumber)) {
    		addCriterion(Restrictions.eq(CHASSISNUMBER, chassisNumber));
    	}
    	if (StringUtils.isNotEmpty(engineNumber)) {
    		addCriterion(Restrictions.eq(ENGINENUMBER, engineNumber));
    	}
    	if (StringUtils.isNotEmpty(registrationNumber)) {
    		addCriterion(Restrictions.eq(PLATENUMBER, registrationNumber));
    	}
    	if (StringUtils.isNotEmpty(contractId)) {
    		addAssociation("contracts", "con", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("con.reference", contractId));
    	}
	}

	/**
	 * @return the chassisNumber
	 */
	public String getChassisNumber() {
		return chassisNumber;
	}

	/**
	 * @param chassisNumber the chassisNumber to set
	 */
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	/**
	 * @return the engineNumber
	 */
	public String getEngineNumber() {
		return engineNumber;
	}

	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	/**
	 * @return the registrationNumber
	 */
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	/**
	 * @param registrationNumber the registrationNumber to set
	 */
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}
