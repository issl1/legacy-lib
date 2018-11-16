package com.nokor.frmk.security.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecProfile;

/**
 * @author prasnar
 * 
 */
public class SecControlProfilePrivilegeRestriction extends BaseRestrictions<SecControlProfilePrivilege> {
	/** */
	private static final long serialVersionUID = 7177028168597671925L;

	private Long appId;
	private String appCode;
	private Long profileId;
	private String controlCode;
	private Long parentControlId;
	
	/**
	 * 
	 */
    public SecControlProfilePrivilegeRestriction() {
		super(SecControlProfilePrivilege.class);
	}

    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildAssociation()
     */
    public void preBuildAssociation() {
    	addAssociation(SecControlProfilePrivilege.PROFILE, "pro", JoinType.INNER_JOIN);
    	addAssociation(SecControlProfilePrivilege.CONTROL, "ctl", JoinType.INNER_JOIN);
    	addAssociation("ctl" + DOT + SecControl.APPLICATION, "app", JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (appCode != null) {
			addCriterion(Restrictions.eq("app" + DOT + SecApplication.CODE, appCode));
		}
    	if (appId != null) {
			addCriterion(Restrictions.eq("app" + DOT + SecApplication.ID, appId));
		}
    	
    	if (profileId != null) {
			addCriterion(Restrictions.eq("pro" + DOT + SecProfile.ID, profileId));
		}
    	
    	if (controlCode != null) {
			addCriterion(Restrictions.eq("ctl" + DOT + SecControl.CODE, controlCode));
		}
    	
    	if (parentControlId != null) {
        	addAssociation("ctl" + DOT + SecControl.PARENT, "ctlParent", JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.eq("ctlParent" + DOT + SecControl.ID, parentControlId));
    	}
	}


	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}


	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}


	/**
	 * @return the appCode
	 */
	public String getAppCode() {
		return appCode;
	}


	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * @return the profileId
	 */
	public Long getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the controlCode
	 */
	public String getControlCode() {
		return controlCode;
	}

	/**
	 * @param controlCode the controlCode to set
	 */
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}

	/**
	 * @return the parentControlId
	 */
	public Long getParentControlId() {
		return parentControlId;
	}

	/**
	 * @param parentControlId the parentControlId to set
	 */
	public void setParentControlId(Long parentControlId) {
		this.parentControlId = parentControlId;
	}


	

    
}
