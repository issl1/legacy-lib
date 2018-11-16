package com.nokor.ersys.core.hr.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * @author prasnar
 * 
 */
public class OrganizationRestriction extends BaseRestrictions<Organization> {
	/** */
	private static final long serialVersionUID = -4011295072884649680L;

	private Long orgId;
	private Long parentOrgId;
	private String name;
	private ETypeOrganization typeOrganization;
	private List<ESubTypeOrganization> subTypeOrganizations;
	private Boolean rootOnly;

	/**
	 * 
	 */
    public OrganizationRestriction() {
		super(Organization.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	addOrder(Order.asc(MOrganization.NAME));
    	if (name != null) {
    		addCriterion(Restrictions.or(
    				Restrictions.ilike(MOrganization.NAME, name, MatchMode.ANYWHERE), 
    				Restrictions.ilike(MOrganization.NAMEEN, name, MatchMode.ANYWHERE),
    				Restrictions.ilike(Organization.DESC, name, MatchMode.ANYWHERE), 
    				Restrictions.ilike(Organization.DESCEN, name, MatchMode.ANYWHERE),
    				Restrictions.ilike(MOrganization.CODE, name, MatchMode.ANYWHERE))
    				);
		}
		
    	if (orgId != null) {
    		addCriterion(Restrictions.eq(MOrganization.ID, orgId));
    	}
    	
    	if (Boolean.TRUE.equals(rootOnly)) {
    		addCriterion(Restrictions.isNull(MOrganization.PARENT + "." + MOrganization.ID));
    	} else if (parentOrgId != null) {
    		addCriterion(Restrictions.eq(MOrganization.PARENT + "." + MOrganization.ID, parentOrgId));
    	}
    	
    	if (typeOrganization != null) {
    		addCriterion(Restrictions.eq(MOrganization.TYPEORGANIZATION, typeOrganization));
    	}
    	
    	if (subTypeOrganizations != null && !subTypeOrganizations.isEmpty()) {
    		addCriterion(Restrictions.in(MOrganization.SUBTYPEORGANIZATION, subTypeOrganizations));
    	}
	}


	/**
	 * @return the SubTypeOrganization
	 */
	public List<ESubTypeOrganization> getSubTypeOrganizations() {
		return subTypeOrganizations;
	}


	/**
	 * @param SubTypeOrganization the SubTypeOrganization to set
	 */
	public void setSubTypeOrganizations(List<ESubTypeOrganization> SubTypeOrganizations) {
		this.subTypeOrganizations = SubTypeOrganizations;
	}
	
	/**
	 * @param subTypeOrganization
	 */
	public void addSubTypeOrganization(ESubTypeOrganization subTypeOrganization) {
		if (subTypeOrganization != null) {
			if (this.subTypeOrganizations == null) {
				this.subTypeOrganizations = new ArrayList<ESubTypeOrganization>();
			}
			this.subTypeOrganizations.add(subTypeOrganization);
		}
	}


	/**
	 * @param mainOnly the mainOnly to set
	 */
	public void setMainOnly() {
		typeOrganization = OrganizationTypes.MAIN;
	}
	
	/**
	 * @param insuranceOnly the insuranceOnly to set
	 */
	public void setInsuranceOnly() {
		typeOrganization = OrganizationTypes.INSURANCE;
	}


	/**
	 * @param outsourceAgentOnly the outsourceAgentOnly to set
	 */
	public void setOutsourceAgentOnly() {
		typeOrganization = OrganizationTypes.AGENT;
	}

	/**
	 * @return the rootOnly
	 */
	public Boolean getRootOnly() {
		return rootOnly;
	}


	/**
	 * @param rootOnly the rootOnly to set
	 */
	public void setRootOnly(Boolean rootOnly) {
		this.rootOnly = rootOnly;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the typeOrganization
	 */
	public ETypeOrganization getTypeOrganization() {
		return typeOrganization;
	}


	/**
	 * @param typeOrganization the typeOrganization to set
	 */
	public void setTypeOrganization(ETypeOrganization typeOrganization) {
		this.typeOrganization = typeOrganization;
	}


	/**
	 * @return the parentOrgId
	 */
	public Long getParentOrgId() {
		return parentOrgId;
	}


	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}


	/**
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}


	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/**
	 * 
	 * @param typeOrganization
	 * @param subTypeOrganization
	 * @param rootOnly
	 * @param parentOrgId
	 * @return
	 */
	public static OrganizationRestriction buildRestrictions(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId) {
		OrganizationRestriction restrictions = new OrganizationRestriction();
		restrictions.setTypeOrganization(typeOrganization);
		restrictions.addSubTypeOrganization(subTypeOrganization);
		restrictions.setRootOnly(rootOnly);
		restrictions.setParentOrgId(parentOrgId);
		
		return restrictions;
	}

	
}
