package com.nokor.ersys.core.hr.service;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * @author prasnar
 * 
 */
public class OrgStructureRestriction extends BaseRestrictions<OrgStructure> {
	/** */
	private static final long serialVersionUID = 2495476191772096636L;
	private String organizationName;
	private Long organizationId;
	private Long parentOrgStructureId;
	private Boolean parentOrgStructureNull;
	

	/**
	 * 
	 */
    public OrgStructureRestriction() {
		super(OrgStructure.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	addOrder(Order.asc(OrgStructure.LEVEL));
       	addOrder(Order.asc(OrgStructure.NAME));
    	if (organizationName != null) {
    		addCriterion(Restrictions.or(
    				Restrictions.ilike(OrgStructure.NAME, organizationName, MatchMode.ANYWHERE), 
    				Restrictions.ilike(OrgStructure.NAMEEN, organizationName, MatchMode.ANYWHERE),
    				Restrictions.ilike(OrgStructure.CODE, organizationName, MatchMode.ANYWHERE))
    				);
		}
		
    	if (Boolean.TRUE.equals(parentOrgStructureNull)) {
    		addCriterion(Restrictions.isNull(OrgStructure.PARENT));
    	} else if (Boolean.FALSE.equals(parentOrgStructureNull)) {
    		addCriterion(Restrictions.ne(OrgStructure.PARENT, null));
    	} else if (parentOrgStructureId != null) {
    		addCriterion(Restrictions.eq(OrgStructure.PARENT, parentOrgStructureId));
    	}
    	
    	if (organizationId != null) {
    		 addCriterion(Restrictions.eq(OrgStructure.ORGANIZATION + DOT + OrgStructure.ID, organizationId));
    	}
	}


	

	/**
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}


	/**
	 * @param organizationName the organizationName to set
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	/**
	 * @return the organizationId
	 */
	public Long getOrganizationId() {
		return organizationId;
	}


	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	/**
	 * @return the parentOrgStructureId
	 */
	public Long getParentOrgStructureId() {
		return parentOrgStructureId;
	}


	/**
	 * @param parentOrgStructureId the parentOrgStructureId to set
	 */
	public void setParentOrgStructureId(Long parentOrgStructureId) {
		this.parentOrgStructureId = parentOrgStructureId;
	}


	/**
	 * @return the parentOrgStructureNull
	 */
	public Boolean getParentOrgStructureNull() {
		return parentOrgStructureNull;
	}


	/**
	 * @param parentOrgStructureNull the parentOrgStructureNull to set
	 */
	public void setParentOrgStructureNull(Boolean parentOrgStructureNull) {
		this.parentOrgStructureNull = parentOrgStructureNull;
	}


	
}
